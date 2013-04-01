package org.pm.csv;


import jxl.Workbook;
import jxl.read.biff.BiffException;
import org.apache.log4j.Logger;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.types.FileSet;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static junit.framework.Assert.assertEquals;

public class AntCsvToExcelTest {
    private static Logger logger = Logger.getLogger(AntCsvToExcel.class);
    private final String OUT_FILE = "out/report.xls";
    private Workbook workbook;

    AntCsvToExcel antCsvToExcel;
    FileSet set;

    @Before
    public void setUp() {
        logger.debug("In setUp");

        antCsvToExcel = new AntCsvToExcel();
        antCsvToExcel.setProject(new Project());

        logger.trace("Setting output file - out/report.xls");
        antCsvToExcel.setOutputFile(OUT_FILE);

        set = new FileSet();
        set.setProject(antCsvToExcel.getProject());

        logger.trace("Setting dir - src/test/resources/csv/CsvToExcel");
        set.setDir(new File("src/test/resources/csv/CsvToExcel"));
        set.setIncludes("*.csv");

        antCsvToExcel.addFileSet(set);
    }

    @Test
    public void testNumberOfFiles() {
        logger.debug("In testNumberOfFiles");

        logger.debug("Set size - " + set.size());
        assertEquals(2, set.size());
    }

    @Test
    public void testNumberOfSheets() throws BiffException, IOException {
        logger.debug("In testNumberOfSheets");

        antCsvToExcel.execute();

        workbook = Workbook.getWorkbook(new File(OUT_FILE));

        assertEquals(2, workbook.getNumberOfSheets());

        workbook.close();
    }

    @After
    public void tearDown() {
        if (workbook != null)
            workbook.close();

        set = null;
        antCsvToExcel = null;
    }
}
