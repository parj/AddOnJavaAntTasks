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

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import jxl.write.WriteException;
import org.junit.After;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import static junit.framework.Assert.assertEquals;

public class CsvToExcelTest {
    private String baseDir = "src/test/resources/csv/CsvToExcel";
    private String[] iFile = {"output.csv"};
    private String[] iFileSeparator = {"outputSeparator.csv"};
    private String oFile = baseDir + File.separator + "Excel.xls";
    
    private CsvToExcel csvExcel;

    @After
    public void tearDown() throws Exception {
        csvExcel = null;
    }

    @Test
    public void testConversion() throws IOException, WriteException, BiffException {

        csvExcel = new CsvToExcel(iFile, oFile, baseDir);
        csvExcel.execute();
        assertEquals(countCells(oFile), 20);
    }

    @Test()
    public void testSeparator() throws IOException, WriteException, BiffException {
        csvExcel = new CsvToExcel(iFileSeparator, oFile, baseDir, '^');
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
