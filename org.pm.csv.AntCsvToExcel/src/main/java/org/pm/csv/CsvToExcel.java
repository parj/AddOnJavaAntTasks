package org.pm.csv;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

public class CsvToExcel {
	private String outputFile;
	private String inputFile;
	private String separator = ",";
	
	private static Logger logger = Logger.getLogger(CsvToExcel.class);
	
	public CsvToExcel(String inputFile, String outputFile) { 
		setInputFile(inputFile);
		setOutputFile(outputFile);
	}
	
	public CsvToExcel(String inputFile, String outputFile, String separator) { 
		setInputFile(inputFile);
		setOutputFile(outputFile);
		setSeparator(separator);
	}

	/**
	 * CSV separator
	 * @param separator
	 */
	public void setSeparator(String separator) {
		logger.trace("Setting separator to " + separator);
		this.separator = separator;
	}


	/**
	 * Set the output excel file
	 * @param outputFile
	 */
	public void setOutputFile(String outputFile) {
		logger.trace("Setting outputFile to " + outputFile);
		this.outputFile = outputFile;
	}

	public void execute() {
		try {
			logger.trace("Getting name of " + this.inputFile);
			String name = new File(this.inputFile).getName();
			BufferedReader reader  = new BufferedReader(new FileReader(this.inputFile));
			
			logger.trace("Initialising oFile " + this.outputFile);
			File oFile = new File(this.outputFile);
    		
			WritableWorkbook workbook = Workbook.createWorkbook(oFile);
			WritableSheet s = workbook.createSheet(name, 0);
			
			String line; int row = 0; int count = 1;
			
			while ((line = reader.readLine()) != null) {
				logger.trace("Line read " + line);
				int column = 0;
				
				//Split the string
				StringTokenizer st = new StringTokenizer(line, this.separator);
				
				//Add a cell on the same row
				while (st.hasMoreTokens()) {
					String token = st.nextToken();
					
					logger.trace("TOKEN - " + token);
					
					s.addCell(new Label(column++, row, token));
				}
				
				//Next line
				row++;
				
				if (row >= 64000) {
					logger.trace("Moving to the next sheet " + name + "_sheet" + count);
					s = workbook.createSheet(name + "_sheet" + count++, 0);
				}
				
				workbook.write();
        		workbook.close();
			}
		} catch (Exception e) {
			System.out.println("Unable to process " + getInputFile());
			e.printStackTrace();
		}
	}

	public void setInputFile(String inputFile) {
		this.inputFile = inputFile;
	}


	public String getInputFile() {
		return inputFile;
	}
}
