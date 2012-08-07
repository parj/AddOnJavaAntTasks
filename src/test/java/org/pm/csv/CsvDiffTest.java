package org.pm.csv;

import junit.framework.TestCase;
import org.pm.diff.Key;
import org.pm.diff.MinimalReport;
import org.pm.diff.exception.HeaderColumnsDoNotMatchException;
import org.pm.diff.exception.KeyColumnsMissingException;

import java.util.ArrayList;
import java.util.List;

public class CsvDiffTest extends TestCase {
	MinimalReport report;
	CsvDiff csv;
	List<Key> keyColumns;

	protected void setUp() throws Exception {
		super.setUp();
		report = new MinimalReport();
		csv = new CsvDiff("src/test/resources/controlFile.csv", "src/test/resources/testFile.csv", ',');
		csv.setReport(report);
		keyColumns = new ArrayList<Key>();
		keyColumns.add(new Key("Header_1"));
	}

	protected void tearDown() throws Exception {
		super.tearDown();
		report = null;
		keyColumns = null;
		csv = null;
	}

	public void testMissingKeyColumns() throws HeaderColumnsDoNotMatchException {
		try {
			csv.diff();
		} catch(KeyColumnsMissingException e) {
			assertTrue(e instanceof org.pm.diff.exception.KeyColumnsMissingException);
		}
	}

	public void testDiff() throws KeyColumnsMissingException, HeaderColumnsDoNotMatchException {
		csv.setKeyColumns(keyColumns);
		csv.diff();
		assertEquals(report.getNumberOfDifferences(), 3);
	}

	//TODO: ADD Mismatched header test
	//TODO: Add listener tests
}
