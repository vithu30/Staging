/*
 * Copyright (c) 2018, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 *
 */

package org.wso2.external_contributions.msf4jhttp;

import com.google.common.io.BaseEncoding;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;

/*
 * Handle get post request to backend
 */
public class HttpHandler {
    private static final Logger logger = Logger.getLogger(HttpHandler.class);
    private String backendPassword;
    private String backendUsername;
    private String backendUrl;


    public HttpHandler() {
        PropertyReader propertyReader = new PropertyReader();
        this.backendPassword = propertyReader.getBackendPassword();
        this.backendUsername = propertyReader.getBackendUsername();
        this.backendUrl = propertyReader.getBackendUrl();
    }

//    public String get(String url) {
//        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
//        HttpGet request = new HttpGet(this.backendUrl + url);
//        request.addHeader("Accept", "application/json");
//        String encodedCredentials = this.encode(this.backendUsername + ":" + this.backendPassword);
//        request.addHeader("Authorization", "Basic " + encodedCredentials);
//        String responseString = null;
//
//        try {
//
//            HttpResponse response = httpClient.execute(request);
//            if (logger.isDebugEnabled()) {
//                logger.debug("Request successful for " + url);
//            }
//            responseString = EntityUtils.toString(response.getEntity(), "UTF-8");
//
//        } catch (IllegalStateException e) {
//            logger.error("The response is empty ");
//        } catch (NullPointerException e) {
//            logger.error("Bad request to the URL");
//        } catch (IOException e) {
//            logger.error("mke");
//        }
//
//        return responseString;
//    }

    public String httpsGet(String url) throws IOException, KeyManagementException, NoSuchAlgorithmException, KeyStoreException, CertificateException {
//        System.setProperty("https.protocols", "TLSv1,TLSv1.1,TLSv1.2");
//        HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
//        String path = "/home/vithursa/Documents/ballerina-0.964.0/bre/security/clientTruststore.p12";
//        String password = "ballerina";
//        KeyStore keyStore = KeyStore.getInstance("jks");
//        InputStream inputStream = new FileInputStream(path);
//        keyStore.load(inputStream,password.toCharArray());

        SSLContext sslContext = null;
        try{
            sslContext = SSLContextBuilder.create().loadTrustMaterial(new TrustSelfSignedStrategy()).build();
        } catch (NoSuchAlgorithmException  e){
            logger.error(e);
        } catch (KeyStoreException e){
            logger.error(e);
        } catch (KeyManagementException e){
            logger.error(e);
        }
        HostnameVerifier allowAllHosts = new NoopHostnameVerifier();
        SSLConnectionSocketFactory sslConnectionSocketFactory = new SSLConnectionSocketFactory(sslContext,allowAllHosts);


//        httpClientBuilder.setSSLSocketFactory(new SSLConnectionSocketFactory(SSLContexts.custom().loadTrustMaterial(keyStore,null).build()));
//        httpClientBuilder.setSSLSocketFactory(new SSLConnectionSocketFactory().SocketFactory(SSLContexts.custom().loadTrustMaterial(keyStore,null).build()));
        CloseableHttpClient httpClient = HttpClients.custom()
                .setSSLSocketFactory(sslConnectionSocketFactory)
                .build();
        HttpGet request = new HttpGet(this.backendUrl + url);
        request.addHeader("Accept", "application/json");
        String responseString = null;

        try {

            HttpResponse response = httpClient.execute(request);
            if (logger.isDebugEnabled()) {
                logger.debug("Request successful for " + url);
            }
            responseString = EntityUtils.toString(response.getEntity(), "UTF-8");

        } catch (IllegalStateException e) {
            logger.error("The response is empty ");
        } catch (NullPointerException e) {
            logger.error("Bad request to the URL");
        } catch (IOException e) {
            logger.error("mke");
        }
        return responseString;


//        String urlString = "https://localhost:9097/issues";
//        URL url = new URL(urlString);
//        HttpsURLConnection httpsURLConnection = (HttpsURLConnection) url.openConnection();
//        httpsURLConnection.setRequestMethod("GET");
//        httpsURLConnection.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml");
//        httpsURLConnection.setRequestProperty("Accept-Language", "it-IT,it");

        // Create a SSL SocketFactory
//        SSLContext context = SSLContext.getInstance("TLS");
//        context.init(null, null, null);
//        SSLSocketFactory sslSocketFactory = context.getSocketFactory();
//        httpsURLConnection.setSSLSocketFactory(sslSocketFactory);
//
////        logger.info("HTTP Response Code {}", httpsURLConnection.getResponseCode());
//
//        Certificate[] serverCertificate = httpsURLConnection.getServerCertificates();
    }


//    public String post(String url, String object) {
//        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
//        HttpPost request = new HttpPost(this.backendUrl + url);
//        request.addHeader("Accept", "application/json");
//        String encodedCredentials = this.encode(this.backendUsername + ":" + this.backendPassword);
//        request.addHeader("Authorization", "Basic " + encodedCredentials);
//        request.addHeader("Content-Type", "application/json");
//        String responseString = null;
//
//        try {
//            StringEntity entity = new StringEntity(object);
//            request.setEntity(entity);
//            HttpResponse response = httpClient.execute(request);
//            if (logger.isDebugEnabled()) {
//                logger.debug("Request successful for " + url);
//            }
//            responseString = EntityUtils.toString(response.getEntity(), "UTF-8");
//
//        } catch (IllegalStateException e) {
//            logger.error("The response is empty ");
//        } catch (NullPointerException e) {
//            logger.error("Bad request to the URL");
//        } catch (IOException e) {
//            logger.error("The request was unsuccessful with dss");
//        }
//        return responseString;
//    }


    private String encode(String text) {
        String returnString = null;
        try {
            returnString = BaseEncoding.base64().encode(text.getBytes("utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return returnString;
    }

}
