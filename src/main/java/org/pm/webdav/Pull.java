package org.pm.webdav;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.log4j.Logger;
import org.apache.tools.ant.Task;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;

public class Pull extends Task {
	private static Logger logger = Logger.getLogger(Pull.class);
    private String user;
	private String password;
	private String url;
	private String file;
	private String outFile;
    private String proxyUser;
    private String proxyPassword;
    private String proxyHost;
    private int proxyPort = Integer.MIN_VALUE;
	private boolean overwrite = false;
    private HttpClient httpClient;

    public Pull() {

    }

    public Pull(String user, String password, String url, String fileToDownload) {
        setUser(user);
        setPassword(password);
        setUrl(url);
        setFile(fileToDownload);
    }

    public Pull(String user, String password, String url, String fileToDownload, String outputFile) throws MalformedURLException{
        setUser(user);
        setPassword(password);
        setUrl(url);
        setFile(fileToDownload);
        setOutFile(outputFile);
        setUp();
    }
	
	public void setUser(String user) {
		this.user = user;
        logger.trace("User is " + this.user);
	}
	
	public void setPassword(String password) {
		this.password = password;
        logger.trace("password is " + this.password);
	}
	
	public void setUrl(String url) {
		this.url = url;
        logger.trace("url is " + this.url);
	}
	
	public void setFile(String file) {
		this.file = file;

        logger.trace("file is " + this.file);
		
		//Issue 3 - outFile property now an optional element
		if (this.outFile == null)
			this.outFile = file;
	}

	public void setOutFile(String outFile) {
		this.outFile = outFile;

        logger.trace("outFile is " + this.outFile);
	}

	public void setOverwrite(boolean overwrite) {
		this.overwrite = overwrite;
        logger.trace("overwrite is " + this.overwrite);
	}

    public void setProxyHost(String proxyHost) {
        this.proxyHost = proxyHost;
        logger.trace("proxyHost is " + this.proxyHost);
    }

    public void setProxyPort(int proxyPort) {
        this.proxyPort = proxyPort;
        logger.trace("proxyHost is " + this.proxyPort);
    }

    public void setProxyUser(String proxyUser) {
        this.proxyUser = proxyUser;
        logger.trace("proxyHost is " + this.proxyUser);
    }

    public void setProxyPassword(String proxyPassword) {
        this.proxyPassword = proxyPassword;
        logger.trace("proxyHost is " + this.proxyPassword);
    }

    public void setUp() throws MalformedURLException {
        logger.trace("Setting up setUp");

        //Setup
        httpClient = new HttpClient();
        httpClient = Common.setProxy(httpClient, proxyHost, proxyPort, proxyUser, proxyPassword);
        httpClient = Common.setCredentials(httpClient, this.url, this.user, this.password);

        logger.trace("Completed setup");
    }

    public boolean download() throws IOException {
        setUp();

        File oFile = new File(outFile);

        //Start timing
        long startTime = System.currentTimeMillis();
        logger.trace("Started time - startTime - " + startTime);
        boolean completed = false;

        if (this.overwrite || !oFile.exists()) {
            if (oFile.exists())
                logger.debug("Overwriting " + oFile.getAbsolutePath());

            logger.debug("Downloading " + url + "/" + file + " to " + oFile.getAbsolutePath());

            //Download the file
            GetMethod method = new GetMethod(url + "/" + file);
            httpClient.executeMethod(method);

            //200 OK => No issues
            if (method.getStatusCode() != HttpURLConnection.HTTP_OK)
                throw new IOException(method.getStatusCode() + " " + method.getStatusText());

            writeFile(method.getResponseBodyAsStream(), oFile);

            long elapsed = ((System.currentTimeMillis() - startTime) / 1000);
            logger.debug(file + " took " + elapsed + " seconds to complete");

            completed = true;
        } else {
            logger.debug("Skipping - overwrite not allowed for " + file);
        }

        return completed;
    }

	public void execute() {
        boolean status = false;

        try {
            status = download();
        } catch(IOException e) {
            logger.error(e);
        }

        String message = status ? "Download successful" : "Download failed";
        logger.info(message);

        if (status) System.exit(0);
        else        System.exit(1);
	}

    public void writeFile(InputStream inputStream, File outFile) throws IOException {
        OutputStream out = new FileOutputStream(outFile);
        byte buf[] = new byte[1024];
        int len;

        while((len = inputStream.read(buf))>0) {
            out.write(buf,0,len);
            out.flush();
        }
        out.close();
    }

}
