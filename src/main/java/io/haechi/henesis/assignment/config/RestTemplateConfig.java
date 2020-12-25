package io.haechi.henesis.assignment.config;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;

import java.io.IOException;
import java.util.Collections;

@Configuration
public class RestTemplateConfig {
    private static final int READ_TIMEOUT = 10000;
    private static final int CONNECT_TIMEOUT = 5000;
    private static final int MAX_CONN_TOTAL = 100;
    private static final int MAX_CONN_PER_ROUTE = 5;

    private final String walletAccessToken;
    private final String walletApiSecret;
    private final String walletUrl;


    public RestTemplateConfig(
            HenesisProperties henesisProperties
    ) {
        this.walletApiSecret = henesisProperties.getApiSecret();
        this.walletAccessToken = henesisProperties.getAccessToken();
        this.walletUrl = henesisProperties.getUrl();
    }

    @Bean
    @Qualifier("henesisRestTemplate")
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate(clientHttpRequestFactory());
        restTemplate.setUriTemplateHandler(new DefaultUriBuilderFactory(
                String.format("%s/api/v2/", walletUrl)
        ));
        restTemplate.setInterceptors(
                Collections.singletonList(new HeaderInterceptor())
        );
        return restTemplate;
    }


    private ClientHttpRequestFactory clientHttpRequestFactory() {
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        factory.setReadTimeout(READ_TIMEOUT);
        factory.setConnectTimeout(CONNECT_TIMEOUT);

        HttpClient httpClient = HttpClientBuilder.create()
                .setMaxConnTotal(MAX_CONN_TOTAL)
                .setMaxConnPerRoute(MAX_CONN_PER_ROUTE)
                .build();
        factory.setHttpClient(httpClient);
        return factory;
    }


    private class HeaderInterceptor implements ClientHttpRequestInterceptor {

        @Override
        public ClientHttpResponse intercept(org.springframework.http.HttpRequest request, byte[] body,
                                            ClientHttpRequestExecution execution) throws IOException {
            HttpHeaders headers = request.getHeaders();
            headers.add("X-Henesis-Secret", walletApiSecret);
            headers.add("Authorization", "Bearer " + walletAccessToken);
            return execution.execute(request, body);
        }
    }


}
