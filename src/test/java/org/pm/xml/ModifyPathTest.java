package org.pm.xml;

import org.pm.xml.ModifyPath;

import junit.framework.TestCase;

public class ModifyPathTest extends TestCase {

	
	public void testGetSetPath() {
		ModifyPath path = new ModifyPath();
		path.setPath("/root");
		assertEquals("GetSetPath", path.getPath(), "/root");
		path = null;
	}

	public void testGetSetValue() {
		ModifyPath path = new ModifyPath();
		path.setValue("foo");
		assertEquals(path.getValue(), "foo");
		path = null;
	}
	
	public void testGetSetDelete() {
		ModifyPath path = new ModifyPath();
		path.setDelete(false);
		assertEquals(path.isDelete(), false);
		path = null;
	}

}
