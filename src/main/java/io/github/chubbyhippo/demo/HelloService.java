package io.github.chubbyhippo.demo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class HelloService {

    private final RestClient restClient;

    public HelloService(RestClient.Builder restClientBuilder,
                        // must be here to make sure the value is injected
                        @Value("${hello.service.url}") String url) {
        this.restClient = restClientBuilder
                .baseUrl(url)
                .build();
    }

    HelloResponse getHello(HelloRequest request) {
        return restClient.get().uri("/hello", request.name())
                .retrieve()
                .body(HelloResponse.class);
    }
}
