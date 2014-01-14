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

package uk.co.firstzero.webdav;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.HeadMethod;
import org.apache.commons.httpclient.methods.InputStreamRequestEntity;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.jackrabbit.webdav.client.methods.DeleteMethod;
import org.apache.jackrabbit.webdav.client.methods.MkColMethod;
import org.apache.jackrabbit.webdav.client.methods.PutMethod;
import org.apache.log4j.Logger;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.types.FileSet;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.util.Vector;

public class Push extends Task{
    private static Logger logger = Logger.getLogger(Push.class);
	private String user;
	private String password;
	private String url;
	Vector<FileSet> fileSets = new Vector<>();
    private boolean overwrite;
    private String proxyUser;
    private String proxyPassword;
    private String proxyHost;
    private int proxyPort = Integer.MIN_VALUE;
    private HttpClient httpClient;

    /**
     * Empty constructor
     */
    public Push() {

    }

    /**
     * Intiliasing constructor
     * @param user WebDAV user
     * @param password WebDAV password
     * @param url WebDAV URL
     * @throws MalformedURLException
     */
    public Push(String user, String password, String url) throws MalformedURLException {
        setUser(user);
        setPassword(password);
        setUrl(url);
        setUp();
    }

    /**
     * Sets the Proxy Host
     * @param proxyHost Proxy Host
     */
    public void setProxyHost(String proxyHost) {
        this.proxyHost = proxyHost;
        logger.trace("proxyHost is " + proxyHost);
    }

    /**
     * Sets the Proxy Port
     * @param proxyPort Proxy Port
     */
    public void setProxyPort(int proxyPort) {
        this.proxyPort = proxyPort;
        logger.trace("proxyHost is " + proxyPort);
    }

    /**
     * Sets the Proxy User
     * @param proxyUser Proxy user
     */
    public void setProxyUser(String proxyUser) {
        this.proxyUser = proxyUser;
        logger.trace("proxyHost is " + proxyUser);
    }

    /**
     * Sets the proxy password
     * @param proxyPassword Proxy password
     */
    public void setProxyPassword(String proxyPassword) {
        this.proxyPassword = proxyPassword;
        logger.trace("proxyHost is " + proxyPassword);
    }

	/**
	 * Set webdav user name
	 * @param user WebDAV user
	 */
	public void setUser(String user) {
		this.user = user;
	}
	
	/**
	 * Set webdav password
	 * @param password WebDAV password
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	
	/**
	 * Set the path of the webdav to which the files are to be uploaded to <br>
	 * Example: http://localhost:8080/repository/default
	 * @param url WebDAV URL
	 */
	public void setUrl(String url) {
		this.url = url;
	}

    public void setOverwrite(boolean overwrite) {
        this.overwrite = overwrite;
    }
	
	/**
	 * For providing a set of input files using Ant's fileset
	 * @param fileSet Files to be uploaded
	 */
	public void addFileSet(FileSet fileSet) {
		if (!fileSets.contains(fileSet)) {
			fileSets.add(fileSet);
		}
    }

    /**
     * Setups credentials and proxies
     * @throws MalformedURLException
     */
    public void setUp() throws MalformedURLException {
        //Setup
        httpClient = new HttpClient();
        httpClient = Common.setProxy(httpClient, proxyHost, proxyPort, proxyUser, proxyPassword);
        httpClient = Common.setCredentials(httpClient, url, user, password);
    }

	/**
	 * The execute function is called by Ant. 
	 */
	public void execute() {
		try {
			setUp();
            DirectoryScanner ds;
            
            for (FileSet fileset : fileSets) {
            	ds = fileset.getDirectoryScanner(getProject());
            	File dir = ds.getBasedir();
            	String[] filesInSet = ds.getIncludedFiles();
            	
            	 for (String filename : filesInSet) {
            		 logger.info("Uploading " + filename);
            		 
            		 File f = new File(dir, filename);
            		 createDirectory(filename, f.getName());
            		 boolean result = uploadFile(f, filename);
                     logger.info("Upload status of " + filename + " - " + result);
            	 }
            }
    		
    	} catch(HttpException e) {
            logger.error(e);

        } catch(FileNotFoundException e) {
            logger.error(e);

        } catch(IOException e) {
            logger.error(e);

	    }
	}
	
	/**
	 * Creates a directory on the webdav server
	 * @param path The directory path to be created
	 * @param fileName The filename to be uploaded
	 */
	private void createDirectory(String path, String fileName) {
		try {
			//Remove the filename at the end
			String directoryPath = path.split(fileName)[0].trim();
			//Build the upload URL
			String uploadUrl = url;
			String[] directories = directoryPath.split(File.separator);
			
			//If a directory needs to be created
			if (directoryPath.length() > 0) {
				//Recursively create the directory structure
				for (String directoryName:directories) {
					uploadUrl = uploadUrl + "/" + directoryName;
					
					MkColMethod mkdir = new MkColMethod(uploadUrl);
					int status = httpClient.executeMethod(mkdir);
					
					if (status == HttpStatus.SC_METHOD_NOT_ALLOWED)	{
                        // Directory exists. Do Nothing
                        logger.trace("Directory exists");
                    } else if (status != HttpStatus.SC_CREATED) {
		            	logger.error("ERR " + " " + status + " " + uploadUrl);
                    } else {
		            	logger.debug("Directory " + uploadUrl + " created");
                    }
				}
			}
		} catch(java.lang.ArrayIndexOutOfBoundsException e) {
			//Ignore as there is no directory to be created
		} catch (Exception e) {
			logger.error("ERR creating " + path);
            logger.error(e);

		 }
	}

    public void uploadFile(File f) throws IOException {
        uploadFile(f, f.getName());
    }
	
	/**
	 * Uploads the file. The File object (f) is used for creating a FileInputStream for uploading
	 * files to webdav
	 * @param f	The File object of the file to be uploaded
	 * @param filename	The relatvie path of the file
	 */
	public boolean uploadFile(File f, String filename) throws IOException {
        String uploadUrl = url + "/" + filename;
        boolean uploaded = false;

        deleteFile(uploadUrl);
        PutMethod putMethod = new PutMethod(uploadUrl);

        logger.debug("Check if file exists - " + fileExists(uploadUrl));

        if (!fileExists(uploadUrl))  {
            RequestEntity requestEntity = new InputStreamRequestEntity(new FileInputStream(f));

            putMethod.setRequestEntity(requestEntity);
            httpClient.executeMethod(putMethod);

            logger.trace("uploadFile - statusCode - " + putMethod.getStatusCode());

            switch (putMethod.getStatusCode()) {
                case HttpURLConnection.HTTP_NO_CONTENT:
                    logger.info("IGNORE - File already exists " + f.getAbsolutePath());
                    break;
                case HttpURLConnection.HTTP_OK:
                    uploaded = true;
                    logger.info("Transferred " + f.getAbsolutePath());
                    break;
                case HttpURLConnection.HTTP_CREATED: //201 Created => No issues
                    uploaded = true;
                    logger.info("Transferred " + f.getAbsolutePath());
                    break;
                default:
                    logger.error("ERR " + " " + putMethod.getStatusCode() + " " + putMethod.getStatusText() + " " + f.getAbsolutePath());
                    break;
            }
         }
         else {
             logger.info("IGNORE - File already exists " + f.getAbsolutePath());
         }
        return uploaded;
	}

	public boolean fileExists(String url) throws IOException {
        return Common.executeMethod(httpClient, new HeadMethod(url), true);
	}

    public boolean deleteFile(String url) {
        boolean deleted = true;

        try {
            logger.debug("Check if fileExists - " + fileExists(url));
            if (fileExists(url) && overwrite) {
                deleted = Common.executeMethod(httpClient, new DeleteMethod(url));
            }
        } catch (HttpException e) {
            logger.error("Could not delete " + url);
            logger.error(e);
            deleted = false;
        } catch (IOException e) {
            logger.error("Could not delete " + url);
            logger.error(e);
            deleted = false;
        }

        return deleted;
    }
}
