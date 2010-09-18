package org.pm.xml;

import org.apache.tools.ant.Task;

public class ModifyPath extends Task {
	private String path;
	private String value;
	private boolean delete;
	
	public ModifyPath() {}

	public void setPath(String path) {
		this.path = path;
	}

	public String getPath() {
		return path;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	public void setDelete(boolean delete) {
		this.delete = delete;
	}

	public boolean isDelete() {
		return delete;
	}
	
	public void execute() {}
}
