package org.pm.webdav;

import junit.framework.TestCase;
import org.apache.log4j.Logger;

import java.io.IOException;


public class PullTest extends TestCase {
    private Logger logger = Logger.getLogger(PullTest.class);

    private Pull pull;

    protected void setUp() {
        pull = new Pull("admin", "admin", "http://localhost:8080/repository/default", "pull.csv", "src/test/resources/webdav/pull.csv");
    }

	public void testDownload() throws IOException {
        pull.download();
    }

    protected void tearDown {
        pull = null;
    }
}
