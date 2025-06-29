package com.github.imrezol.trelloexporter;

import com.github.imrezol.trelloexporter.exporters.BoardExporter;
import com.github.imrezol.trelloexporter.exporters.BoardsExporter;
import com.github.imrezol.trelloexporter.exporters.CardExporter;
import com.github.imrezol.trelloexporter.trello.dto.Board;
import com.github.imrezol.trelloexporter.trello.dto.Card;
import com.github.imrezol.trelloexporter.trello.dto.CardAttachment;
import com.github.imrezol.trelloexporter.trello.dto.Checklist;
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

    @Autowired
    private BoardsExporter boardsExporter;

    @Autowired
    private BoardExporter boardExporter;

    @Autowired
    private CardExporter cardExporter;

    public List<String> jsonFilenames = new ArrayList<>();

    public static void main(String[] args) {
        SpringApplication.run(TrelloExporterApplication.class, args);
    }

    @Override
    public void run(String... args) {

        List<Board> boards = new ArrayList<>();
        for (String filename : args) {
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


        generateMdFiles(boards);
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
                    String targetDir = Utils.getUrl(Properties.attachmentsDir, Properties.baseDir, board.id, card.id);
                    for (CardAttachment attachment : card.attachments) {
                        trelloApi.downloadAttachment(card, attachment, targetDir);
                    }
                }
            }
        }

        System.out.println();
    }

    private void generateMdFiles(List<Board> boards) {
        boardsExporter.export(boards);

        for (Board board : boards) {
            boardExporter.export(board);

            Map<String, List<Checklist>> checklistsByCardId = new HashMap<>();

            for (Checklist checklist : board.checklists) {
                List<Checklist> checklists = checklistsByCardId.computeIfAbsent(checklist.idCard, k -> new ArrayList<>());
                checklists.add(checklist);
            }

            for (Card card : board.cards) {
                cardExporter.export(board.name, card, checklistsByCardId.get(card.id));
            }
        }

    }
}