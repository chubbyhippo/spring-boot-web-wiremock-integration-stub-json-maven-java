package io.github.chubbyhippo.demo;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class HelloService {

    private final RestClient restClient;

    public HelloService(RestClient.Builder restClientBuilder) {
        this.restClient = restClientBuilder
                .baseUrl("http://localhost:9999")
                .build();
    }

    HelloResponse getHello(HelloRequest request) {
        return restClient.get().uri("/hello", request.name())
                .retrieve()
                .body(HelloResponse.class);
    }
}
