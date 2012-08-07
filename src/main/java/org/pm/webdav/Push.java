package org.pm.webdav;

import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.HeadMethod;
import org.apache.commons.httpclient.methods.InputStreamRequestEntity;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.jackrabbit.webdav.client.methods.MkColMethod;
import org.apache.jackrabbit.webdav.client.methods.PutMethod;
import org.apache.log4j.Logger;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.types.FileSet;

import java.io.File;
import java.io.FileInputStream;
import java.net.HttpURLConnection;
import java.util.Vector;

public class Push extends Task{
    private static Logger logger = Logger.getLogger(Push.class);
	private String user;
	private String password;
	private String url;
	Vector<FileSet> fileSets = new Vector<FileSet>();
	private HttpClient client;

    /**
     * Empty Constructor
     */
    public Push() {
        //Empty constructor
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
			DirectoryScanner ds;
    		client = new HttpClient();
    		Credentials creds = new UsernamePasswordCredentials(user, password);
    		client.getState().setCredentials(AuthScope.ANY, creds);
            
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
    		
    	} catch (Exception e) {
	    	e.printStackTrace();
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
	
	/**
	 * Uploads the file. The File object (f) is used for creating a FileInputStream for uploading
	 * files to webdav
	 * @param f	The File object of the file to be uploaded
	 * @param filename	The relatvie path of the file
	 */
	private void uploadFile(File f, String filename) {
		 try {
			 String uploadUrl = url + "/" + filename;
			 
			 //Issue 5 - Check file exists before uploading
			 //This will be faster than uploading the file and
			 //then checking the status
			 if (!fileExists(uploadUrl))  {
				 PutMethod method = new PutMethod(uploadUrl);
				 RequestEntity requestEntity = new InputStreamRequestEntity(new FileInputStream(f));
				 method.setRequestEntity(requestEntity);
				 client.executeMethod(method);
	    		
	    		//201 Created => No issues
				if (method.getStatusCode() == 204)
					logger.info("IGNORE - File already exists " + f.getAbsolutePath());
				else if (method.getStatusCode() != 201)
	    			logger.error("ERR " + " " + method.getStatusCode() + " " + method.getStatusText() + " " + f.getAbsolutePath());
	    		else {
	    			logger.info("Transferred " + f.getAbsolutePath());
	    			
	    			if (fileExists(uploadUrl))
	    				logger.info("Checked - File uploaded to server");
	    			else
	    			    logger.error("FAILED to upload - " + f.getAbsolutePath());
	    		}
			 }
			 else {
				 logger.info("IGNORE - File already exists " + f.getAbsolutePath());
			 }
		 } catch (Exception e) {
			 logger.error("ERR " + f.getAbsolutePath());
             logger.error(e.getMessage());
			 e.printStackTrace();
		 }
	}
	
	private boolean fileExists(String url) {
		try {
			HeadMethod method = new HeadMethod(url);
			client.executeMethod(method);
		    return (method.getStatusCode() == HttpURLConnection.HTTP_OK);
	    }
	    catch (Exception e) {
	    	e.printStackTrace();
            logger.error(e.getMessage());
		    return false;
	    }
	}
}
