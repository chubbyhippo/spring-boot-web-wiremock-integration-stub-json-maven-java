package io.github.chubbyhippo.demo;

import com.github.tomakehurst.wiremock.client.WireMock;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import org.wiremock.spring.ConfigureWireMock;
import org.wiremock.spring.EnableWireMock;

import java.io.IOException;
import java.nio.charset.Charset;

@EnableWireMock(
        @ConfigureWireMock(baseUrlProperties = "custom-url")
)
@SpringBootTest(
        properties = "hello.service.url=${custom-url}",
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@AutoConfigureMockMvc
class DemoApplicationTests {

    @Autowired
    MockMvcTester mockMvcTester;
    @Value("classpath:hello.json")
    Resource resource;

    @Test
    @DisplayName("test get hello")
    void testGetHello() throws IOException {
        var helloJson = resource.getContentAsString(Charset.defaultCharset());

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
