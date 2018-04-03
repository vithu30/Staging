package org.wso2.external_contributions;

import sun.net.www.protocol.http.HttpURLConnection;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

public class HttpsClient {
    final static String path = "/home/vithursa/Documents/ballerina-0.964.0/bre/security/final.p12";
    final static String password = "ballerina";

    final static String serverName = "localhost";
    final static int port  = 9097;
    static boolean debug = false;

    void doClientSide() throws IOException {

        SSLSocketFactory sslSocketFactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
        SSLSocket sslSocket = (SSLSocket) sslSocketFactory.createSocket(serverName,port);
        OutputStream outputStream = sslSocket.getOutputStream();
        outputStream.toString();
//        outputStream.write("Hello".getBytes());
        outputStream.flush();
        outputStream.close();

    }

    public static void main (String [] args) throws IOException {
        System.setProperty("javax.net.ssl.trustStore",path);
        System.setProperty("javax.net.ssl.trustStorePassword",password);
        System.setProperty("javax.net.ssl.trustStoreType", "JKS");
        if(debug){
            System.setProperty("javax.net.debug","all");
        }
        new HttpsClient().doClientSide();
    }
}
