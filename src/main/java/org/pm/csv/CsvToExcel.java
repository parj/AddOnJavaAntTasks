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
	private String[] inputFile;
    private String baseDir;
	private char separator = ',';
	
	private static Logger logger = Logger.getLogger(CsvToExcel.class);
	
	public CsvToExcel(String[] inputFile, String outputFile, String baseDir) {
		setInputFile(inputFile);
		setOutputFile(outputFile);
        setBaseDir(baseDir);
	}
	
	public CsvToExcel(String[] inputFile, String outputFile, String baseDir, char separator) {
		setInputFile(inputFile);
		setOutputFile(outputFile);
        setBaseDir(baseDir);
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

    public void setBaseDir(String baseDir) {
        logger.trace("Setting basedir to " + baseDir);
        this.baseDir = baseDir;
    }


	/**
	 * Set the output excel file
	 * @param outputFile
	 */
	public void setOutputFile(String outputFile) {
		logger.trace("Setting outputFile to " + outputFile);
		this.outputFile = outputFile;
	}

    public void setInputFile(String[] inputFile) {
        logger.trace("Setting inputFile to " + inputFile);
        this.inputFile = inputFile;
    }

    public static WritableWorkbook createWorkbook(String outputFile) throws IOException {
        //Output file
        logger.trace("Initialising oFile " + outputFile);
        File oFile = new File(outputFile);

        return Workbook.createWorkbook(oFile);
    }

	public void execute() throws IOException, WriteException {

        WritableWorkbook workbook = createWorkbook(outputFile);

        for (int i = 0; i < inputFile.length; ++i) {
            String filename = inputFile[i];
            logger.debug("Processing " + filename);

            String name = filename;

            if (name.length() > 12)
                name = name.substring(0, 12);

            CSVReader reader = new CSVReader(new FileReader(this.baseDir + File.separator + filename), separator);

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
        }
        workbook.write();
        workbook.close();
	}
}
