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
import java.util.Arrays;

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
        logger.debug(method.getStatusCode() + " " + method.getStatusText() + " " + method.getResponseBodyAsString() + " " + Arrays.toString(method.getResponseHeaders()));

        return result;
    }

    /**
     * Used for checking if a file exists
     * @param client    The client to execute the method
     * @param method    The method to be executed
     * @param ignoreHTTP_NOT_FOUND  Used to flag if the HTTP_NOT_FOUND error has to be ignored, if not the IOException raised will be thrown
     * @return  Returns if the execution has been successful
     * @throws IOException
     */
    public static boolean executeMethod(HttpClient client, HttpMethod method, boolean ignoreHTTP_NOT_FOUND) throws IOException {
        try {
            client.executeMethod(method);
        } catch(IOException e) {
            //If it is not 404 - throw exception, otherwise
            if (method.getStatusCode() != HttpURLConnection.HTTP_NOT_FOUND)
                throw e;
        }
        int statusCode = method.getStatusCode();
        logger.trace("executeMethod - statusCode - " + statusCode);

        boolean result = (method.getStatusCode() == HttpURLConnection.HTTP_OK);
        logger.debug("executeMethod - result - " + result);
        logger.debug(method.getStatusCode() + " " + method.getStatusText() + " " + method.getResponseBodyAsString() + " " + Arrays.toString(method.getResponseHeaders()));

        return result;
    }
}
