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

package org.pm.sql;

import org.apache.log4j.Logger;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.types.FileSet;
import org.pm.csv.CsvDiff;
import org.pm.diff.CsvReport;
import org.pm.diff.Report;

import java.io.*;
import java.sql.*;
import java.util.Vector;

public class AntReadBlob extends Task {
	private String className;
	private String jdbcUrl;
	private String user;
    private String password;
    private String extension;
    private String sql;

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

	public void execute() {
        Connection conn = null;
        try {
            Class.forName(getClassName());
            conn = DriverManager.getConnection(getJdbcUrl(), getUser(), getPassword());

            String sql = getSql();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet resultSet = stmt.executeQuery();

            while (resultSet.next()) {
                String name = resultSet.getString(1);

                File image = new File(name + File.separator + getExtension());
                FileOutputStream fos = new FileOutputStream(image);

                byte[] buffer = new byte[256];

                //
                // Get the binary stream of our BLOB data
                //
                InputStream is = resultSet.getBinaryStream(3);
                while (is.read(buffer) > 0) {
                    fos.write(buffer);
                }

                fos.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (conn != null && !conn.isClosed()) {
                    conn.close();
                }
            } catch(SQLException e){
                e.printStackTrace();
            }
        }
    }
}

