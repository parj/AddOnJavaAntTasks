package org.pm.webdav;

import au.com.bytecode.opencsv.CSVReader;
import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;

import static junit.framework.Assert.assertEquals;


/** Force this to run after WebDav_1_PushTest - The file needs to be pushed before being downloaded **/
public class WebDav_2_PullTest {
    private Logger logger = Logger.getLogger(WebDav_2_PullTest.class);
    private String oFile = "src/test/resources/webdav/output.csv";
    private String url = "http://localhost:8080/repository/default";
    private String fileName = "output.csv";
    private Pull pull;

    @Before
    public void setUp() throws MalformedURLException {
        pull = new Pull("admin", "admin", url, fileName, oFile);
        File oFile_delete = new File(oFile);
        oFile_delete.delete();
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
                    assertEquals("Header_1", line[0]);
                    break;
                case 2:
                    logger.trace("Read - " + line[2]);
                    assertEquals("˚∆∫å∂¬ß^∆", new String(line[2].getBytes(), "UTF8"));
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
