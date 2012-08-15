package org.pm.xml;

import org.apache.log4j.Logger;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.types.FileSet;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.soap.Node;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

public class AntXPath extends Task {
    Logger logger = Logger.getLogger(AntXPath.class);
    
	private Vector<FileSet> fileSets = new Vector<FileSet>();
	private List/*<Property>*/ modifyPaths = new ArrayList();
	private String outputDirectory;
	private String renamePattern;
	private String patternSplitter = "#";

	private DocumentBuilderFactory factory;
	private DocumentBuilder builder;
	private Transformer xFormer;
    private XPathFactory xPathFactory;

    public AntXPath() {
        
    }

    public XPathFactory getXPathFactory() {
        if(xPathFactory == null)
            xPathFactory = XPathFactory.newInstance();
        return xPathFactory;
    }
	
	public void addFileSet(FileSet fileset) {
		if (!fileSets.contains(fileset)) {
    	  fileSets.add(fileset);
      	}
	}
	
	public void addModifyPath(ModifyPath path) {
        modifyPaths.add(path);
    }
	
	/**
	 * Directory to write out processed file
	 * @param outputDirectory
	 */
	public void setOutputDirectory(String outputDirectory) {
		this.outputDirectory = outputDirectory;
	}

	/**
	 * Use xpaths to extract values from xml and rename file.
	 * Example //date or //date#_#//price
	 * @param renamePattern
	 */
	public void setRenamePattern(String renamePattern) {
		this.renamePattern = renamePattern;
	}

	public void execute() {
        try {
		    preSetup();
        } catch(ParserConfigurationException e) {
            logger.error("Unable to run preSetup() to setup dependencies");
            logger.error(e);
                e.printStackTrace();
            System.exit(1);
        } catch(TransformerConfigurationException e) {
            logger.error("Unable to run preSetup() to setup dependencies");
            logger.error(e);
            e.printStackTrace();
            System.exit(1);
        }
        
        DirectoryScanner ds;

		for (FileSet fileset : fileSets) {
			ds = fileset.getDirectoryScanner(getProject());
        	File dir = ds.getBasedir();
        	String[] filesInSet = ds.getIncludedFiles();
        	
        	 for (String filename : filesInSet) {
                 logger.debug("Processing " + filename);
        		 File f = new File(dir, filename);
        		 processFile(f);
        	 }
			 
		 }
	}
	
	private void preSetup() throws ParserConfigurationException, TransformerConfigurationException {
        factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        builder = factory.newDocumentBuilder();
        xFormer = TransformerFactory.newInstance().newTransformer();
	}
	
	private void processFile(File iFile) {
		try {
			Document doc = builder.parse(iFile);
			
			logger.trace("Parsed " + iFile.getAbsolutePath());
			
			for (Iterator paths = modifyPaths.iterator(); paths.hasNext(); ) {
				ModifyPath path = (ModifyPath)paths.next();
                logger.trace("Processing xPath - " + path.getPath());
                logger.trace("xPath value - " + path.getValue() + " : delete - " + path.isDelete());
				doc = processNode(path, doc);
			}
			
            writeDocument(doc, iFile.getName());

		} catch (XPathExpressionException e) {
            logger.error("Unable to evaluate xPath");
            logger.error(e);
            e.printStackTrace();
        } catch (TransformerException e) {
            logger.error("Unable to run write output xml file");
			logger.error(e);
            e.printStackTrace();
		} catch (IOException e) {
            logger.error("Unable to read input file");
			logger.error(e);
            e.printStackTrace();
        } catch (SAXException e) {
            logger.error("Unable to read input file");
			logger.error(e);
            e.printStackTrace();
        }
	}
	
	public Document processNode(ModifyPath path, Document doc) throws XPathExpressionException {
        XPath xPath = getXPathFactory().newXPath();

		//Evaluate XPaths
		XPathExpression expr = xPath.compile(path.getPath());
		Object xPathResult = expr.evaluate(doc, XPathConstants.NODESET);
		NodeList nodes = (NodeList) xPathResult;
		
		//Process each node
		for (int index = 0; index < nodes.getLength(); index++) {
			if (path.isDelete()) {
				delete(nodes.item(index));
				logger.trace("Removed node - " + path.getPath());
			}
			else {
				nodes.item(index).setTextContent(path.getValue());
				logger.trace("Modified node - " + path.getPath() + " with Value - " + path.getValue());
			}
		}
		
		return doc;
	}
	
	private void delete(org.w3c.dom.Node node) {
		if(node != null) {
			if(node.getNodeType() == Node.ELEMENT_NODE) {
				node.getParentNode().removeChild(node);
				logger.trace("Removed Element - " + node.toString());
			}
			else {
				Attr a = (Attr)node;
				a.getOwnerElement().removeAttributeNode(a);
				logger.trace("Removed Attribute - " + node.toString());
			}
		}
	}
	
	private String getNewFileName(Document doc, String fileName) {

        XPath xPath = xPathFactory.newXPath();
		StringBuffer sBuffer = new StringBuffer();
		
		try {
			//Check if renamePattern has been set
			if (renamePattern.length() > 0) {
                logger.trace("renamePattern - " + renamePattern);

				//Split the string
				String[] patterns = renamePattern.split(patternSplitter);
				
				for(String pattern : patterns) {
					//Evaluate XPaths
					if (pattern.startsWith("/")) {
						XPathExpression expr = xPath.compile(pattern);
						String xPathResult = (String)expr.evaluate(doc, XPathConstants.STRING);
                        logger.trace("xPath Pattern Result - " + xPathResult);

                        sBuffer.append(xPathResult);
					}
					else
						sBuffer.append(pattern);
                    
                    logger.trace("String buffer - "+ sBuffer.toString());
				}
				
				sBuffer.append(".xml");
			
			    return sBuffer.toString();
		    }
			else
				return fileName;
		} catch (Exception e) {
			logger.error("Error evaluating filename");
            logger.error(e);
			e.printStackTrace();
			return fileName;
		}
	}

    private void writeDocument(Document doc, String fileName) throws TransformerException {
        Source source = new DOMSource(doc);

        //Generate output filename
        String oFileName = getNewFileName(doc, fileName);
        File oFile = new File(outputDirectory + "/" + oFileName);
        Result result = new StreamResult(oFile);

        logger.debug("About to write to file " + oFile.getAbsolutePath());
        xFormer.transform(source, result);
    }
}