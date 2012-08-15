package org.pm.csv;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import jxl.write.WriteException;
import org.junit.After;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import static junit.framework.Assert.assertEquals;

/**
 * Created by IntelliJ IDEA.
 * User: parj
 * Date: 07/08/2012
 * Time: 15:58
 * To change this template use File | Settings | File Templates.
 */
public class CsvToExcelTest {
    private String iFile = "src/test/resources/csv/CsvToExcel/output.csv";
    private String iFileSeparator = "src/test/resources/csv/CsvToExcel/outputSeparator.csv";
    private String oFile = "src/test/resources/csv/CsvToExcel/Excel.xls";
    
    private CsvToExcel csvExcel;

    @After
    public void tearDown() throws Exception {
        csvExcel = null;
    }

    @Test
    public void testConversion() throws IOException, WriteException, BiffException {
        csvExcel = new CsvToExcel(iFile, oFile);
        csvExcel.execute();
        assertEquals(countCells(oFile), 20);
    }

    @Test()
    public void testSeparator() throws IOException, WriteException, BiffException {
        csvExcel = new CsvToExcel(iFileSeparator, oFile, '^');
        csvExcel.execute();
        assertEquals(countCells(oFile), 24);
    }

    public int countCells(String excelFile) throws IOException, BiffException {
        File inputWorkbook = new File(excelFile);
        Workbook w = Workbook.getWorkbook(inputWorkbook);
        Sheet sheet = w.getSheet(0);
        int count = 0;

        for (int j = 0; j < sheet.getColumns(); j++) {
            for (int i = 0; i < sheet.getRows(); i++) {
                count++;
            }
        }
        w.close();

        return count;
    }
}
