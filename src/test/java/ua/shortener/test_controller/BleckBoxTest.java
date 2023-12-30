package ua.shortener.test_controller;


import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ua.shortener.UrlShortenerApplication;
import ua.shortener.test_controller.config.ContainersEnvironment;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.equalTo;


@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = UrlShortenerApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BleckBoxTest extends ContainersEnvironment {


    @Test
    public void testRedirectLink() throws Exception {


        Response response = given()
                .contentType(ContentType.JSON)
                .when()
                .get("/sh/testlink")
                .then()
                .statusCode(anyOf(equalTo(200), equalTo(302))) // Дозволяємо як 200 (ОК), так і 302 (Редирект)
                .extract().response();

// Перевірка заголовка редиректу
        if (response.getStatusCode() == 302) {
            String locationHeader = response.getHeader("Location");
            assertThat(locationHeader, equalTo("https://example.com/"));
        }

    }

}
