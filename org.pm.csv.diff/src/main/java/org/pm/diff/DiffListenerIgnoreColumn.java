package org.pm.diff;

import java.util.List;

public class DiffListenerIgnoreColumn implements DiffListener {
	private List<String> columns;
	
	public DiffListenerIgnoreColumn(List<String> columns) {
		this.columns = columns;
	}
	
	public void add(String column) {
		this.add(column);
	}
	
	public boolean ignore(String column, String expected, String reached) {
		return columns.contains(column);
	}
}
