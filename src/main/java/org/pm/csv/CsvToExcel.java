package org.pm.csv;

import au.com.bytecode.opencsv.CSVReader;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class CsvToExcel {
	private String outputFile;
	private String inputFile;
	private char separator = ',';
	
	private static Logger logger = Logger.getLogger(CsvToExcel.class);
	
	public CsvToExcel(String inputFile, String outputFile) { 
		setInputFile(inputFile);
		setOutputFile(outputFile);
	}
	
	public CsvToExcel(String inputFile, String outputFile, char separator) {
		setInputFile(inputFile);
		setOutputFile(outputFile);
		setSeparator(separator);
	}

	/**
	 * CSV separator
	 * @param separator
	 */
	public void setSeparator(char separator) {
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

	public void execute() throws IOException, WriteException {
        //Input File
		logger.trace("Getting name of " + this.inputFile);
		String name = new File(this.inputFile).getName();
        CSVReader reader = new CSVReader(new FileReader(this.inputFile), separator);

        //Output file
		logger.trace("Initialising oFile " + this.outputFile);
		File oFile = new File(this.outputFile);

        //Create Sheet
		WritableWorkbook workbook = Workbook.createWorkbook(oFile);
		WritableSheet s = workbook.createSheet(name, 0);

        //Initialise variables
		String[] line; int row = 0; int count = 1;

        //Loop through file
		while ((line = reader.readNext()) != null) {
			logger.debug("Line read " + line);
			int column = 0;

			//Add a cell on the same row
			while (column < line.length) {
                String columnText = line[column];
				logger.trace("TOKEN - " + columnText);

                if (columnText.length() > 0)
				    s.addCell(new Label(column, row, columnText));
                
                column++;
			}

			//Next line
			row++;

			if (row >= 64000) {
				logger.trace("Moving to the next sheet " + name + "_sheet" + count);
				s = workbook.createSheet(name + "_sheet" + count++, 0);
			}
		}
        workbook.write();
        workbook.close();
	}

	public void setInputFile(String inputFile) {
		this.inputFile = inputFile;
	}


	public String getInputFile() {
		return inputFile;
	}
}
