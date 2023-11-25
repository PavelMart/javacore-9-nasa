import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.FileOutputStream;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        ObjectMapper mapper = new ObjectMapper();
        String imgUrl = null;

        CloseableHttpClient httpClient = HttpClientBuilder.create()
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setConnectTimeout(5000)
                        .setSocketTimeout(30000)
                        .setRedirectsEnabled(false)
                        .build())
                .build();

        HttpGet request = new HttpGet("https://api.nasa.gov/planetary/apod?api_key=BzeOaQifquRiHHbKJjydVZX8PcjjMPpfQoQUFkAW");

        try {
            CloseableHttpResponse response = httpClient.execute(request);
            NasaResponse data = mapper.readValue(response.getEntity().getContent(), new TypeReference<>() {});

            imgUrl = data.getUrl();
            String[] imgTitleSplit = imgUrl.split("/");
            String imgTitle = imgTitleSplit[imgTitleSplit.length - 1];

            request = new HttpGet(imgUrl);
            response = httpClient.execute(request);

            try (FileOutputStream fos = new FileOutputStream(imgTitle)) {
                response.getEntity().writeTo(fos);
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

    }
}
