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
        if (proxyHost != null && proxyPort != Integer.MIN_VALUE) {
            Credentials credsProxy;

            if (proxyUser != null)
                credsProxy = new UsernamePasswordCredentials(proxyUser, proxyPassword);
            else
                credsProxy = new UsernamePasswordCredentials("","");

            httpClient.getHostConfiguration().setProxy(proxyHost, proxyPort);
            httpClient.getState().setCredentials(new AuthScope(proxyHost, proxyPort), credsProxy);
        }
        return httpClient;
    }

    public static HttpClient setCredentials(HttpClient httpClient, String strUrl, String user, String password) throws MalformedURLException{
        URL url = new URL(strUrl);
        Credentials creds = new UsernamePasswordCredentials(user, password);
        httpClient.getState().setCredentials(new AuthScope(url.getHost(), url.getPort()), creds);
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
