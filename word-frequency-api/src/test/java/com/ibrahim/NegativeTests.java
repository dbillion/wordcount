package com.ibrahim;  // Match your main package

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;
import java.io.File;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.*;

@QuarkusTest
public class NegativeTests {

// @Test
// void testInvalidFileType() {
//     File imageFile = new File("src/test/resources/image.png"); 
    
//     given()
//         .multiPart("file", imageFile)
//         .when()
//         .post("/words")
//         .then()
//         .statusCode(200)
//         .body(containsString("Invalid file type")); 
// }

}