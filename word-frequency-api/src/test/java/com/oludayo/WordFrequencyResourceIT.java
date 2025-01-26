
package com.oludayo;
import java.io.File;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import static io.restassured.RestAssured.given;

@QuarkusTest
public class WordFrequencyResourceIT {

    @Test
    void testValidFileUpload() {
        File testFile = new File("src/test/resources/sample.txt"); 

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