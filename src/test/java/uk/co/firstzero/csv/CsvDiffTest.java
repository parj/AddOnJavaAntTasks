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

import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import uk.co.firstzero.diff.MinimalReport;
import uk.co.firstzero.diff.exception.HeaderColumnsDoNotMatchException;
import uk.co.firstzero.diff.exception.KeyColumnsMissingException;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;


public class CsvDiffTest {
	MinimalReport report;
	CsvDiff csv;
	String keyColumns;

    @Before
	public void setUp() throws Exception {
		report = new MinimalReport();
		csv = new CsvDiff("src/test/resources/csv/CsvDiff/controlFile.csv", "src/test/resources/csv/CsvDiff/testFile.csv", ',');
		csv.setReport(report);
        keyColumns = "Header_1;Header_2";
	}

    @After
    public void tearDown() throws Exception {
		report = null;
		keyColumns = null;
		csv = null;
	}

    @Test
	public void testMissingKeyColumns() throws HeaderColumnsDoNotMatchException, IOException {
		try {
			csv.diff();
		} catch(KeyColumnsMissingException e) {
			assertTrue(e instanceof uk.co.firstzero.diff.exception.KeyColumnsMissingException);
		}
	}

    @Test
	public void testDiff() throws KeyColumnsMissingException, HeaderColumnsDoNotMatchException, IOException {
		csv.setKeyColumns(keyColumns);
		csv.diff();
		assertEquals(report.getNumberOfDifferences(), 3);
	}

	//TODO: ADD Mismatched header test
	//TODO: Add listener tests
}
