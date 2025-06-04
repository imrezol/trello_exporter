package com.github.imrezol.trelloexporter;

import com.github.imrezol.trelloexporter.trello.dto.Board;
import com.github.imrezol.trelloexporter.trello.dto.TrelloList;
import com.github.imrezol.trelloexporter.trello.service.API;
import jakarta.annotation.PostConstruct;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
/*
https://github.com/jline/jline3
https://jline.org/docs/line-reader
 */
@Service
public class Chooser {

    @Autowired
    private API trelloAPI;

    private Terminal terminal;
    private LineReader reader;
    private PrintWriter writer;

    private String boardId;
    private String listId;

    @PostConstruct
    public void init() throws IOException {
        terminal = TerminalBuilder.builder().system(true).build();
         reader = LineReaderBuilder.builder().terminal(terminal).build();
         writer = terminal.writer();
    }

    public void chooseBoard(){
        writer.println("Getting boards...");
        writer.flush();
        List<Board> boards = trelloAPI.getBoards();

        while (boardId == null) {
            writer.println("Boards:");
        for (int i=0; i<boards.size();i++) {
            Board board = boards.get(i);
            String item = String.format("%d. %s", i, board.name);
            writer.println(item);
        }
        terminal.flush();


            String line = reader.readLine(String.format("Choose board (0-%d, default:0): ", boards.size()-1));
            if (line == null) {
                boardId = boards.getFirst().id;
            } else {
                int idx = Integer.parseInt(line);
                if (idx < 0 || idx > boards.size()-1) {
                    writer.println("Invalid choose");
                } else {
                    boardId = boards.get(idx).id;
                }
            }
        }
    }

    public void chooseList(String boardId){
        writer.println("Getting lists...");
        writer.flush();
        List<TrelloList> trelloLists = trelloAPI.getLists(boardId);

        while (listId == null) {
            writer.println("Boards:");
            for (int i=0; i<trelloLists.size();i++) {
                TrelloList list = trelloLists.get(i);
                String item = String.format("%d. %s", i, list.name);
                writer.println(item);
            }
            terminal.flush();


            String line = reader.readLine(String.format("Choose list (0-%d, default:0): ", trelloLists.size()-1));
            if (line == null) {
                listId = trelloLists.getFirst().id;
            } else {
                int idx = Integer.parseInt(line);
                if (idx < 0 || idx > trelloLists.size()-1) {
                    writer.println("Invalid choose");
                } else {
                    listId = trelloLists.get(idx).id;
                }
            }
        }
    }

    public String getListId() {
        return listId;
    }

    public String getBoardId() {
        return boardId;
    }
}
