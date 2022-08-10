package com.axway;

import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;

@Mojo(name = "fed", defaultPhase = LifecyclePhase.PACKAGE)
public class FedPackage extends AbstractGatewayPackage {

	public FedPackage() {
	}

	@Override
	protected String packageType() {
		return "fed";
	}

}
