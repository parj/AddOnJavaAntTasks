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

package uk.co.firstzero.xml;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ModifyPathTest {
    ModifyPath path;

    @Before
    public void setUp() {
        path = new ModifyPath();
    }

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

    @After
    public void tearDown() {
        path = null;
    }

}
