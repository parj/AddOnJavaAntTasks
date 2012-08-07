package org.pm.xml;

import junit.framework.TestCase;
import org.custommonkey.xmlunit.Difference;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.List;


public class AntXMLUnitTest extends TestCase {
    private String _cFile = "src/test/resources/xml/AntXMLUnitTest/controlFile.xml";
    private String _tFile = "src/test/resources/xml/AntXMLUnitTest/testFile.xml";
    File cFile; File tFile;

    private AntXMLUnit antXMLUnit = new AntXMLUnit();

    protected void setUp() throws IOException {
        cFile = new File(_cFile);
        tFile = new File(_tFile);
    }

	public void testProcessFile() throws ParserConfigurationException, IOException, SAXException {
        List<Difference> differences = antXMLUnit.processFile(cFile, tFile);
        assertEquals(differences.size(), 10);
    }

    public void testInstance() throws ParserConfigurationException, IOException, SAXException {
        List<Difference> differences = antXMLUnit.processFile(cFile, tFile);
        assertTrue(differences.get(0) instanceof Difference);
    }
}
