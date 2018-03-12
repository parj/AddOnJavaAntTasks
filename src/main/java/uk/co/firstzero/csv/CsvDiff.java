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

package uk.co.firstzero.csv;

import au.com.bytecode.opencsv.CSVReader;
import org.apache.log4j.Logger;
import uk.co.firstzero.diff.DiffListener;
import uk.co.firstzero.diff.Difference;
import uk.co.firstzero.diff.Report;
import uk.co.firstzero.diff.TwoWayHashMap;
import uk.co.firstzero.diff.exception.HeaderColumnsDoNotMatchException;
import uk.co.firstzero.diff.exception.KeyColumnsMissingException;

import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class CsvDiff {
	private char delimiter;
	private String numberOfHeaders = "1";
	private String expectedFile;
	private String reachedFile;
	private List<DiffListener> listeners;
	private String keyColumns;
    private String[] keyColumnsToArray;
	private Report report;

	private HashMap<String, String[]> missingExpected;
	private HashMap<String, String[]> missingReached;
	private TwoWayHashMap headers;

	private int intNumberOfHeaders = 1;

	private static Logger logger = Logger.getLogger(CsvDiff.class);

	public CsvDiff(String expectedFile, String reachedFile, char delimiter) {
		this.missingExpected = new HashMap<String, String[]>();
		this.missingReached = new HashMap<String, String[]>();
		this.headers = new TwoWayHashMap();
		setExpectedFile(expectedFile);
		setReachedFile(reachedFile);
		setDelimiter(delimiter);
	}

	public void setDelimiter(char delimiter) {
		logger.trace("Setting delimiter to " + delimiter);
		this.delimiter = delimiter;
	}

	public char getDelimiter() {
		return delimiter;
	}

	public void setNumberOfHeaders(String numberOfHeaders) {
		logger.trace("Setting numberOfHeaders to " + numberOfHeaders);
		try {
			intNumberOfHeaders = new Integer(numberOfHeaders).intValue();
			this.numberOfHeaders = numberOfHeaders;
		} catch (Exception e) {
			System.out.println("Unable to convert to integer");

		}
	}

	public String getNumberOfHeaders() {
		return numberOfHeaders;
	}

	public void setExpectedFile(String expectedFile) {
		logger.trace("Setting expectedFile to " + expectedFile);
		this.expectedFile = expectedFile;
	}

	public String getExpectedFile() {
		return expectedFile;
	}

	public void setReachedFile(String reachedFile) {
		logger.trace("Setting reachedFile to " + reachedFile);
		this.reachedFile = reachedFile;
	}

	public String getReachedFile() {
		return reachedFile;
	}

	public void setListeners(List<DiffListener> listeners) {
		logger.trace("Setting number of listeners to " + listeners.size());
		this.listeners = listeners;
	}

	public List<DiffListener> getListeners() {
		return listeners;
	}

	public void setKeyColumns(String keyColumns) {
		logger.trace("Setting number of keyColumns to " + keyColumns.length());
		this.keyColumns = keyColumns;
        keyColumnsToArray = keyColumns.split(";");
	}

	public String getKeyColumns() {
		return keyColumns;
	}

	public void diff() throws KeyColumnsMissingException, HeaderColumnsDoNotMatchException, IOException {
		logger.debug("START diff");

		CSVReader fileE; CSVReader fileR;
		String[] expected; String[] reached;
		String keyExpected; String keyReached;
		int rowCount = 0;
		int totalMismatches = 0;

		if (keyColumns == null || keyColumnsToArray.length == 0)
			throw new KeyColumnsMissingException("Missing key columns");

		try {
			logger.trace("Initialising CSVReader for " + this.expectedFile);
			fileE = new CSVReader(new FileReader(this.expectedFile), this.delimiter);

			logger.trace("Initialising CSVReader for " + this.reachedFile);
			fileR = new CSVReader(new FileReader(this.reachedFile), this.delimiter);

			logger.trace("Determining Headers");
			determineHeaders(fileE, fileR);

            expected = fileE.readNext();
            reached = fileR.readNext();

			//Loop through the files
			while(!(expected == null && reached == null)) {

				logger.trace("Read from expected " + expected);
				logger.trace("Read from reached " + reached);

				keyExpected = "";
				keyReached = "";

				//Combine the key columns for each row
				for (int i = 0; i < keyColumnsToArray.length; ++i) {
					//Lookup location of key column
					int keyHeaderLocation = headers.get(keyColumnsToArray[i]).intValue();

                    if (expected != null) {
                        if (keyExpected.length() == 0 )
                            keyExpected = expected[keyHeaderLocation];
                        else
                            keyExpected = keyExpected + "_" + expected[keyHeaderLocation];
                    }

                    if (reached != null) {
                        if (keyReached.length() == 0)
                            keyReached = reached[keyHeaderLocation];
                        else
                            keyReached = keyReached + "_" + reached[keyHeaderLocation];
                    }

					logger.trace("Building Expected key " + keyExpected);
					logger.trace("Building Reached key " + keyReached);
				}

                logger.debug("Expected key " + keyExpected);
                logger.debug("Reached key " + keyReached);

				//Keys have matched from both files
				if (keyExpected.compareTo(keyReached) == 0) {
					logger.debug("KEYS " + keyExpected + " " + keyExpected + " MATCHED");
                    totalMismatches += diffColumns(expected, reached, keyExpected, rowCount);
				} else {
                    if (expected != null && (!"".equals(keyExpected)))
                        this.missingExpected.put(keyExpected, expected);

                    if (reached != null && (!"".equals(keyReached)))
                        this.missingReached.put(keyReached, reached);
                }

				//Check if the key from reached file is in the missing set of expected
				if (this.missingExpected.containsKey(keyReached)) {
					logger.debug("FOUND REACHED KEY " + keyReached + " in missingExpected");

                    String[] localExpected, localReached;
                    String localKeyExpected;

					localExpected = this.missingExpected.get(keyReached);
                    localReached = reached;
                    localKeyExpected = keyReached;

					this.missingExpected.remove(keyReached);
                    this.missingReached.remove(keyReached);

					totalMismatches += diffColumns(localExpected, localReached, localKeyExpected, rowCount);
				}

				//Check if the key from expected file is in the missing set of reached
				if (this.missingReached.containsKey(keyExpected)) {
					logger.debug("FOUND EXPECTED KEY " + keyExpected + " in missingReached");

                    String[] localExpected, localReached;
                    String localKeyReached;

					localReached = this.missingReached.get(keyExpected);
                    localExpected = expected;
                    localKeyReached = keyExpected;

					this.missingReached.remove(keyExpected);
                    this.missingExpected.remove(keyExpected);

                    totalMismatches += diffColumns(localExpected, localReached, localKeyReached, rowCount);
				}


                expected = null;
                reached = null;

                expected = fileE.readNext();
                reached = fileR.readNext();

				rowCount += 1;
			}

			for (Iterator<String> i = this.missingExpected.keySet().iterator() ; i.hasNext();) {
				Difference difference = new Difference(new Integer(rowCount), null, "Missing Expected", i.next(), null, null);
				logger.info(difference.toString());

				if (report != null) report.write(difference);

			}

			for (Iterator<String> i = this.missingReached.keySet().iterator() ; i.hasNext();) {
				Difference difference = new Difference(new Integer(rowCount), null, "Missing Reached", null,  i.next(), null);
				logger.info(difference.toString());

				if (report != null) report.write(difference);
			}

			System.out.println("Number of mismatches: " + totalMismatches);
			System.out.println("Number of missing Control lines: " + this.missingExpected.size());
			System.out.println("Number of missing Test lines: " + this.missingReached.size());

		} catch(java.io.IOException e) {
			System.out.println("Unable to read expected or reached file");
			throw e;
		}
	}

	private int diffColumns(String[] expected, String[] reached, String keyExpected, int rowCount) {
		logger.debug("START diffColumns");

		int totalMismatches = 0;

		for (int i = 0; i < expected.length; ++i) {
			logger.trace("Column at line " + rowCount + " column " + i + " in expected " + expected[i] + " - in reached " + reached[i]);

			String control; String test;
			control = expected[i];
			test = reached[i];
			//If control = test => ignore = true
			boolean ignore = (control.compareTo(test) == 0) ? true : false;

			logger.trace("Before firing listeners - ignore is " + ignore);

			if (control.compareTo(test) != 0) {
				if (listeners != null) {
					for (Iterator<DiffListener> iterator = listeners.iterator();;) {
						DiffListener listener = iterator.next();

						boolean iteratorIgnore = listener.ignore(headers.get(i), control, test);

						//If any of the listener comes back true mark ignore True
						if (iteratorIgnore)
							ignore = iteratorIgnore;
					}
				}
			}

			logger.trace("After firing listeners - ignore is " + ignore);

			if (!ignore) {
				totalMismatches += 1;
				try {
					Difference difference = new Difference(new Integer(rowCount), headers.get(i), "MISMATCH", control, test, keyExpected);
					logger.info(difference.toString());
					report.write(difference);
				} catch (IOException e) {

				}
			}
		}

		logger.debug("STOP diffColumns");
		return totalMismatches;
	}

	private void determineHeaders(CSVReader fileE, CSVReader fileR) throws HeaderColumnsDoNotMatchException {
		logger.debug("START determineHeaders");

		String[] headerExpected; String[] headerReached;

		try {
			for(int i = 1; i <= this.intNumberOfHeaders; ++i) {
				headerExpected = fileE.readNext();
				headerReached = fileR.readNext();

				logger.trace("Read from expected " + headerExpected.toString());
				logger.trace("Read from reached " + headerReached.toString());
				logger.debug("Number of columns in header for " + fileE + " at line " + i + " - " + headerExpected.length);
				logger.debug("Number of columns in header for " + fileR + " at line " + i + " - " + headerReached.length);

				if (headerExpected.length != headerReached.length)
					throw new HeaderColumnsDoNotMatchException("Number of header columns do not match on line " + intNumberOfHeaders);
				else {
					for(int j = 0; j < headerExpected.length; ++j) {
						logger.trace("Header at line " + i + " column " + j + " " + headerExpected[j]);

						//Add the header to the header hashmap
						if (!headers.containsKey(new Integer(j))) {
							logger.trace("Adding header - " + headerExpected[j]);
							headers.put(new Integer(j), headerExpected[j]);
						}

						//If the column number is same and text is different, splice them together
						else if (headerExpected[j] != headers.get(new Integer(j))) {
							String header = headers.get(new Integer(j));
							headers.remove(new Integer(j));
							header = header + '_' + headerExpected[j];
							logger.trace("Adding header - " + header);
							headers.put(new Integer(j), header);
						}
					}
				}
			}
		} catch(java.io.IOException e) {
			System.out.println("Unable to read files");

		}

		logger.trace("STOP determineHeaders");
	}

	public void setReport(Report report) {
		this.report = report;
	}

	public Report getReport() {
		return report;
	}
}
