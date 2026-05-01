package io.github.chubbyhippo.demo;

import com.github.tomakehurst.wiremock.client.WireMock;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
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
@AutoConfigureMockMvc
class DemoApplicationTests {

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

    }

}
