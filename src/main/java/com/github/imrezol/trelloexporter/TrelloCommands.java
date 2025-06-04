package com.github.imrezol.trelloexporter;

import com.github.imrezol.trelloexporter.trello.service.API;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.*;

import java.util.List;

@ShellComponent
@ShellCommandGroup("Trello commands")
public class TrelloCommands {


    @Autowired
    private API trelloApi;


    @ShellMethod(key = "e", value = "Export")
    public String export() {

        List<String> boards = trelloApi.getBoards()
                .stream()
                .map(board -> String.format("%s: %s , %s", board.id, board.name, board.desc))
                .toList();

        return "Boards:\n" + String.join("\n", boards);
    }


    @ShellMethod(key = "l", value = "List boards")
    public String listBoards(
    ) {
        List<String> boards = trelloApi.getBoards()
                .stream()
                .map(board -> String.format("%s: %s , %s", board.id, board.name, board.desc))
                .toList();


        return "Boards:\n" + String.join("\n", boards);
    }

}