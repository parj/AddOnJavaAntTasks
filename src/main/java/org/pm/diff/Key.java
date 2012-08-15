package org.pm.diff;

import org.apache.tools.ant.Project;
import org.apache.tools.ant.types.DataType;

public class Key extends DataType {
	String name;
	
	public Key(String name) {
		this.name = name;
	}

	public void addText(String name) {
		Project project = getProject();
		this.name = project.replaceProperties(name);
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return this.name;
	}
}