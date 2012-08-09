package org.pm.csv;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.pm.diff.Key;
import org.pm.diff.MinimalReport;
import org.pm.diff.exception.HeaderColumnsDoNotMatchException;
import org.pm.diff.exception.KeyColumnsMissingException;

import java.util.ArrayList;
import java.util.List;
import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.assertEquals;


public class CsvDiffTest {
	MinimalReport report;
	CsvDiff csv;
	List<Key> keyColumns;

    @Before
	public void setUp() throws Exception {
		report = new MinimalReport();
		csv = new CsvDiff("src/test/resources/csv/CsvDiff/controlFile.csv", "src/test/resources/csv/CsvDiff/testFile.csv", ',');
		csv.setReport(report);
		keyColumns = new ArrayList<Key>();
		keyColumns.add(new Key("Header_1"));
	}

    @After
    public void tearDown() throws Exception {
		report = null;
		keyColumns = null;
		csv = null;
	}

    @Test
	public void testMissingKeyColumns() throws HeaderColumnsDoNotMatchException {
		try {
			csv.diff();
		} catch(KeyColumnsMissingException e) {
			assertTrue(e instanceof org.pm.diff.exception.KeyColumnsMissingException);
		}
	}

    @Test
	public void testDiff() throws KeyColumnsMissingException, HeaderColumnsDoNotMatchException {
		csv.setKeyColumns(keyColumns);
		csv.diff();
		assertEquals(report.getNumberOfDifferences(), 3);
	}

	//TODO: ADD Mismatched header test
	//TODO: Add listener tests
}
