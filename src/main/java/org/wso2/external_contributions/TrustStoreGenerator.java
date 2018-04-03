package org.wso2.external_contributions;

import org.wso2.external_contributions.msf4jhttp.PropertyReader;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

public class TrustStoreGenerator {
    private String trustStorePath;
    private String trustStorePassword;

    public TrustStoreGenerator(){
        PropertyReader reader = new PropertyReader();
        this.trustStorePath = reader.getCertPath();
        this.trustStorePassword = reader.getTrustStorePassword();
    }
    public SSLContext getTrustManagerFactory() throws IOException, CertificateException, NoSuchAlgorithmException, KeyStoreException, UnrecoverableKeyException, KeyManagementException {
        System.setProperty("javax.net.ssl.trustStore",trustStorePath);
        System.setProperty("javax.net.ssl.trustStorePassword",trustStorePassword);
        final char[] trustPassphrase = trustStorePassword.toCharArray();
        final KeyStore keyStore = KeyStore.getInstance("jks");
        keyStore.load(new FileInputStream(trustStorePath), trustPassphrase);
        final TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance("SunX509");
        trustManagerFactory.init(keyStore);
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null,trustManagerFactory.getTrustManagers(),null);
        return sslContext;
    }
    public void establishConnection() throws IOException, UnrecoverableKeyException, CertificateException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        String urlString = "https://localhost:9097/issues";
        URL url = new URL(urlString);
        HttpsURLConnection httpsURLConnection = (HttpsURLConnection) url.openConnection();
        httpsURLConnection.setSSLSocketFactory(getTrustManagerFactory().getSocketFactory());
        System.out.println(httpsURLConnection.getResponseCode());
    }

    public static void main(String [] args) throws CertificateException, UnrecoverableKeyException, NoSuchAlgorithmException, IOException, KeyManagementException, KeyStoreException {
        TrustStoreGenerator trustStoreGenerator = new TrustStoreGenerator();
        trustStoreGenerator.establishConnection();
    }

}
