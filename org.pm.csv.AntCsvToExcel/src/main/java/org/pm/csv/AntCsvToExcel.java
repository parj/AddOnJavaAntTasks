package org.pm.csv;

import java.util.Vector;

import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.types.FileSet;

public class AntCsvToExcel extends Task {
	private Vector<FileSet> fileSets = new Vector<FileSet>();
	private String outputFile;
	private String separator = ",'";
	private boolean verbose = false;
	
	public AntCsvToExcel() { }
	

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
	public void setSeparator(String separator) {
		this.separator = separator;
	}


	/**
	 * Set the output excel file
	 * @param outputFile
	 */
	public void setOutputFile(String outputFile) {
		this.outputFile = outputFile;
	}

	public void setVerbose(boolean verbose) {
		this.verbose = verbose;
	}

	public void execute() {
		DirectoryScanner ds;
		
		for (FileSet fileset : fileSets) {
			//Read in the control files
			ds = fileset.getDirectoryScanner(getProject());
        	String[] filesInSet = ds.getIncludedFiles();
    		
    		for (String filename : filesInSet) {
    			if (verbose)
    				log("Processing " + filename);
    			
    			try {
    				CsvToExcel csv = new CsvToExcel(filename, this.outputFile ,this.separator);
        			csv.execute();
    			} catch (Exception e) {
    				System.out.println("Unable to process " + filename);
    				e.printStackTrace();
    			}
    		}
		}
	}
}
