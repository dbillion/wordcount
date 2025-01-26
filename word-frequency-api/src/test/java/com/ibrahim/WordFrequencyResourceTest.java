package com.ibrahim;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;



public class WordFrequencyResourceTest {

    private final WordFrequencyResource resource = new WordFrequencyResource();

    @Test
    void testCountWords_EmptyInput() {
        Map<String, Long> result = resource.countWords("");
        assertEquals(0, result.size());
    }

  

    @Test
    void testCountWords_BasicSentence() {
        String sentence = "The quick brown fox jumps over the lazy dog.";
        Map<String, Long> result = resource.countWords(sentence);
        long total = result.values().stream().mapToLong(Long::longValue).sum();
        assertEquals(9, total);
    }

    @Test
    void testCountWords_WithPunctuation() {
        String input = "Hello! Hello? World...";
        Map<String, Long> result = resource.countWords(input);
        
        assertEquals(2, result.get("hello"));
        assertEquals(1, result.get("world"));
    }

    @Test
    void testCountWords_CaseInsensitivity() {
        String input = "Apple apple APPLE";
        Map<String, Long> result = resource.countWords(input);
        assertEquals(3, result.get("apple"));
    }
}