package com.axway;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.apache.commons.io.FilenameUtils;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

public abstract class AbstractGatewayPackage extends AbstractMojo {

	@Parameter
	private String name;
	@Parameter
	private String targetPassphrase;
	@Parameter
	private String projectPassphrase;

	@Parameter
	private String projpackPath;

	@Parameter
	private String polFilePath;

	@Parameter(defaultValue = "${project.basedir}/target", required = true)
	private File targetFolder;

	@Component
	private MavenProject project;

	private Log log = getLog();

	public AbstractGatewayPackage() {
		// TODO Auto-generated constructor stub

	}
	
	protected abstract String packageType();
	
	

	@Override
	public void execute() throws MojoExecutionException, MojoFailureException {

		try {
			
			String type = packageType();
			
			File outputFolder = new File(targetFolder, "axway");
			outputFolder.mkdir();

			log.info("Extracting dependent projects....");
			List<String> inputParam = new ArrayList<>();
			inputParam.add(projpackPath);
			inputParam.add("--create");
			inputParam.add("--passphrase-none");
			inputParam.add("--name");
			inputParam.add(name);
			inputParam.add("--type");
			inputParam.add(type);

			File[] files = new Filter().finder(new File(targetFolder, "alternateLocation"));
			for (int i = 0; i < files.length; i++) {
				inputParam.add("--add");
				
				log.info("Processing the file : " + files[i].getName());
				String projDir = extractJar(files[i], outputFolder);

				inputParam.add(projDir);
				inputParam.add("--projpass-none");
			}

			inputParam.add("--dir");
			inputParam.add(targetFolder.getPath());

			log.info(inputParam.toString());
			ProcessBuilder pb = new ProcessBuilder(inputParam);
			pb.redirectErrorStream(true);
			Process process = pb.start();

			BufferedReader br = null;

			try {
				br = new BufferedReader(new InputStreamReader(process.getInputStream()));
				String line = null;
				log.info("-----------------------------------------------------");
				while ((line = br.readLine()) != null) {
					log.info(line);
				}
				log.info("-----------------------------------------------------");
			} finally {
				br.close();
			}
			
			String generatedFile = targetFolder.getPath()+File.separator+name;

			File artifact = new File(generatedFile+"."+type);
			
			log.info("Output path" + artifact.getPath());
			
			project.getArtifact().setFile(artifact);
			log.info("Packaging complete");

		} catch (Exception e) {
			log.error(e);

		}

		log.info("Deployment Complete....");
	}

	private String extractJar(File filename, File destDir) throws IOException {

		String name = FilenameUtils.removeExtension(filename.getName());
		destDir = new File(destDir, name);
		destDir.mkdir();

		JarFile jarFile = new JarFile(filename);
		Enumeration<JarEntry> jarEntries = jarFile.entries();

		while (jarEntries.hasMoreElements()) {
			JarEntry jarEntry = jarEntries.nextElement();
			File file = new java.io.File(destDir, jarEntry.getName());
			if (!file.exists()) {
				file.getParentFile().mkdirs();
				file = new File(destDir, jarEntry.getName());
			}
			if (jarEntry.isDirectory()) {
				continue;
			}
			InputStream inputStream = jarFile.getInputStream(jarEntry);
			FileOutputStream fileOutputStream = new java.io.FileOutputStream(file);
			while (inputStream.available() > 0) {
				fileOutputStream.write(inputStream.read());
			}
			fileOutputStream.close();
			inputStream.close();

		}

		jarFile.close();

		return destDir.getAbsolutePath();
	}

	public class Filter {

		public File[] finder(File dir) {

			return dir.listFiles(new FilenameFilter() {
				public boolean accept(File dir, String filename) {
					return filename.endsWith(".jar");
				}
			});

		}

	}

}
