/*
Copyright (c) 2011, 2012 Parjanya Mudunuri

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
associated documentation files (the "Software"), to deal in the Software without restriction, including
without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to
the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial
portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT
LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

http://opensource.org/licenses/mit-license.php
 */

package org.pm.diff;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;

public class TwoWayHashMapTest {
	private TwoWayHashMap twoMap;

    @Before
	public void setUp()  {
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

    @After
	public void tearDown() throws Exception {
		twoMap = null;
	}

    @Test
	public void testPutStringInteger() {
		twoMap.put("ABCD", 1234);
		assertEquals(twoMap.get(1234), "ABCD");
	}

    @Test
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
