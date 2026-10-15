package com.todoapp.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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

    @Value("${cohere.api.model:command-a-plus-05-2026}")
    private String cohereModel;

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
        String url = "https://api.cohere.com/v2/chat";

        ObjectNode requestBodyJson = objectMapper.createObjectNode();
        requestBodyJson.put("model", cohereModel);
        requestBodyJson.putArray("messages")
                .addObject()
                .put("role", "user")
                .put("content", "Summarize these pending to-do items in a concise paragraph:\n" + text);

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
            if (!response.isSuccessful()) {
                String errorBody = response.body() == null ? "" : response.body().string();
                throw new IOException("Cohere Chat API returned HTTP " + response.code() + ": " + errorBody);
            }
            String responseBody = response.body().string();
            JsonNode jsonNode = objectMapper.readTree(responseBody);
            JsonNode content = jsonNode.path("message").path("content");
            if (content.isArray()) {
                for (JsonNode part : content) {
                    if ("text".equals(part.path("type").asText()) && part.hasNonNull("text")) {
                        return part.get("text").asText();
                    }
                }
            }
            throw new IOException("Cohere returned a response without summary text.");
        }
    }
}
