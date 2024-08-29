package com.bookingmeetingroom.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class JsonPlaceholderClient {

    @Value("${jsonplaceholder.base-url}")
    private String baseUrl;

    private final RestTemplate restTemplate;

    public JsonPlaceholderClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public ResponseEntity<String> createPost(String postJson) {
        String url = baseUrl + "/posts";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(org.springframework.http.MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(postJson, headers);
        return restTemplate.exchange(url, HttpMethod.POST, request, String.class);
    }

    public ResponseEntity<String> updatePost(Long id, String postJson) {
        String url = baseUrl + "/posts/" + id;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(org.springframework.http.MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(postJson, headers);
        return restTemplate.exchange(url, HttpMethod.PUT, request, String.class);
    }

    public ResponseEntity<Void> deletePost(Long id) {
        String url = baseUrl + "/posts/" + id;
        return restTemplate.exchange(url, HttpMethod.DELETE, null, Void.class);
    }
}
