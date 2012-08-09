package org.pm.xml;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class ModifyPathTest {

	@Test
	public void testGetSetPath() {
		ModifyPath path = new ModifyPath();
		path.setPath("/root");
		assertEquals("GetSetPath", path.getPath(), "/root");
		path = null;
	}

    @Test
	public void testGetSetValue() {
		ModifyPath path = new ModifyPath();
		path.setValue("foo");
		assertEquals(path.getValue(), "foo");
		path = null;
	}

    @Test
	public void testGetSetDelete() {
		ModifyPath path = new ModifyPath();
		path.setDelete(false);
		assertEquals(path.isDelete(), false);
		path = null;
	}

}
