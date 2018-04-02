package org.wso2.external_contributions;

import org.wso2.external_contributions.msf4jhttp.PropertyReader;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

public class TrustStoreGenerator {
    private String certPath;
    private String trustStorePassword;

    public TrustStoreGenerator(){
        PropertyReader reader = new PropertyReader();
        this.certPath = reader.getCertPath();
        this.trustStorePassword = reader.getTrustStorePassword();
    }
    public void setSslSystemProperties() throws IOException, CertificateException, NoSuchAlgorithmException, KeyStoreException, UnrecoverableKeyException {
        final char[] trustPassphrase = trustStorePassword.toCharArray();
        final KeyStore keyStore = KeyStore.getInstance("jks");
        keyStore.load(new FileInputStream(certPath), trustPassphrase);
        final TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance("SunX509");
        trustManagerFactory.init(keyStore);
    }

}
