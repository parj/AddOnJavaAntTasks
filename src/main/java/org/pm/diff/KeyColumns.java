package org.pm.diff;

import java.util.ArrayList;
import java.util.List;

import org.apache.tools.ant.types.DataType;
import org.pm.diff.Key;

public class KeyColumns extends DataType {
	private List<Key> keyColumns = new ArrayList<Key>();
	
	public List<Key> getKeyColumns() {
		return this.keyColumns;
	}
	
	public void addKey(Key key) {
		this.keyColumns.add(key);
	}
}
