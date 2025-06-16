package com.github.imrezol.trelloexporter.trello.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ApiProperties {

    @Value("${trello.apikey}")
    public String apiKey;

    @Value("${trello.token}")
    public String token;

    public  final String scheme = "https";
    public  final String apiUrl = "api.trello.com";

}
