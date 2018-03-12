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

package uk.co.firstzero.diff;

import au.com.bytecode.opencsv.CSVWriter;

import java.io.FileWriter;
import java.io.IOException;

/**
 * Produces a CSV Report that holds all the differences
 */
public class CsvReport implements Report {
	private CSVWriter writer;
	private String separator = ",";

    /**
     * Default constructor
     * @param fileName  Name &amp; Path of the file to write to
     * @throws IOException IO Exception
     */
	public CsvReport(String fileName) throws IOException {
		open(fileName);
	}

    /**
     * Opens the file for writing
     * @param fileName Name &amp; Path of the file to write to
     * @throws IOException IO Exception
     */
	public void open(String fileName) throws IOException {
		writer = new CSVWriter(new FileWriter(fileName), separator.charAt(0));
		writeHeader();
	}

    /**
     * Writes header to the file
     */
	public void writeHeader() {
		String[] result = {
			"Line Number",
            "Key",
            "Column",
			"Mismatch Type",
			"Expected",
			"Reached",
			"Difference"
		};
		writer.writeNext(result);
	}

    /**
     * Writes difference
     * @param difference    The difference found
     * @throws IOException IO Exception
     */
	public void write(Difference difference) throws IOException {
		float expected;
		float reached;
		String diff;

		try {
			expected = Float.valueOf((String)difference.getExpected());
			reached = Float.valueOf((String)difference.getExpected());
			diff = Float.toString(expected - reached);
		}	catch (Exception e) {
            //If the above is not a number
            diff = "";
		}

        String[] result = {
        					difference.getLineNumber().toString(),
                            difference.getKey(),
                            difference.getColumn(),
                            difference.getMismatchType(),
        					difference.getExpected(),
        					difference.getReached(),
        					diff
        			};
		writer.flush();
	}

    /**
     * Closes the file
     * @throws IOException IO Exception
     */
	public void close() throws IOException {
		if (writer != null) {
			writer.flush();
			writer.close();
		}
	}



}
