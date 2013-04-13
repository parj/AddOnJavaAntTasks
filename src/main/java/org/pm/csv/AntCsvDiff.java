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

package org.pm.csv;

import org.apache.log4j.Logger;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.types.FileSet;
import org.pm.diff.CsvReport;
import org.pm.diff.Report;

import java.io.File;
import java.util.Vector;

public class AntCsvDiff extends Task {
	private Vector<FileSet> fileSets = new Vector<FileSet>();
	private String keyColumns;
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

    			}
    		}
		}
	}	
}
