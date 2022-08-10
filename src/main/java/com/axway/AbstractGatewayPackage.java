package com.axway;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import java.io.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

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
    private final Log log = getLog();
    public AbstractGatewayPackage() {
    }

    protected abstract String packageType();

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        try {
            String type = packageType();
            File outputFolder = new File(targetFolder, "axway");
            if(!outputFolder.mkdir()){
                throw new MojoFailureException("Unable to create a local directory named axway");
            }
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
            for (File file : files) {
                inputParam.add("--add");
                log.info("Processing the file : " + file.getName());
                String projDir = extractJar(file, outputFolder);
                inputParam.add(projDir);
                inputParam.add("--projpass-none");
            }
            inputParam.add("--dir");
            inputParam.add(targetFolder.getPath());
            log.info(inputParam.toString());
            ProcessBuilder pb = new ProcessBuilder(inputParam);
            pb.redirectErrorStream(true);
            Process process = pb.start();

            try (BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                log.info("-----------------------------------------------------");
                while ((line = br.readLine()) != null) {
                    log.info(line);
                }
                log.info("-----------------------------------------------------");
            }
            int status = process.waitFor();
            log.info("Status value " + status);
            if (status == 0) {
                log.info("Exit value " + process.exitValue());
                if (process.exitValue() != 0) {
                    throw new MojoFailureException("Error with projpack");
                }
            } else {
                throw new MojoFailureException("Error with projpack");
            }
            String generatedFile = targetFolder.getPath() + File.separator + name;
            File artifact = new File(generatedFile + "." + type);
            log.info("Output path" + artifact.getPath());
            project.getArtifact().setFile(artifact);
            log.info("Packaging complete");
        } catch (Exception e) {
            throw new MojoFailureException("Error with projpack :" + e.getMessage());
        }
        log.info("projpack Complete....");
    }

    public String extractJar(File filename, File destDir) throws IOException {

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
            InputStream inputStream = null;
            FileOutputStream fileOutputStream = null;
            try {
                inputStream = jarFile.getInputStream(jarEntry);
                fileOutputStream = new FileOutputStream(file);
                IOUtils.copy(inputStream, fileOutputStream);
                fileOutputStream.close();
                inputStream.close();
            } finally {
                IOUtils.closeQuietly(fileOutputStream);
                IOUtils.closeQuietly(inputStream);
            }
        }
        jarFile.close();
        return destDir.getAbsolutePath();
    }

    public static class Filter {
        public File[] finder(File dir) {
            return dir.listFiles((dir1, filename) -> filename.endsWith(".jar"));
        }
    }
}
