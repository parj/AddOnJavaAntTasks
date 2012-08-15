package org.pm.webdav;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static junit.framework.Assert.assertTrue;

public class WebDav_1_PushTest {
    private Logger logger = Logger.getLogger(WebDav_1_PushTest.class);
    private String _iFile = "src/test/resources/webdav/output.csv";
    private String url = "http://localhost:8080/repository/default";
    private File iFile;
    private String fullUrl;
    private Push push;

    @Before
    public void setUp() throws IOException {
        push = new Push("admin", "admin", "http://localhost:8080/repository/default");
        push.setOverwrite(true);

        iFile = new File(_iFile);
        fullUrl = url + "/" + iFile.getName();

        logger.debug("fullUrl - " + fullUrl);
    }

    @Test
	public void testDownload() throws IOException {
        push.uploadFile(iFile, "output.csv");
        boolean status = push.fileExists(fullUrl);

        logger.debug("fileExists - status - " + status);
        assertTrue(status);
    }

    @After
    public void tearDown() {
        push = null;
        iFile = null;
    }
}
