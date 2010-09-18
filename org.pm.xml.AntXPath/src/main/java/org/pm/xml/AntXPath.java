package org.pm.xml;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.soap.Node;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.types.FileSet;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

public class AntXPath extends Task {
	private Vector<FileSet> fileSets = new Vector<FileSet>();
	private List/*<Property>*/ modifyPaths = new ArrayList();
	private boolean verbose = false;
	private String outputDirectory;
	private String renamePattern;
	private String patternSplitter = "#";
	
	private DocumentBuilderFactory factory;
	private DocumentBuilder builder;
	private XPathFactory xPathFactory;
	private XPath xpath;
	private Transformer xformer;
	
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

	/**
	 * For enabling verbose logging
	 * @param verbose
	 */
	public void setVerbose(boolean verbose) {
			this.verbose = verbose;
	}
	
	public void execute() {
		DirectoryScanner ds;
		preSetup();

		for (FileSet fileset : fileSets) {
			ds = fileset.getDirectoryScanner(getProject());
        	File dir = ds.getBasedir();
        	String[] filesInSet = ds.getIncludedFiles();
        	
        	 for (String filename : filesInSet) {
        		 if (verbose)
        			 log("Processing " + filename);
        		 File f = new File(dir, filename);
        		 processFile(f);
        	 }
			 
		 }
	}
	
	private void preSetup() {
		try {
			factory = DocumentBuilderFactory.newInstance();
			factory.setNamespaceAware(true);
			builder = factory.newDocumentBuilder();
			xPathFactory = XPathFactory.newInstance();
			xpath = xPathFactory.newXPath();
			xformer = TransformerFactory.newInstance().newTransformer();
		} catch (Exception e) {
			System.out.println("Unable to set up dependencies");
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	private void processFile(File iFile) {
		try {
			Document doc = builder.parse(iFile);
			
			if (verbose)	log("Parsed " + iFile.getAbsolutePath());
			
			for (Iterator paths = modifyPaths.iterator(); paths.hasNext(); ) {
				ModifyPath path = (ModifyPath)paths.next();
				doc = processNode(path, doc);
			}
			
			Source source = new DOMSource(doc);
			String oFileName = getNewFileName(doc, iFile.getName());
			File oFile = new File(outputDirectory + "/" + oFileName); 
			Result result = new StreamResult(oFile);
			xformer.transform(source, result); 
			
			if (verbose)	log("Written to file " + oFile.getAbsolutePath());
		} catch (Exception e) {
			System.out.println("Unable to process " + iFile.toString());
			e.printStackTrace();
		}
	}
	
	private Document processNode(ModifyPath path, Document doc) throws Exception {
		//Evaluate XPaths
		XPathExpression expr = xpath.compile(path.getPath());
		Object xPathResult = expr.evaluate(doc, XPathConstants.NODESET);
		NodeList nodes = (NodeList) xPathResult;
		
		//Process each node
		for (int index = 0; index < nodes.getLength(); index++) {
			if (path.isDelete()) {
				delete(nodes.item(index));
				if (verbose)	log("Removed node - " + path.getPath());
			}
			else {
				nodes.item(index).setTextContent(path.getValue());
				if (verbose)	
					log("Modified node - " + path.getPath() + " with Value - " + path.getValue());
			}
		}
		
		return doc;
	}
	
	private void delete(org.w3c.dom.Node node) throws Exception {
		if(node != null) {
			if(node.getNodeType() == Node.ELEMENT_NODE) {
				node.getParentNode().removeChild(node);
				System.out.println("Removed Element - " + node.toString());
			}
			else {
				Attr a = (Attr)node;
				a.getOwnerElement().removeAttributeNode(a);
				System.out.println("Removed Attribute - " + node.toString());
			}
		}
	}
	
	private String getNewFileName(Document doc, String fileName) {
		StringBuffer sBuffer = new StringBuffer();
		
		try {
			//Check if renamePattern has been set
			if (renamePattern.length() > 0) {
				//Split the string
				String[] patterns = renamePattern.split(patternSplitter);
				
				for(String pattern : patterns) {
					//Evaluate XPaths
					if (pattern.startsWith("/")) {
						XPathExpression expr = xpath.compile(pattern);
						String xPathResult = (String)expr.evaluate(doc, XPathConstants.STRING);
						sBuffer.append(xPathResult);
					}
					else
						sBuffer.append(pattern);
				}
				
				sBuffer.append(".xml");
			
			return sBuffer.toString();
			}
			else
				return fileName;
		} catch (Exception e) {
			System.err.println("Error evaluating filename");
			e.printStackTrace();
			return fileName;
		}
	}
}