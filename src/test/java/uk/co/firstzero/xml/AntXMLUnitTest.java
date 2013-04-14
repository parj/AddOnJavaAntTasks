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

import org.custommonkey.xmlunit.Difference;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;


public class AntXMLUnitTest {
    private String _cFile = "src/test/resources/xml/AntXMLUnitTest/controlFile.xml";
    private String _tFile = "src/test/resources/xml/AntXMLUnitTest/testFile.xml";
    File cFile; File tFile;

    private AntXMLUnit antXMLUnit;

    @Before
    public void setUp() throws IOException {
        antXMLUnit = new AntXMLUnit();
        cFile = new File(_cFile);
        tFile = new File(_tFile);
    }

    @Test
	public void testProcessFile() throws ParserConfigurationException, IOException, SAXException {
        List<Difference> differences = antXMLUnit.processFile(cFile, tFile);
        assertEquals(differences.size(), 10);
    }

    @Test
    public void testInstance() throws ParserConfigurationException, IOException, SAXException {
        List<Difference> differences = antXMLUnit.processFile(cFile, tFile);
        assertTrue(differences.get(0) instanceof Difference);
    }

    @After
    public void tearDown() {
        cFile = null;
        tFile = null;
        antXMLUnit = null;
    }
}
