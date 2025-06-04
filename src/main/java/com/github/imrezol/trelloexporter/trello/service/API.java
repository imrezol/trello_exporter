package com.github.imrezol.trelloexporter.trello.service;

import com.github.imrezol.trelloexporter.trello.dto.Board;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@Service
public class API {


    @Value("${trello.apikey}")
    private String apiKey;

    @Value("${trello.token}")
    private String token;

    public List<Board> getBoards(){
        RestTemplate restTemplate = new RestTemplate();
        //String url = "https://api.trello.com/1/members/me/boards?key={APIKey}&token={APIToken}";
        String url = UriComponentsBuilder.newInstance()
                .scheme("https")
                .host("api.trello.com")
                .path("1/members/me/boards")
                .queryParam("key", apiKey)
                .queryParam("token", token)
                .encode()
                .toUriString();

        HttpHeaders headers = new HttpHeaders();
        headers.set("accept", "application/json");
        headers.set("Authorization", "Bearer JWT TOKEN HERE");

        HttpEntity<Board> requestEntity = new HttpEntity<>(null, headers);

        ResponseEntity<List<Board>> response = restTemplate.exchange(url,
                HttpMethod.GET,
                requestEntity,
                new ParameterizedTypeReference<List<Board>>() {
                });

        return response.getBody();
    }

}
