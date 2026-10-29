package com.todoapp.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Service
public class CohereService {

    @Value("${cohere.api.key}")
    private String cohereApiKey;

    private final OkHttpClient httpClient;
    private final ObjectMapper objectMapper;

    public CohereService() {
        this.httpClient = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build();
        this.objectMapper = new ObjectMapper();
    }

    public String summarizeText(String text) throws IOException {
        if (cohereApiKey == null || cohereApiKey.isBlank()) {
            throw new IOException("Cohere API key is not configured. Set COHERE_API_KEY in the backend environment.");
        }

        String url = "https://api.cohere.com/v2/chat";
        ObjectNode requestBodyJson = objectMapper.createObjectNode();
        requestBodyJson.put("model", "command-a-plus-05-2026");
        requestBodyJson.put("max_tokens", 300);
        ArrayNode messages = requestBodyJson.putArray("messages");
        messages.addObject()
                .put("role", "system")
                .put("content", "Summarize the to-do items clearly and concisely as a paragraph.");
        messages.addObject()
                .put("role", "user")
                .put("content", text);

        RequestBody body = RequestBody.create(
                requestBodyJson.toString(),
                MediaType.parse("application/json; charset=utf-8")
        );

        Request request = new Request.Builder()
                .url(url)
                .header("Authorization", "Bearer " + cohereApiKey)
                .header("Content-Type", "application/json")
                .post(body)
                .build();

        try (Response response = httpClient.newCall(request).execute()) {
            ResponseBody responseBody = response.body();
            String responseText = responseBody == null ? "" : responseBody.string();
            if (!response.isSuccessful()) {
                throw new IOException("Cohere API request failed with HTTP " + response.code() + ": " + responseText);
            }

            JsonNode jsonNode = objectMapper.readTree(responseText);
            JsonNode content = jsonNode.at("/message/content");
            StringBuilder summary = new StringBuilder();
            if (content.isArray()) {
                for (JsonNode block : content) {
                    if ("text".equals(block.path("type").asText())) {
                        String textBlock = block.path("text").asText("");
                        if (!textBlock.isBlank()) {
                            if (summary.length() > 0) {
                                summary.append("\n");
                            }
                            summary.append(textBlock);
                        }
                    }
                }
            }
            if (summary.isEmpty()) {
                throw new IOException("Cohere response did not contain generated summary text.");
            }
            return summary.toString();
        }
    }
}
