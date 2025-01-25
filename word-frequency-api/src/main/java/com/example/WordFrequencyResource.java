package com.ibrahim;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.ExampleObject;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/words")
@Tag(name = "Word Frequency API", description = "APIs for analyzing word frequency in text files")
public class WordFrequencyResource {

    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
        summary = "Upload a text file to analyze word frequency",
        description = "This endpoint accepts a text file and returns total word count and frequencies."
    )
    @APIResponse(
        responseCode = "200",
        description = "Word frequency analysis result",
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON,
            examples = @ExampleObject(
                value = "{\"totalWords\": 5, \"frequencies\": {\"hello\": 3, \"world\": 1, \"quarkus\": 1}}"
            )
        )
    )
    @APIResponse(
        responseCode = "400",
        description = "Bad request if no file or empty file"
    )
    @APIResponse(
        responseCode = "500",
        description = "Internal server error if processing fails"
    )
    public Response uploadFile(
        @RequestBody(
            description = "Text file to upload",
            content = @Content(
                mediaType = MediaType.MULTIPART_FORM_DATA,
                schema = @Schema(implementation = FileUploadForm.class)
            )
        )
        @MultipartForm FileUploadForm form
    ) {
        if (form == null || form.getFile() == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("No file uploaded").build();
        }

        try (InputStream uploadedInputStream = form.getFile()) {
            byte[] bytes = uploadedInputStream.readAllBytes();
            
            if (bytes.length == 0) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("File is empty").build();
            }

            String content = new String(bytes, StandardCharsets.UTF_8);
            Map<String, Long> frequencies = countWords(content);
            long totalWords = frequencies.values().stream().mapToLong(Long::longValue).sum();

            Map<String, Object> result = new HashMap<>();
            result.put("totalWords", totalWords);
            result.put("frequencies", frequencies);

            return Response.ok(result).build();
            
        } catch (IOException e) {
            return Response.serverError()
                    .entity("Processing error: " + e.getMessage())
                    .build();
        }
    }

     Map<String, Long> countWords(String content) {
        return Arrays.stream(content.split("\\W+"))
                .filter(word -> !word.isEmpty())
                .map(String::toLowerCase)
                .collect(Collectors.groupingBy(
                        Function.identity(),
                        Collectors.counting()
                ));
    }
}