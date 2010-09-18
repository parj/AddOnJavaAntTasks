package org.pm.xml;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import javax.swing.text.html.HTMLDocument.Iterator;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.TransformerFactory;
import javax.xml.xpath.XPathFactory;

import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.types.FileSet;
import org.custommonkey.xmlunit.DetailedDiff;
import org.custommonkey.xmlunit.Diff;
import org.custommonkey.xmlunit.Difference;
import org.custommonkey.xmlunit.DifferenceConstants;
import org.custommonkey.xmlunit.DifferenceEngine;
import org.custommonkey.xmlunit.DifferenceListener;
import org.custommonkey.xmlunit.XMLUnit;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

public class AntXMLUnit extends Task {
	private Vector<FileSet> fileSets = new Vector<FileSet>();
	private boolean verbose = false;
	private String testDirectory;
	private String resultDirectory;
	private String separator = ",";
	
	private DocumentBuilderFactory factory;
	private DocumentBuilder builder;
	private static List ignoreIds = new ArrayList();
	
	public AntXMLUnit() { } 
	
	/**
	 * Input file set
	 * @param fileset
	 */
	public void addFileSet(FileSet fileset) {
		if (!fileSets.contains(fileset)) {
    	  fileSets.add(fileset);
      	}
	}
	
	/**
	 * The directory from which to read the test xml files
	 * @param testDirectory
	 */
	public void setTestDirectory(String testDirectory) {
		this.testDirectory = testDirectory;
	}
	
	/**
	 * The directory to which to publish the csv report
	 * @param resultDirectory
	 */
	public void setResultDirectory(String resultDirectory) {
		this.resultDirectory = resultDirectory;
	}
	
	public void setSeparator(String separator) {
		this.separator = separator;
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
		
		//Check if the test directory exists
		File testDir = new File(testDirectory);
    	if (!testDir.exists()) {
    		System.err.println(testDirectory + " does not exist");
    		System.exit(1);
    	}
		
		for (FileSet fileset : fileSets) {
			//Read in the control files
			ds = fileset.getDirectoryScanner(getProject());
        	File controlDir = ds.getBasedir();
        	String[] filesInSet = ds.getIncludedFiles();
        	
        	 for (String filename : filesInSet) {
        		 log("Processing " + filename);
        		 
        		 try {
	        		 File controlFile = new File(controlDir, filename);
	        		 File testFile = new File(testDir, filename);
	        		 processFile(controlFile, testFile);
        		 } catch(Exception e) {
        			 System.out.println("Unable to process - " + filename);
        			 e.printStackTrace();
        		 }
        	 }
			 
		 }
	}
	
	/**
	 * Sets up xml document factory and xmlunit constants
	 */
	private void preSetup() {
		try {
			factory = DocumentBuilderFactory.newInstance();
			factory.setNamespaceAware(true);
			builder = factory.newDocumentBuilder();
			
			XMLUnit.setIgnoreAttributeOrder(true);
			XMLUnit.setIgnoreWhitespace(true);
			
			ignoreIds.add(DifferenceConstants.CHILD_NODELIST_SEQUENCE);
			ignoreIds.add(DifferenceConstants.CHILD_NODELIST_SEQUENCE_ID);
		} catch (Exception e) {
			System.out.println("Unable to set up dependencies");
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	private void processFile(File controlFile, File testFile) {
		try {
			Document control = builder.parse(controlFile);
			Document test = builder.parse(testFile);
			Writer report = new BufferedWriter(new FileWriter(resultDirectory + "/" + controlFile.getName() + ".csv"));
			String header = "Diff ID" + this.separator + "Description" + this.separator 
							+ "Control Value" + this.separator + "Test Value" + this.separator
							+ "XPath" + "\n";
			report.write(header);
			
			Diff diff = new Diff(control, test);
			DetailedDiff dd = new DetailedDiff(diff);
			List l = dd.getAllDifferences();
			
			for (Object difference_ : l) {
				Difference difference = (Difference)difference_;
				
				if (ignoreIds.contains(difference.getId())) {
					if (verbose)
						log("SKIP - " 
							+ difference.getId() + this.separator
							+ difference.getDescription() + this.separator
							+ difference.getControlNodeDetail().getXpathLocation());
				}
				else {
					String differenceString = difference.getId() + this.separator 
						+ difference.getDescription() + this.separator
						+ difference.getControlNodeDetail().getValue() + this.separator 
						+ difference.getTestNodeDetail().getValue() + this.separator
						+ difference.getControlNodeDetail().getXpathLocation();
					
					report.write(differenceString + "\n");
					
					if (verbose) log("DIFF - " + differenceString);
				}
			}
			
			if (l.size() == 0) System.out.println(controlFile.getAbsolutePath() + "/" + controlFile.getName() + "No difference found");
			
			report.close();
		} catch(Exception e) {
			System.out.println("Unable to process - " + controlFile.getAbsolutePath() + " - " + testFile.getAbsolutePath());
		}
	}
}
