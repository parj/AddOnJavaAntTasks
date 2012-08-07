package org.pm.csv;

import org.apache.log4j.Logger;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.types.FileSet;

import java.util.Vector;

public class AntCsvToExcel extends Task {
    private static Logger logger = Logger.getLogger(AntCsvToExcel.class);
	private Vector<FileSet> fileSets = new Vector<FileSet>();
	private String outputFile;
	private String separator = ",'";
	
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

	public void execute() {
		DirectoryScanner ds;
		
		for (FileSet fileset : fileSets) {
			//Read in the control files
			ds = fileset.getDirectoryScanner(getProject());
        	String[] filesInSet = ds.getIncludedFiles();
    		
    		for (String filename : filesInSet) {
                logger.debug("Processing " + filename);
    			
    			try {
    				CsvToExcel csv = new CsvToExcel(filename, this.outputFile ,this.separator);
        			csv.execute();
    			} catch (Exception e) {
    				logger.error("Unable to process " + filename);
                    logger.error(e.getMessage());
    				e.printStackTrace();
    			}
    		}
		}
	}
}
