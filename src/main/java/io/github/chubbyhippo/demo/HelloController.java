package io.github.chubbyhippo.demo;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
    private final HelloService helloService;

    public HelloController(HelloService helloService) {
        this.helloService = helloService;
    }

    @PostMapping("/hello")
    HelloResponse hello(@RequestBody HelloRequest request) {
        return helloService.getHello(request);
    }
}
