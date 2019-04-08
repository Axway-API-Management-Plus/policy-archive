package com.axway;

import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;

@Mojo(name = "pol", defaultPhase = LifecyclePhase.PACKAGE)
public class PolPackage extends AbstractGatewayPackage {

	public PolPackage() {
		// TODO Auto-generated constructor stub
	}
	
	@Override
	protected String packageType() {
		// TODO Auto-generated method stub
		return "pol";
	}


}
