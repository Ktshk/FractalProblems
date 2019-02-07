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

    public static final String ELASTICBEANSTALK_URL = "https://helloworld-env-1.ha4x2kktxp.us-east-2.elasticbeanstalk.com/demo";

    @Ignore
    @Test
    public void testLocalHttpRequest() {
        makeHttpRequest("http://localhost:8080/demo");
    }

    @Ignore
    @Test
    public void testElasticbeanRequest() {
        makeHttpRequest(ELASTICBEANSTALK_URL);
    }

    @Ignore
    @Test
    public void testHTTPSElasticbeanRequest() throws NoSuchAlgorithmException, KeyManagementException, IOException {
        // configure the SSLContext with a TrustManager
        SSLContext ctx = SSLContext.getInstance("TLS");
        ctx.init(new KeyManager[0], new TrustManager[] {new DefaultTrustManager()}, new SecureRandom());
        SSLContext.setDefault(ctx);

        URL url = new URL(ELASTICBEANSTALK_URL);
        HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
        conn.setDoOutput(true);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        conn.setRequestProperty("Accept", "application/json");

        OutputStream os = conn.getOutputStream();
        os.write(requestJson().getBytes("UTF-8"));
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
            HttpPost httpPost = new HttpPost(url);
            StringEntity params = new StringEntity(requestJson(), "UTF-8");
            httpPost.addHeader("content-type", "application/json; charset=UTF-8");

            httpPost.setEntity(params);

            System.out.println("Executing request " + httpPost.getRequestLine());

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
            String responseBody = httpclient.execute(httpPost, responseHandler);
            System.out.println("----------------------------------------");
            System.out.println(responseBody);
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

    private String requestJson() {
        return "{\n" +
                "  \"meta\": {\n" +
                "    \"locale\": \"ru-RU\",\n" +
                "    \"timezone\": \"Europe/Moscow\",\n" +
                "    \"client_id\": \"ru.yandex.searchplugin/5.80 (Samsung Galaxy; Android 4.4)\",\n" +
                "    \"interfaces\": {\n" +
                "      \"screen\": { }\n" +
                "    }\n" +
                "  },\n" +
                "  \"request\": {\n" +
                "    \"command\": \"закажи пиццу на улицу льва толстого, 16 на завтра\",\n" +
                "    \"original_utterance\": \"закажи пиццу на улицу льва толстого, 16 на завтра\",\n" +
                "    \"type\": \"SimpleUtterance\",\n" +
                "    \"markup\": {\n" +
                "      \"dangerous_context\": true\n" +
                "    },\n" +
                "    \"payload\": {},\n" +
                "    \"nlu\": {\n" +
                "      \"tokens\": [\n" +
                "        \"закажи\",\n" +
                "        \"пиццу\",\n" +
                "        \"на\",\n" +
                "        \"льва\",\n" +
                "        \"толстого\",\n" +
                "        \"16\",\n" +
                "        \"на\",\n" +
                "        \"завтра\"\n" +
                "      ],\n" +
                "      \"entities\": [\n" +
                "        {\n" +
                "          \"tokens\": {\n" +
                "            \"start\": 2,\n" +
                "            \"end\": 6\n" +
                "          },\n" +
                "          \"type\": \"YANDEX.GEO\",\n" +
                "          \"value\": {\n" +
                "            \"house_number\": \"16\",\n" +
                "            \"street\": \"льва толстого\"\n" +
                "          }\n" +
                "        },\n" +
                "        {\n" +
                "          \"tokens\": {\n" +
                "            \"start\": 3,\n" +
                "            \"end\": 5\n" +
                "          },\n" +
                "          \"type\": \"YANDEX.FIO\",\n" +
                "          \"value\": {\n" +
                "            \"first_name\": \"лев\",\n" +
                "            \"last_name\": \"толстой\"\n" +
                "          }\n" +
                "        },\n" +
                "        {\n" +
                "          \"tokens\": {\n" +
                "            \"start\": 5,\n" +
                "            \"end\": 6\n" +
                "          },\n" +
                "          \"type\": \"YANDEX.NUMBER\",\n" +
                "          \"value\": 16\n" +
                "        },\n" +
                "        {\n" +
                "          \"tokens\": {\n" +
                "            \"start\": 6,\n" +
                "            \"end\": 8\n" +
                "          },\n" +
                "          \"type\": \"YANDEX.DATETIME\",\n" +
                "          \"value\": {\n" +
                "            \"day\": 1,\n" +
                "            \"day_is_relative\": true\n" +
                "          }\n" +
                "        }\n" +
                "      ]\n" +
                "    }\n" +
                "  },\n" +
                "  \"session\": {\n" +
                "    \"new\": true,\n" +
                "    \"message_id\": 4,\n" +
                "    \"session_id\": \"2eac4854-fce721f3-b845abba-20d60\",\n" +
                "    \"skill_id\": \"3ad36498-f5rd-4079-a14b-788652932056\",\n" +
                "    \"user_id\": \"AC9WC3DF6FCE052E45A4566A48E6B7193774B84814CE49A922E163B8B29881DC\"\n" +
                "  },\n" +
                "  \"version\": \"1.0\"\n" +
                "}";
    }

    private static class DefaultTrustManager implements X509TrustManager {

        @Override
        public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {}

        @Override
        public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {}

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }
    }
}
