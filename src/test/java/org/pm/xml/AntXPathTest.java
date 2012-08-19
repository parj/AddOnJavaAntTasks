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

package org.pm.xml;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.IOException;

import static junit.framework.Assert.assertEquals;

public class AntXPathTest {
    private String iFile = "src/test/resources/xml/AntXPathTest/controlFile.xml";
    private DocumentBuilderFactory factory;
	private DocumentBuilder builder;
    private AntXPath antXPath = new AntXPath();

    @Before
    public void setUp() throws ParserConfigurationException, TransformerConfigurationException {
        factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        builder = factory.newDocumentBuilder();
    }

    @Test
	public void testModifyXPath() throws IOException, SAXException, XPathExpressionException {
        String path = "//book[@id = 'bk102']/title";
        Document document = builder.parse(iFile);
        ModifyPath modifyPath = new ModifyPath();
        modifyPath.setPath(path);
        modifyPath.setValue("FOO");
        document = antXPath.processNode(modifyPath, document);

        XPathFactory xPathFactory = XPathFactory.newInstance();
        XPath xPath = xPathFactory.newXPath();
        assertEquals(xPath.evaluate(path, document), "FOO");
    }

    @Test
    public void testDeleteXPath() throws IOException, SAXException, XPathExpressionException {
        String path = "//book[@id = 'bk104']/author";
        Document document = builder.parse(iFile);
        ModifyPath modifyPath = new ModifyPath();
        modifyPath.setPath(path);
        modifyPath.setDelete(true);
        document = antXPath.processNode(modifyPath, document);

        XPathFactory xPathFactory = XPathFactory.newInstance();
        XPath xPath = xPathFactory.newXPath();
        assertEquals(xPath.evaluate(path, document), "");
    }

    @After
    public void tearDown() {
        factory = null;
        builder = null;
        antXPath = null;
    }
}
