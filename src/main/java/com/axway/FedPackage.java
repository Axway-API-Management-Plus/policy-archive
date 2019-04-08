package com.axway;

import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;

@Mojo(name = "fed", defaultPhase = LifecyclePhase.PACKAGE)
public class FedPackage extends AbstractGatewayPackage {

	public FedPackage() {
		// TODO Auto-generated constructor stub
	}

	@Override
	protected String packageType() {
		// TODO Auto-generated method stub
		return "fed";
	}

}
