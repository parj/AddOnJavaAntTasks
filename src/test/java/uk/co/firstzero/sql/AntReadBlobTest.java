package uk.co.firstzero.sql;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.*;

import static org.junit.Assert.assertEquals;

public class AntReadBlobTest {
    Connection conn;
    private static final Logger logger = Logger.getLogger(AntReadBlob.class);

    private final String JDBC_URL = "jdbc:h2:" + System.getProperty("user.dir") + "/src/test/resources/sql/temp";
    private final String USER = "sa";
    private final String PASSWORD = "";
    private final String CLASS_NAME = "org.h2.Driver";
    private final String SQL_EXTRACTION = "SELECT name, blob from TEST";
    private final String SQL_CREATE_TABLE = "CREATE TABLE IF NOT EXISTS TEST(name VARCHAR(255), blob BLOB)";
    private final String SQL_INSERT_TABLE = "INSERT INTO TEST(name, blob) VALUES(?, ?)";
    private final String SQL_DROP_TABLE = "DROP ALL OBJECTS";
    private final String FILE_ZIP = "src/test/resources/sql/blob.zip";
    private final String DIRECTORY_OUTPUT = "src/test/resources/sql";
    private final int COLUMN_NAME = 1;
    private final int COLUMN_BLOB = 2;


    @Before
    public void setUp() throws ClassNotFoundException, SQLException, FileNotFoundException {
        logger.debug("Trying to load driver - " + CLASS_NAME);
        Class.forName(CLASS_NAME);

        logger.debug("Trying to connect - " + JDBC_URL);
        conn = DriverManager.getConnection(JDBC_URL, USER, PASSWORD);

        logger.debug("CREATING TABLE");
        createTable();

        logger.debug("INSERTING BLOB");
        insertBlob();
    }

    @Test
    public void testName() throws SQLException, FileNotFoundException {
        PreparedStatement stmt = conn.prepareStatement(SQL_EXTRACTION);
        ResultSet resultSet = stmt.executeQuery();
        resultSet.next();

        assertEquals(resultSet.getString(1), "blob.zip");

        resultSet.close();
        stmt.close();
    }

    @Test
    public void testExtract() throws SQLException, FileNotFoundException {
        logger.debug("Creating AntReadBlob");
        AntReadBlob antReadBlob = new AntReadBlob();

        logger.trace("Setting JDBC_URL: " + JDBC_URL);
        antReadBlob.setJdbcUrl(JDBC_URL);

        logger.trace("Setting CLASS_NAME: " + CLASS_NAME);
        antReadBlob.setClassName(CLASS_NAME);

        logger.trace("Setting USER: " + USER);
        antReadBlob.setUser(USER);

        logger.trace("Setting PASSWORD: " + PASSWORD);
        antReadBlob.setPassword(PASSWORD);

        logger.trace("Setting SQL: " + SQL_EXTRACTION);
        antReadBlob.setSql(SQL_EXTRACTION);

        logger.trace("Setting Output Directory: " + DIRECTORY_OUTPUT);
        antReadBlob.setOutputDirectory(DIRECTORY_OUTPUT);

        logger.trace("Setting Extension: .jpg");
        antReadBlob.setExtension(".jpg");

        logger.trace("Setting Unzip: true");
        antReadBlob.setUnzip(true);

        antReadBlob.execute();

        assertEquals(new File(DIRECTORY_OUTPUT + File.separator + "blob.zip.jpg").exists(), true);
    }

    public void createTable() throws SQLException {
        Statement statement = conn.createStatement();
        statement.executeUpdate(SQL_CREATE_TABLE);
        statement.close();
    }

    public void dropTable() throws SQLException {
        Statement statement = conn.createStatement();
        statement.executeUpdate(SQL_DROP_TABLE);
        statement.close();
    }

    public void insertBlob() throws SQLException, FileNotFoundException {
        File f = new File(FILE_ZIP);
        InputStream fis = new FileInputStream(f);

        PreparedStatement ps = conn.prepareStatement(SQL_INSERT_TABLE);
        ps.setString(COLUMN_NAME, f.getName());
        ps.setBinaryStream(COLUMN_BLOB, fis, (int)f.length());

        ps.execute();
        conn.commit();

        ps.close();
    }

    @After
    public void tearDown() throws SQLException {
        logger.debug("Dropping all objects");
        dropTable();

        logger.debug("Closing connection");
        conn.close();
    }
}
