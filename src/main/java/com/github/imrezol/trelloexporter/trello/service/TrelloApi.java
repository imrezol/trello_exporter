package com.github.imrezol.trelloexporter.trello.service;

import com.github.imrezol.trelloexporter.utils.ConsoleUtil;
import com.github.imrezol.trelloexporter.trello.dto.Card;
import com.github.imrezol.trelloexporter.trello.dto.CardAttachment;
import com.github.imrezol.trelloexporter.utils.FileUtil;
import jakarta.annotation.PostConstruct;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

@Service
public class TrelloApi {

    @Autowired
    private ApiProperties apiProperties;

    private HttpHeaders headers;

    private final OkHttpClient client = new OkHttpClient.Builder()
            .build();

    @PostConstruct
    public void init() {
        headers = new HttpHeaders();
        headers.set("accept", "application/json");
        headers.set("Authorization", "Bearer JWT TOKEN HERE");
    }

    public String getBoards() {
        RestTemplate restTemplate = new RestTemplate();

        String url = generateUrl("1/members/me/boards");

        return restTemplate.getForObject(url, String.class);
    }

    public String getBoardJson(String boardId) {
        RestTemplate restTemplate = new RestTemplate();
        String url = generateUrl("1//boards/" + boardId);

        return restTemplate.getForObject(url, String.class);
    }

    public String getLists(String boardId) {
        RestTemplate restTemplate = new RestTemplate();

        String url = generateUrl("1/boards/" + boardId + "/lists");


        return restTemplate.getForObject(url, String.class);
    }


    public String getCards(String listId) {
        RestTemplate restTemplate = new RestTemplate();

        String url = generateUrl("1/lists/" + listId + "/cards");

        return restTemplate.getForObject(url, String.class);
    }

    public String getCard(String cardId) {
        RestTemplate restTemplate = new RestTemplate();

        String url = generateUrl("1/cards/" + cardId);

        return restTemplate.getForObject(url, String.class);

    }

    public String getChecklists(String cardId) {
        RestTemplate restTemplate = new RestTemplate();

        String url = generateUrl("1/cards/" + cardId + "/checklists");

        return restTemplate.getForObject(url, String.class);
    }

    public String getActions(String cardId) {
        RestTemplate restTemplate = new RestTemplate();
        // https://developer.atlassian.com/cloud/trello/guides/rest-api/api-introduction/#paging
        // https://stackoverflow.com/questions/51777063/how-can-i-get-all-actions-for-a-board-using-trellos-rest-api
//        String url = generateUrl(String.format("1/cards/%s/",cardId)) + "&actions=all";
        String url = generateUrl("1/cards/" + cardId + "/actions") + "&limit=1000";

        return restTemplate.getForObject(url, String.class);
    }

    public String getAttachments(String cardId) {
        RestTemplate restTemplate = new RestTemplate();

        String url = generateUrl("1/cards/" + cardId + "/attachments");

        return restTemplate.getForObject(url, String.class);
    }

    public void downloadAttachment(Card card, CardAttachment attachment, String targetDir) {

        System.out.println(ConsoleUtil.pad(1, attachment.fileName));

        FileUtil.ensureDirectory(targetDir);
        String fileName = FileUtil.getUrl(targetDir, attachment.getLocalFilename());

        String downloadUrl = generateUrl(
                "/1/cards/" + card.id +
                        "/attachments/" + attachment.id +
                        "/download/" + attachment.fileName
        );


        Request request = new Request.Builder()
                .header("Authorization", " OAuth oauth_consumer_key=\"" + apiProperties.apiKey + "\", oauth_token=\"" + apiProperties.token + "\"")
                .url(downloadUrl).build();
        try (Response response = client.newCall(request).execute()) {

            if (!response.isSuccessful()) {
                throw new RuntimeException("Failed to download file: " + response);
            }

            try (InputStream in = response.body().byteStream();
                 FileOutputStream out = new FileOutputStream(fileName)) {

                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = in.read(buffer)) != -1) {
                    out.write(buffer, 0, bytesRead);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String generateUrl(String path) {
        return UriComponentsBuilder.newInstance()
                .scheme(apiProperties.scheme)
                .host(apiProperties.apiUrl)
                .path(path)
                .queryParam("key", apiProperties.apiKey)
                .queryParam("token", apiProperties.token)
                .encode()
                .toUriString();
    }


}
