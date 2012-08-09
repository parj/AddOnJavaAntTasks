package org.pm.webdav;

import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by IntelliJ IDEA.
 * User: parj
 * Date: 09/08/2012
 * Time: 19:49
 * To change this template use File | Settings | File Templates.
 */
public class Common {
    private static Logger logger = Logger.getLogger(Common.class);

    public static HttpClient setProxy(HttpClient httpClient, String proxyHost, int proxyPort, String proxyUser, String proxyPassword) {
        logger.trace("proxyHost is " + proxyHost);
        logger.trace("proxyPort is " + proxyPort);
        logger.trace("proxyUser is " + proxyUser);
        
        if (proxyHost != null && proxyPort != Integer.MIN_VALUE) {
            logger.trace("Setting up proxy");

            Credentials credsProxy;

            if (proxyUser != null)
                credsProxy = new UsernamePasswordCredentials(proxyUser, proxyPassword);
            else {
                logger.trace("proxyUser is null");
                credsProxy = new UsernamePasswordCredentials("","");
            }

            logger.trace("Applying host and port proxy");
            httpClient.getHostConfiguration().setProxy(proxyHost, proxyPort);

            logger.trace("Applying authentication scope and credentials");
            httpClient.getState().setCredentials(new AuthScope(proxyHost, proxyPort), credsProxy);
        }

        logger.trace("Completed setup of proxy");
        return httpClient;
    }

    public static HttpClient setCredentials(HttpClient httpClient, String strUrl, String user, String password) throws MalformedURLException{
        logger.trace("strUrl is " + strUrl);
        logger.trace("user is " + user);

        URL url = new URL(strUrl);

        logger.trace("Setting up username password credentials");
        Credentials creds = new UsernamePasswordCredentials(user, password);
        httpClient.getState().setCredentials(new AuthScope(url.getHost(), url.getPort()), creds);

        logger.trace("Completed setup of username password credentials");
        return  httpClient;
    }

    public static boolean executeMethod(HttpClient client, HttpMethod method) throws IOException {
        client.executeMethod(method);
        int statusCode = method.getStatusCode();
        logger.trace("executeMethod - statusCode - " + statusCode);

        boolean result = method.getStatusCode() == HttpURLConnection.HTTP_OK;
        logger.debug("executeMethod - result - " + result);

        return result;
    }
}
