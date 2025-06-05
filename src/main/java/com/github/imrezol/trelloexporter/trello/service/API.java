package com.github.imrezol.trelloexporter.trello.service;

import com.github.imrezol.trelloexporter.trello.dto.Board;
import com.github.imrezol.trelloexporter.trello.dto.Card;
import com.github.imrezol.trelloexporter.trello.dto.CardDetails;
import com.github.imrezol.trelloexporter.trello.dto.TrelloList;
import jakarta.annotation.PostConstruct;
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

    private HttpHeaders headers;

    @PostConstruct
    public void init()  {
        headers = new HttpHeaders();
        headers.set("accept", "application/json");
        headers.set("Authorization", "Bearer JWT TOKEN HERE");
    }

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

        HttpEntity<Board> requestEntity = new HttpEntity<>(null, headers);

        ResponseEntity<List<Board>> response = restTemplate.exchange(url,
                HttpMethod.GET,
                requestEntity,
                new ParameterizedTypeReference<>() {
                });

        return response.getBody();
    }

    public List<TrelloList> getLists(String boardId){
        RestTemplate restTemplate = new RestTemplate();

        String url = generateUrl(String.format("1/boards/%s/lists",boardId));


        HttpEntity<TrelloList> requestEntity = new HttpEntity<>(null, headers);

        ResponseEntity<List<TrelloList>> response = restTemplate.exchange(url,
                HttpMethod.GET,
                requestEntity,
                new ParameterizedTypeReference<>() {
                });

        return response.getBody();
    }

    public List<Card> getCards(String listId){
        RestTemplate restTemplate = new RestTemplate();

        String url = generateUrl(String.format("1/lists/%s/cards",listId));

        HttpEntity<Card> requestEntity = new HttpEntity<>(null, headers);

        ResponseEntity<List<Card>> response = restTemplate.exchange(url,
                HttpMethod.GET,
                requestEntity,
                new ParameterizedTypeReference<>() {
                });

        return response.getBody();
    }

    public CardDetails getCard(String cardId) {
        RestTemplate restTemplate = new RestTemplate();

        String url = generateUrl(String.format("1/cards/%s",cardId));

        final String response = restTemplate.getForObject(url, String.class);

        System.out.println("-------------------------------");
        System.out.println(response);
        System.out.println("-------------------------------");

        return null;

    }

    private String generateUrl(String path){
        return UriComponentsBuilder.newInstance()
                .scheme("https")
                .host("api.trello.com")
                .path(path)
                .queryParam("key", apiKey)
                .queryParam("token", token)
                .encode()
                .toUriString();
    }
}
