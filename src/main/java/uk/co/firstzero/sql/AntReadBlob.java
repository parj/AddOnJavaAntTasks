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

package uk.co.firstzero.sql;

import org.apache.log4j.Logger;
import org.apache.tools.ant.Task;

import java.io.*;
import java.sql.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class AntReadBlob extends Task {
	private String className;
	private String jdbcUrl;
	private String user;
    private String password;
    private String extension;
    private String sql;
    private String outputDirectory;
    private boolean unzip;
    private static final int NAME = 1;
    private static final int BLOB = 2;
    private static final int KBYTES = 1024;


	private static Logger logger = Logger.getLogger(AntReadBlob.class);

    /**
     * Get the driver class to use - example oracle.jdbc.driver.OracleDriver
     * @return
     */
    public String getClassName() {
        return className;
    }

    /**
     * Sets the driver class to use - example oracle.jdbc.driver.OracleDriver
     * @param className
     */
    public void setClassName(String className) {
        this.className = className;
    }

    /**
     * The JDBC url to connect to - example - jdbc:oracle:thin:@localhost:1521:xe
     * @return
     */
    public String getJdbcUrl() {
        return jdbcUrl;
    }

    /**
     *
     * @param jdbcUrl
     */
    public void setJdbcUrl(String jdbcUrl) {
        this.jdbcUrl = jdbcUrl;
    }

    /**
     * The JDBC user
     * @return
     */
    public String getUser() {
        return user;
    }

    /**
     * The JDBC user
     * @param user
     */
    public void setUser(String user) {
        this.user = user;
    }

    /**
     * The password
     * @return
     */
    public String getPassword() {
        return password;
    }

    /**
     * The password
     *
     * @param password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * The file extension to use - example .zip
     * @return
     */
    public String getExtension() {
        return extension;
    }

    /**
     * The file extension to use - example .zip
     * @return
     */
    public void setExtension(String extension) {
        this.extension = extension;
    }

    /**
     * The sql to run - it should contain a string name and then blob - example
     * SELECT name, blob_data from test_db where condition_1=1234
     *
     * @return
     */
    public String getSql() {
        return sql;
    }

    /**
     * The sql to run - it should contain a string name and then blob - example
     * SELECT name, blob_data from test_db where condition_1=1234
     *
     * @param sql
     */
    public void setSql(String sql) {
        this.sql = sql;
    }


    /**
     * The output directory to output the zip files
     * @return The output directory to output the zip files
     */
    public String getOutputDirectory() {
        return outputDirectory;
    }

    /**
     * The output directory to output the zip files
     * @param outputDirectory
     */
    public void setOutputDirectory(String outputDirectory) {
        this.outputDirectory = outputDirectory;
    }

    /**
     * Unzip the input stream
     * @return True or False to unzip the input stream
     */
    public boolean isUnzip() {
        return unzip;
    }

    /**
     * Unzip the input stream
     * @param unzip
     */
    public void setUnzip(boolean unzip) {
        this.unzip = unzip;
    }

	public void execute() {
        Connection conn = null;
        try {
            logger.debug("Trying to load " + getClassName());
            Class.forName(getClassName());

            logger.trace("Trying to connect using + " + getJdbcUrl());
            conn = DriverManager.getConnection(getJdbcUrl(), getUser(), getPassword());

            logger.trace("Trying to run + " + getSql());
            PreparedStatement stmt = conn.prepareStatement(getSql());
            ResultSet resultSet = stmt.executeQuery();

            logger.debug("Extracting resultset. Number of records - " + resultSet.getFetchSize());
            while (resultSet.next()) {
                logger.trace("Name - " + resultSet.getString(NAME));
                String name = resultSet.getString(NAME);

                String fileName = getOutputDirectory() + File.separator + name + getExtension();
                logger.debug("Filename outputing to - " + fileName);

                File file = new File(fileName);
                FileOutputStream fos = new FileOutputStream(file);

                byte[] buffer = new byte[KBYTES];

                logger.debug("Unzip is " + isUnzip());
                if (isUnzip()) {
                    logger.trace("Trying to extract blob");
                    ZipInputStream zis = new ZipInputStream(resultSet.getBinaryStream(BLOB));
                    ZipEntry ze = zis.getNextEntry();

                    //Extract the first file
                    if( ze!=null ) {
                        logger.trace("Extracting zip");
                        int len;

                        logger.trace("Writing out file");
                        while ((len = zis.read(buffer)) > 0) {
                            fos.write(buffer, 0, len);
                        }
                    }
                } else {
                    InputStream is = resultSet.getBinaryStream(BLOB);
                    while (is.read(buffer) > 0) {
                        fos.write(buffer);
                    }
                }

                fos.close();
            }

            resultSet.close();
            stmt.close();

        } catch (SQLException e) {
            logger.error(e.getMessage());
        } catch (ClassNotFoundException e) {
            logger.error(e.getMessage());
        } catch (FileNotFoundException e) {
            logger.error(e.getMessage());
        } catch (IOException e) {
            logger.error(e.getMessage());
        } finally {
            try {
                if (conn != null && !conn.isClosed()) {
                    conn.close();
                }
            } catch(SQLException e){

            }
        }
    }
}

