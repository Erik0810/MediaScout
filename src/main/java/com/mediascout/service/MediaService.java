package com.mediascout.service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.github.cdimascio.dotenv.Dotenv;

@Service
public class MediaService {

    private static final Dotenv dotenv = Dotenv.load();
    private static final String OPENAI_API_KEY = dotenv.get("OPENAI_API_KEY");
    private static final String OPENAI_API_URL = "https://api.openai.com/v1/chat/completions";
    private static final String GOOGLE_API_KEY = dotenv.get("GOOGLE_API_KEY");
    private static final String GOOGLE_CSE_ID = dotenv.get("GOOGLE_CSE_ID");
    private static final String GOOGLE_API_URL = "https://www.googleapis.com/customsearch/v1";

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final RestTemplate restTemplate = new RestTemplate();

    public String getMediaSuggestion(String mediaType, String timePeriod, String additionalInfo) {
        String userPrompt = String.format("Find the %s im thinking of from the %s with the following details: %s", 
                                            mediaType, timePeriod, additionalInfo);

        Map<String, Object> openaiRequest = new HashMap<>();
        openaiRequest.put("model", "gpt-4-turbo-preview");
        openaiRequest.put("messages", List.of(
                Map.of("role", "system", "content", "You are a helpful assistant that find the media a user is thinking of (movies, books, music, etc.). Only provide the title."),
                Map.of("role", "user", "content", userPrompt)
        ));
        openaiRequest.put("max_tokens", 150);
        openaiRequest.put("temperature", 0.7);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + OPENAI_API_KEY);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(openaiRequest, headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(OPENAI_API_URL, HttpMethod.POST, entity, String.class);
            return parseOpenAIResponse(response.getBody());
        } catch (HttpClientErrorException e) {
            System.err.println("Error calling OpenAI API: " + e.getMessage());
            return null;
        }
    }

    private String parseOpenAIResponse(String responseBody) {
        try {
            JsonNode rootNode = objectMapper.readTree(responseBody);
            JsonNode choicesNode = rootNode.get("choices");

            if (choicesNode != null && choicesNode.isArray() && choicesNode.size() > 0) {
                JsonNode messageNode = choicesNode.get(0).get("message");
                if (messageNode != null) {
                    return messageNode.get("content").asText().trim();
                }
            }
        } catch (Exception e) {
            System.err.println("Error parsing OpenAI response: " + e.getMessage());
        }
        return null;
    }

    public String fetchImageFromGoogle(String query) {
        try {
            String encodedQuery = URLEncoder.encode(query, StandardCharsets.UTF_8);
            String url = String.format("%s?q=%s&key=%s&cx=%s&searchType=image&num=1", 
                                       GOOGLE_API_URL, encodedQuery, GOOGLE_API_KEY, GOOGLE_CSE_ID);

            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            JsonNode rootNode = objectMapper.readTree(response.getBody());

            if (rootNode.has("items") && rootNode.get("items").isArray() && rootNode.get("items").size() > 0) {
                return rootNode.get("items").get(0).get("link").asText();
            }
        } catch (Exception e) {
            System.err.println("Error fetching image: " + e.getMessage());
        }
        return null;
    }
}
