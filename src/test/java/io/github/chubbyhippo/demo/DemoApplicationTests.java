package io.github.chubbyhippo.demo;

import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder;
import com.github.tomakehurst.wiremock.client.WireMock;
import org.apache.hc.client5.http.impl.Wire;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import org.springframework.test.web.servlet.client.RestTestClient;
import org.springframework.util.StreamUtils;
import org.wiremock.spring.ConfigureWireMock;
import org.wiremock.spring.EnableWireMock;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@EnableWireMock(
        @ConfigureWireMock(baseUrlProperties = "hello.service.url")
)
@SpringBootTest(
        properties = "hello.service.url=${remote.hello.service.url}",
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
//@AutoConfigureRestTestClient
@AutoConfigureMockMvc
class DemoApplicationTests {

//    @Autowired
//    RestTestClient restTestClient;

    @Autowired
    MockMvcTester mockMvcTester;

    @Test
    @DisplayName("test get hello")
    void testGetHello() throws IOException {
        var helloJson = StreamUtils.copyToString(
                new ClassPathResource("hello.json").getInputStream(),
                StandardCharsets.UTF_8
        );

        WireMock.stubFor(WireMock.get("/hello")
                .willReturn(WireMock.aResponse()
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                        .withBody(helloJson)));

        mockMvcTester.post()
                .uri("/hello")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "name": "Wiremock"
                        }
                        """)
                .assertThat()
                .hasStatusOk();

//
//        restTestClient.post()
//                .uri("/hello")
//                .contentType(MediaType.APPLICATION_JSON)
//                .body("""
//                        {
//                          "name": "Wiremock"
//                        }
//                        """)
//                .exchange()
//                .expectStatus().isOk()
//                .expectBody().jsonPath("$.message").isEqualTo("Hello Wiremock");

    }

}
