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

package org.wso2.external_contributions.httpClient;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;

/*
 * Handle get request to backend
 */
public class HttpHandler {
    private static final Logger logger = Logger.getLogger(HttpHandler.class);
    private static final PropertyReader propertyReader = new PropertyReader();
    private String backendUrl;

    public HttpHandler() {
        this.backendUrl = propertyReader.getBackendUrl();
    }

    public String httpsGet(String url) throws IOException, KeyManagementException, NoSuchAlgorithmException, KeyStoreException, CertificateException {

        InputStream file = Thread.currentThread().getContextClassLoader()
                .getResourceAsStream(propertyReader.getTrustStoreFile());
        KeyStore keyStore = KeyStore.getInstance("PKCS12");
        keyStore.load(file, propertyReader.getTrustStorePassword().toCharArray());
        SSLContext sslContext = SSLContexts.custom().loadTrustMaterial(keyStore,null).build();
        HostnameVerifier allowAllHosts = new NoopHostnameVerifier();
        SSLConnectionSocketFactory sslConnectionSocketFactory = new SSLConnectionSocketFactory(sslContext,allowAllHosts);
        CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(sslConnectionSocketFactory).build();
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
    }
}
