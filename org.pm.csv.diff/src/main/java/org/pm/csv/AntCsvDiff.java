package org.pm.csv;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.types.FileSet;
import org.pm.diff.CsvReport;
import org.pm.diff.Key;
import org.pm.diff.KeyColumns;
import org.pm.diff.Report;

public class AntCsvDiff extends Task {
	private Vector<FileSet> fileSets = new Vector<FileSet>();
	private List<Key> keyColumns = new ArrayList<Key>();
	private String testDirectory;
	private String resultDirectory;
	private char separator = ',';
	private static Logger logger = Logger.getLogger(AntCsvDiff.class);
	
	public Vector<FileSet> getFileSets() {
		return fileSets;
	}

	public void setFileSets(Vector<FileSet> fileSets) {
		this.fileSets = fileSets;
	}

	public String getTestDirectory() {
		return testDirectory;
	}
	
	public void setTestDirectory(String testDirectory) {
		this.testDirectory = testDirectory;
	}
	
	public void addKeyColumns(KeyColumns keyColumns) {
		for(Key key : keyColumns.getKeyColumns()) {
			logger.trace("Adding key via addKeyColumns - " + key.getName());
			this.keyColumns.add(key);
		}
	}

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
	 * CSV separator
	 * @param separator
	 */
	public void setSeparator(char separator) {
		this.separator = separator;
	}
	
	public char getSeparator() {
		return this.separator;
	}
	
	public void setResultDirectory(String resultDirectory) {
		this.resultDirectory = resultDirectory;
	}


	public String getResultDirectory() {
		return resultDirectory;
	}

	public void execute() {
		DirectoryScanner ds;
		
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
    			logger.debug("Processing " + filename);
    			
    			try {
    				logger.trace("Initialising controlFile - " + controlDir + "/" + filename);
    				File controlFile = new File(controlDir, filename);
    				
    				logger.trace("Initialising testFile - " + testDir + "/" + filename);
	        		File testFile = new File(testDir, filename);
	        		
	        		logger.trace("Initialising report");
	        		String name = controlFile.getName();
	        		Report report = new CsvReport(this.resultDirectory + "/report_" + name + ".csv");
	        		
    				CsvDiff csv = new CsvDiff(controlFile.getCanonicalPath(), testFile.getCanonicalPath(), this.separator);
    				csv.setReport(report);
    				csv.setKeyColumns(keyColumns);
    				csv.diff();
    			} catch (Exception e) {
    				System.out.println("Unable to process " + filename);
    				e.printStackTrace();
    			}
    		}
		}
	}	
}
