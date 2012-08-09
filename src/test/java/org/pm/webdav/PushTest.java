package org.pm.webdav;

import junit.framework.TestCase;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;


public class PushTest extends TestCase {
    private Logger logger = Logger.getLogger(PushTest.class);
    private String _iFile = "src/test/resources/webdav/output.csv";
    private String url = "http://localhost:8080/repository/default";
    private File iFile;
    private String fullUrl;
    private Push push;

    protected void setUp() throws IOException {
        push = new Push("admin", "admin", "http://localhost:8080/repository/default");
        push.setOverwrite(true);

        iFile = new File(_iFile);
        fullUrl = url + "/" + iFile.getName();

        logger.debug("fullUrl - " + fullUrl);
    }

	public void testDownload() throws IOException {
        push.uploadFile(iFile, "output.csv");
        boolean status = push.fileExists(fullUrl);

        logger.debug("fileExists - status - " + status);
        assertTrue(status);
    }

    protected void tearDown() {
        push = null;
        iFile = null;
    }
}
