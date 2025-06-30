package com.github.imrezol.trelloexporter;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.imrezol.trelloexporter.generator.*;
import com.github.imrezol.trelloexporter.generator.CardGenerator;
import com.github.imrezol.trelloexporter.trello.dto.*;
import com.github.imrezol.trelloexporter.trello.service.ApiProperties;
import com.github.imrezol.trelloexporter.trello.service.TrelloApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.*;

@SpringBootApplication
public class TrelloExporterApplication
        implements CommandLineRunner {

    @Autowired
    private ApiProperties apiProperties;

    @Autowired
    private TrelloApi trelloApi;


    public List<String> jsonFilenames = new ArrayList<>();

    public static void main(String[] args) {
        SpringApplication.run(TrelloExporterApplication.class, args);
    }

    @Override
    public void run(String... args) {

        List<Board> boards = new ArrayList<>();
        for (String filename : args) {
            System.out.println("Processing file: " + filename);
            try {
                String jsonString = Files.readString(Path.of(filename), StandardCharsets.UTF_8);
                Board board = Board.fromJson(jsonString);
                boards.add(board);
            } catch (NoSuchFileException ee) {
                throw new RuntimeException(ee); // TODO
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

 downloadAttachments(boards);

        debug(boards);

        generateFiles(boards);
    }

    private static void debug(List<Board> boards) {
        Set<String> actionTypes = new HashSet<>();

        for (Board board : boards) {
            for (Action action : board.actions) {
                actionTypes.add(action.type);

                if (!action.isCardRelated() && !action.isBoardRelated()) {
                    System.out.println(Utils.lineSeparator);
                    System.out.println("Action type: " + action.type);
                    String s = null;
                    try {
                        ObjectMapper objectMapper = Utils.getObjectMapper();
                        objectMapper.setSerializationInclusion(Include.NON_NULL);
                        s = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(action.data);
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                    System.out.println(s);
                    System.out.println(Utils.lineSeparator);
                    System.exit(0);
                }
            }
        }
    }

    private void downloadAttachments(List<Board> boards) {

        System.out.println("Downloading attachments:");

        if (!apiProperties.isConfigured) {

            System.out.println(Utils.lineSeparator);

            System.out.println("No TRELLO_API_KEY or TRELLO_TOKEN environment variable, attachments won't downloaded");
            System.out.println("To get API key and token visit this site: https://trello.com/app-key");

            System.out.println(Utils.lineSeparator);
            System.out.println("Press Enter to continue...");
            System.out.println(Utils.lineSeparator);
            Scanner scanner = new Scanner(System.in);
            scanner.nextLine();
        } else {
            for (Board board : boards) {
                for (Card card : board.cards) {
                    String targetDir = Utils.getUrl(CardGenerator.attachmentsDir, Properties.baseDir, board.id, card.id);
                    for (CardAttachment attachment : card.attachments) {
                        trelloApi.downloadAttachment(card, attachment, targetDir);
                    }
                }
            }
        }

        System.out.println();
    }

    private void generateFiles(List<Board> boards) {

        Generator htmlGenerator = new HtmlGenerator();
        Generator mdGenerator = new MdGenerator();

        new BoardsGenerator(htmlGenerator, boards).generate();
        new BoardsGenerator(mdGenerator, boards).generate();

        for (Board board : boards) {
            new BoardGenerator(htmlGenerator, board).generate();
            new BoardGenerator(mdGenerator, board).generate();

            Map<String, List<Checklist>> checklistsByCardId = getChecklistsByCardId(board);
            Map<String, List<Action>> actionsByCardId = getActionsByCardId(board);
            Map<String, TrelloList> listsById = getListsByCard(board);
            for (Card card : board.cards) {

                new CardGenerator(mdGenerator,card, board.name, listsById.get(card.idList).name, checklistsByCardId.get(card.id), actionsByCardId.get(card.id))
                        .generate();

                new CardGenerator(htmlGenerator,card, board.name, listsById.get(card.idList).name, checklistsByCardId.get(card.id), actionsByCardId.get(card.id))
                        .generate();
            }
        }

    }

    private static Map<String, List<Action>> getActionsByCardId(Board board) {
        Map<String, List<Action>> actionsByCardId = new HashMap<>();

        for (Action acction : board.actions) {
            List<Action> actions = actionsByCardId.computeIfAbsent(acction.data.idCard, k -> new ArrayList<>());
            actions.add(acction);
        }

        return actionsByCardId;
    }

    private static Map<String, List<Checklist>> getChecklistsByCardId(Board board) {
        Map<String, List<Checklist>> checklistsByCardId = new HashMap<>();

        for (Checklist checklist : board.checklists) {
            List<Checklist> checklists = checklistsByCardId.computeIfAbsent(checklist.idCard, k -> new ArrayList<>());
            checklists.add(checklist);
        }
        return checklistsByCardId;
    }

    private static Map<String, TrelloList> getListsByCard(Board board) {
        Map<String, TrelloList> listsById = new HashMap<>();

        for (TrelloList trelloList : board.lists) {
            listsById.put(trelloList.id, trelloList);
        }
        return listsById;
    }
}