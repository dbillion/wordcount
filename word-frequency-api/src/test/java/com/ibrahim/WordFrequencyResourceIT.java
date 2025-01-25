
package com.ibrahim;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import java.io.File;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.*;

@QuarkusTest
public class WordFrequencyResourceIT {

    @Test
    void testValidFileUpload() {
        File testFile = new File("src/test/resources/sample.txt"); // Sample: "Hello world hello"

        given()
            .multiPart("file", testFile)
            .when()
            .post("/words")
            .then()
            .statusCode(200)
            .body("totalWords", equalTo(3))
            .body("frequencies.hello", equalTo(2))
            .body("frequencies.world", equalTo(1));
    }

    @Test
    void testNoFileUpload() {
        given()
            .when()
            .post("/words")
            .then()
            .statusCode(400)
            .body(containsString("No file uploaded"));
    }

    @Test
    void testEmptyFileUpload() {
        File emptyFile = new File("src/test/resources/empty.txt");

        given()
            .multiPart("file", emptyFile)
            .when()
            .post("/words")
            .then()
            .statusCode(400)
            .body(containsString("File is empty"));
    }
}