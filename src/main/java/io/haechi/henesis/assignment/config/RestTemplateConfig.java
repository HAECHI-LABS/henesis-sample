package io.haechi.henesis.assignment.config;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;

import java.io.IOException;
import java.util.Collections;

@Configuration
public class RestTemplateConfig {

    @Value("${restTemplate.factory.readTimeout}")
    private int READ_TIMEOUT;

    @Value("${restTemplate.factory.connectTimeout}")
    private int CONNECT_TIMEOUT;

    @Value("${restTemplate.httpClient.maxConnTotal}")
    private int MAX_CONN_TOTAL;

    @Value("${restTemplate.httpClient.maxConnPerRoute}")
    private int MAX_CONN_PER_ROUTE;

    private final String walletAccessToken;
    private final String walletApiSecret;
    private final String walletUrl;
    private final String masterWalletId;

    public RestTemplateConfig(
            @Qualifier("walletAccessToken") String walletAccessToken,
            @Qualifier("walletApiSecret") String walletApiSecret,
            @Qualifier("walletUrl") String walletUrl,
            @Qualifier("masterWalletId") String masterWalletId
    ) {
        this.walletAccessToken = walletAccessToken;
        this.walletApiSecret = walletApiSecret;
        this.walletUrl = walletUrl;
        this.masterWalletId = masterWalletId;
    }

    @Bean
    public RestTemplate defaultRestTemplate() {
        RestTemplate restTemplate = new RestTemplate(clientHttpRequestFactory());
        restTemplate.setUriTemplateHandler(new DefaultUriBuilderFactory());
        return restTemplate;
    }


    @Bean
    @Qualifier("walletClient")
    public RestTemplate walletRestTemplate(){
        RestTemplate restTemplate = new RestTemplate(clientHttpRequestFactory());
        restTemplate.setUriTemplateHandler(new DefaultUriBuilderFactory(
                String.format("%s/api/v2/eth",walletUrl)
        ));
        restTemplate.setInterceptors(
                Collections.singletonList(new HeaderInterceptor())
        );
        return restTemplate;
    }

    @Bean
    @Qualifier("masterWalletClient")
    public RestTemplate masterWalletRestTemplate(){
        RestTemplate restTemplate = new RestTemplate(clientHttpRequestFactory());
        restTemplate.setUriTemplateHandler(new DefaultUriBuilderFactory(
                String.format("%s/api/v2/eth/wallets/%s",walletUrl,masterWalletId)
        ));
        restTemplate.setInterceptors(
                Collections.singletonList(new HeaderInterceptor())
        );
        return restTemplate;
    }

    private ClientHttpRequestFactory clientHttpRequestFactory(){
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


    private class HeaderInterceptor implements ClientHttpRequestInterceptor{

        @Override
        public ClientHttpResponse intercept(org.springframework.http.HttpRequest request, byte[] body,
                                            ClientHttpRequestExecution execution) throws IOException {
            HttpHeaders headers = request.getHeaders();
            headers.add("X-Henesis-Secret", walletApiSecret);
            headers.add("Authorization","Bearer "+ walletAccessToken);
            return execution.execute(request, body);
        }
    }



}
