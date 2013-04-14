package uk.co.firstzero.sql;

import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static junit.framework.Assert.assertNotNull;

public class H2Test {
    @Before
    public void setUp() throws ClassNotFoundException {
        Class.forName("org.h2.Driver");
    }

    @Test
    public void testH2() throws SQLException {
        Connection conn = DriverManager.
                getConnection("jdbc:h2:~/test", "sa", "");
        assertNotNull(conn);

        // add application code here
        conn.close();
    }
}
