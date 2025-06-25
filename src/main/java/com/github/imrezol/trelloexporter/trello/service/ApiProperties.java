package com.github.imrezol.trelloexporter.trello.service;

import jakarta.annotation.PostConstruct;
import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


@Component
public class ApiProperties {

    private static final Logger logger = LoggerFactory.getLogger(ApiProperties.class);

    @Value("${trello.apikey}")
    public String apiKey;

    @Value("${trello.token}")
    public String token;

    public  final String scheme = "https";
    public  final String apiUrl = "api.trello.com";

    @PostConstruct
    public void init()  {
        List<String> errors = new ArrayList<>();
        if (Strings.isBlank(apiKey)) {
            errors.add("No TRELLO_API_KEY environment variable");
        }
        if (Strings.isBlank(token)) {
            errors.add("No TRELLO_TOKEN environment variable");
        }

        if (!errors.isEmpty()) {
            logger.error("*******************************************");
            for (String error : errors) {
                logger.error(error);
            }
            logger.error("To get API key and token visit this site: https://trello.com/app-key");
            logger.error("*******************************************");
            System.exit(1);
        }
    }
}
