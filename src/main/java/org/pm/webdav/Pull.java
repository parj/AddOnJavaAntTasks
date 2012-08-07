package org.pm.webdav;

import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.log4j.Logger;
import org.apache.tools.ant.Task;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class Pull extends Task {
	private static Logger logger = Logger.getLogger(Pull.class);
    private String user;
	private String password;
	private String url;
	private String file;
	private String outFile;
	private boolean verbose = false;
	private boolean overwrite = false;

    public Pull() {

    }
	
	public void setUser(String user) {
		this.user = user;
        logger.trace("User is " + user);
	}
	
	public void setPassword(String password) {
		this.password = password;
        logger.trace("password is " + password);
	}
	
	public void setUrl(String url) {
		this.url = url;
        logger.trace("url is " + url);
	}
	
	public void setFile(String file) {
		this.file = file;

        logger.trace("file is " + file);
		
		//Issue 3 - outFile property now an optional element
		if (this.outFile == null)
			this.outFile = file;
	}

	public void setOutFile(String outFile) {
		this.outFile = outFile;

        logger.trace("outFile is " + outFile);
	}

	public void setOverwrite(boolean overwrite) {
		this.overwrite = overwrite;
        logger.trace("overwrite is " + overwrite);
	}

    public boolean download() {
        boolean completed = false;
        
        try {
    		//Setup
            HttpClient client = new HttpClient();
    		Credentials creds = new UsernamePasswordCredentials(user, password);
    		client.getState().setCredentials(AuthScope.ANY, creds);
    		File f = new File(outFile);

            //Start timing
    		long startTime = System.currentTimeMillis();
            logger.trace("Started time - startTime - " + startTime);

    		if (this.overwrite || !f.exists()) {
                if (f.exists()) logger.debug("Overwriting " + f.getAbsolutePath());
                logger.debug("Downloading " + url + "/" + file + " to " + f.getAbsolutePath());

    			//Download the file
    			GetMethod method = new GetMethod(url + "/" + file);
        		client.executeMethod(method);

        		//200 OK => No issues
        		if (method.getStatusCode() != 200)
        			throw new Exception(method.getStatusCode() + " " + method.getStatusText());

	    		InputStream is = method.getResponseBodyAsStream();
	    		OutputStream out = new FileOutputStream(f);
	    		byte buf[] = new byte[1024];
	    		int len;

	    		while((len = is.read(buf))>0) {
	    		    out.write(buf,0,len);
	    			out.flush();
	    		}
	    		out.close();

                long elapsed = ((System.currentTimeMillis() - startTime) / 1000);
                logger.debug(file + " took " + elapsed + " seconds to complete");

                completed = true;
    		} else {
    			logger.debug("Skipping - overwrite not allowed for " + file);
            }
    	} catch (Exception e) {
	    	e.printStackTrace();
            logger.error(e.getMessage());
	    }

        return completed;
    }

	public void execute() {
		Pull pull = new Pull();
        boolean status = pull.download();

        String message = status ? "Download successful" : "Download failed";
        logger.info(message);

        if (status) System.exit(0);
        else        System.exit(1);
	}

}
