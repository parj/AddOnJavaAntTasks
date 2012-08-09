package org.pm.webdav;

import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
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
import java.util.Vector;

public class Push extends Task{
    private static Logger logger = Logger.getLogger(Push.class);
	private String user;
	private String password;
	private String url;
	Vector<FileSet> fileSets = new Vector<FileSet>();
	private HttpClient client;
    private boolean overwrite;

    /**
     * Empty Constructor
     */
    public Push() {
        //Empty constructor
    }

    public Push(String user, String password, String url) {
        setUser(user);
        setPassword(password);
        setUrl(url);
        setUp();
    }

    public void setUp() {
        client = new HttpClient();
        Credentials creds = new UsernamePasswordCredentials(user, password);
        client.getState().setCredentials(AuthScope.ANY, creds);
    }

	/**
	 * Set webdav user name
	 * @param user
	 */
	public void setUser(String user) {
		this.user = user;
	}
	
	/**
	 * Set webdav password
	 * @param password
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	
	/**
	 * Set the path of the webdav to which the files are to be uploaded to <br>
	 * Example: http://localhost:8080/repository/default
	 * @param url
	 */
	public void setUrl(String url) {
		this.url = url;
	}

    public void setOverwrite(boolean overwrite) {
        this.overwrite = overwrite;
    }
	
	/**
	 * For providing a set of input files using Ant's fileset
	 * @param fileSet
	 */
	public void addFileSet(FileSet fileSet) {
		if (!fileSets.contains(fileSet)) {
			fileSets.add(fileSet);
		}
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
            		 logger.debug("Processing " + filename);
            		 
            		 File f = new File(dir, filename);
            		 createDirectory(filename, f.getName());
            		 uploadFile(f, filename);
            	 }
            }
    		
    	} catch(HttpException e) {
            logger.error(e.getMessage());
        } catch(FileNotFoundException e) {
            logger.error(e.getMessage());
        } catch(IOException e) {
            logger.error(e.getMessage());
	    }
	}
	
	/**
	 * Creates a directory on the webdav server
	 * @param path
	 * @param fileName
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
					int status = client.executeMethod(mkdir);
					
					if (status == 405)	
						{/*Directory exists. Do Nothing*/}
					else if (status != 201)
		            	logger.error("ERR " + " " + status + " " + uploadUrl);
		            else
		            	logger.debug("Directory " + uploadUrl + " created");
				}
			}
		} catch(java.lang.ArrayIndexOutOfBoundsException e) {
			//Ignore as there is no directory to be created
		} catch (Exception e) {
			logger.error("ERR creating " + path);
            logger.error(e.getMessage());
			e.printStackTrace();
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
	public void uploadFile(File f, String filename) throws IOException {
        String uploadUrl = url + "/" + filename;

        deleteFile(uploadUrl);
        PutMethod putMethod = new PutMethod(uploadUrl);

        if (!fileExists(uploadUrl))  {
            RequestEntity requestEntity = new InputStreamRequestEntity(new FileInputStream(f));

            putMethod.setRequestEntity(requestEntity);
            client.executeMethod(putMethod);

            logger.trace("uploadFile - statusCode - " + putMethod.getStatusCode());

            switch (putMethod.getStatusCode()) {
                case HttpURLConnection.HTTP_NO_CONTENT:
                    logger.info("IGNORE - File already exists " + f.getAbsolutePath());
                    break;
                case HttpURLConnection.HTTP_OK:
                    logger.info("Transferred " + f.getAbsolutePath());
                    break;
                case HttpURLConnection.HTTP_CREATED: //201 Created => No issues
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
	}

	public boolean fileExists(String url) throws HttpException, IOException {
        HeadMethod headMethod = new HeadMethod(url);
        client.executeMethod(headMethod);
        int statusCode = headMethod.getStatusCode();
        logger.trace("fileExists - statusCode - " + statusCode);

        boolean exists = headMethod.getStatusCode() == HttpURLConnection.HTTP_OK;
        logger.debug("fileExists - " + exists);

        return exists;
	}

    public boolean deleteFile(String url) {
        DeleteMethod deleteMethod = new DeleteMethod(url);
        boolean deleted = true;

        try {
            if (fileExists(url) && overwrite) {
                client.executeMethod(deleteMethod);
                int statusCode = deleteMethod.getStatusCode();
                logger.trace("deleteFile - statusCode - " + statusCode);

                deleted = deleteMethod.getStatusCode() == HttpURLConnection.HTTP_OK;
                logger.debug("deleteFile - " + deleted);
            }
        } catch (HttpException e) {
            logger.error("Could not delete " + url);
            logger.error(e.getMessage());
            deleted = false;
        } catch (IOException e) {
            logger.error("Could not delete " + url);
            logger.error(e.getMessage());
            deleted = false;
        }

        return deleted;
    }
}
