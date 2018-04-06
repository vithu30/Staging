package org.wso2.external_contributions.serverlets;

import org.apache.log4j.Logger;
import org.wso2.external_contributions.httpClient.HttpHandler;

import javax.servlet.annotation.WebServlet;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

@WebServlet(
        name = "pullRequests",
        urlPatterns = "/pullRequests"
)
public class PullRequests extends HttpServlet {
    private static final Logger logger = Logger.getLogger(PullRequests.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {

        try {
            HttpHandler httpHandler = new HttpHandler();
            logger.info("Requesting backend ");
            String backResponse = httpHandler.httpsGet("/ballerinaService/pullRequests");
            logger.info("Got: " + backResponse);
            response.setContentType("application/json;charset=UTF-8");
            ServletOutputStream out = response.getOutputStream();
            out.print(backResponse);
        } catch (IOException e) {
            logger.error("The response output stream failed");
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
    }
}
