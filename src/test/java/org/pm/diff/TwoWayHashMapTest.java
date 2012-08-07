package org.pm.diff;

import junit.framework.TestCase;

public class TwoWayHashMapTest extends TestCase {
	private TwoWayHashMap twoMap;
	
	protected void setUp() throws Exception {
		super.setUp();
		twoMap = new TwoWayHashMap();
		twoMap.put("luhIUHi", 99000);
		twoMap.put(298, "KLUHa");
		twoMap.put("l;kJ", 9812);
		twoMap.put(28, "`;lij");
		twoMap.put("^&%VETI", 1298);
		twoMap.put(287149, "I&To7y8");
		twoMap.put("lhnILBI8�Č��", 98912387);
		twoMap.put(28723, "�^wh");
	}

	protected void tearDown() throws Exception {
		super.tearDown();
		twoMap = null;
	}

	public void testPutStringInteger() {
		twoMap.put("ABCD", 1234);
		assertEquals(twoMap.get(1234), "ABCD");
	}

	public void testPutIntegerString() {
		twoMap.put(5678, "EFGH");
		assertEquals((Integer)twoMap.get("EFGH"), new Integer(5678));
	}

	public void testRemoveString() {
		twoMap.remove(1234);
		assertFalse(twoMap.containsKey("ABCD"));
	}
	public void testRemoveInteger() {
		twoMap.remove("EFGH");
		assertFalse(twoMap.containsKey(5678));
	}

}
