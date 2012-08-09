package org.pm.webdav;

import au.com.bytecode.opencsv.CSVReader;
import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.FileReader;
import java.io.IOException;

import static junit.framework.Assert.assertEquals;


public class PullTest {
    private Logger logger = Logger.getLogger(PullTest.class);
    private String oFile = "src/test/resources/webdav/output.csv";
    private String url = "http://localhost:8080/repository/default";
    private String fileName = "output.csv";
    private Pull pull;

    @Before
    public void setUp() {
        pull = new Pull("admin", "admin", url, fileName, oFile);
    }

    @Test
	public void testDownload() throws IOException {
        pull.download();
        CSVReader fileE = new CSVReader(new FileReader(oFile), ',');
        String[] line;

        int count = 0;
        while((line = fileE.readNext()) != null) {
            switch (count) {
                case 0:
                    logger.trace("Read - " + line[0]);
                    assertEquals(line[0], "Header_1");
                    break;
                case 2:
                    logger.trace("Read - " + line[2]);
                    assertEquals(new String(line[2].getBytes(), "UTF8"), "˚∆∫å∂¬ß^∆");
                    break;
            }

            ++count;
        }

        assertEquals(count, 5);
    }

    @After
    public void tearDown() {
        pull = null;
    }
}
