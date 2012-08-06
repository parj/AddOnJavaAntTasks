package org.pm.diff;

public interface DiffListener {
	public boolean ignore(String column, String expected, String reached);
}
