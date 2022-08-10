package com.axway;

import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

public class TestArchive {

    @Test
    public void testExtractJar(){

        File jarFile = new File("src/test/resources/policy-archive-1.0.0.jar");
        File destDir = new File("src/test/temp");
        AbstractGatewayPackage abstractGatewayPackage = new FedPackage();
        try {
            abstractGatewayPackage.extractJar(jarFile, destDir);
        } catch (IOException e) {
            throw new RuntimeException(e);
           
        }
    }
}
