package org.pm.xml;

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

}
