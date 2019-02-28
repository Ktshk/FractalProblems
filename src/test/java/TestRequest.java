import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.Ignore;
import org.junit.Test;

import javax.net.ssl.*;
import java.io.*;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

/**
 * Created by Katushka on 05.02.2019.
 */
public class TestRequest {

    public static final String ELASTICBEANSTALK_URL = "helloworld-env-1.ha4x2kktxp.us-east-2.elasticbeanstalk.com/demo";

    @Ignore
    @Test
    public void testLocalHttpRequest() {
        makeHttpRequest("http://localhost:8080/demo");
    }

    @Ignore
    @Test
    public void testElasticbeanRequest() {
        makeHttpRequest("http://" + ELASTICBEANSTALK_URL);
    }

    @Ignore
    @Test
    public void testHTTPSElasticbeanRequest() throws NoSuchAlgorithmException, KeyManagementException, IOException {
        // configure the SSLContext with a TrustManager
        SSLContext ctx = SSLContext.getInstance("TLS");
        ctx.init(new KeyManager[0], new TrustManager[]{new DefaultTrustManager()}, new SecureRandom());
        SSLContext.setDefault(ctx);

        URL url = new URL("https://" + ELASTICBEANSTALK_URL);
        HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
        conn.setDoOutput(true);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        conn.setRequestProperty("Accept", "application/json");

        OutputStream os = conn.getOutputStream();
        os.write(createJson("").getBytes("UTF-8"));
        os.close();

        conn.setHostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String arg0, SSLSession arg1) {
                return true;
            }
        });

        System.out.println(conn.getResponseCode());
        System.out.println(conn.getResponseMessage());

        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String inputLine;
        StringBuilder content = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine).append('\n');
        }
        System.out.println(content);
        in.close();

        conn.disconnect();
    }


    private void makeHttpRequest(String url) {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        try {
            // Create a custom response handler
            ResponseHandler<String> responseHandler = new ResponseHandler<String>() {

                @Override
                public String handleResponse(final HttpResponse response) throws IOException {
                    int status = response.getStatusLine().getStatusCode();
                    if (status >= 200 && status < 300) {
                        HttpEntity entity = response.getEntity();
                        return entity != null ? EntityUtils.toString(entity) : null;
                    } else {
                        throw new ClientProtocolException("Unexpected response status: " + status);
                    }
                }

            };

            executeHttpRequest(url, httpclient, responseHandler, "");
            executeHttpRequest(url, httpclient, responseHandler, "средняя");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                httpclient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void executeHttpRequest(String url, CloseableHttpClient httpclient, ResponseHandler<String> responseHandler, String text) throws IOException {
        HttpPost httpPost = new HttpPost(url);
        StringEntity params = new StringEntity(createJson(text), "UTF-8");
        httpPost.addHeader("content-type", "application/json; charset=UTF-8");

        httpPost.setEntity(params);

        System.out.println("Executing request " + httpPost.getRequestLine());

        String responseBody = httpclient.execute(httpPost, responseHandler);
        System.out.println("----------------------------------------");
        System.out.println(responseBody);
    }

    private String createJson(String text) {
        return "{\n" +
                "  \"meta\": {\n" +
                "    \"client_id\": \"ru.yandex.searchplugin/7.16 (none none; android 4.4.2)\",\n" +
                "    \"interfaces\": {\n" +
                "      \"screen\": {}\n" +
                "    },\n" +
                "    \"locale\": \"ru-RU\",\n" +
                "    \"timezone\": \"UTC\"\n" +
                "  },\n" +
                "  \"request\": {\n" +
                "    \"command\": \"" + text + "\",\n" +
                "    \"nlu\": {\n" +
                "      \"entities\": [],\n" +
                "      \"tokens\": [" +
                (!text.isEmpty() ? ("\n        \"" + text + "\"\n") : "") +
                "      ]\n" +
                "    },\n" +
                "    \"original_utterance\": \"нет\",\n" +
                "    \"type\": \"SimpleUtterance\"\n" +
                "  },\n" +
                "  \"session\": {\n" +
                "    \"message_id\": 3,\n" +
                "    \"new\": " + text.isEmpty() + ",\n" +
                "    \"session_id\": \"e58cb037-712e3090-33e4d929-cca75a07\",\n" +
                "    \"skill_id\": \"f99fbe77-b15a-4c5f-91a7-ab2595febe32\",\n" +
                "    \"user_id\": \"AA23058EB00BD2097FAE781A72ABF8AB303994B20C8DB2D0E281FA101296A015\"\n" +
                "  },\n" +
                "  \"version\": \"1.0\"\n" +
                "}";
    }

    private static class DefaultTrustManager implements X509TrustManager {

        @Override
        public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
        }

        @Override
        public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }
    }
}
