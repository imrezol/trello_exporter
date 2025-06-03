package com.github.imrezol.trelloexporter;

import org.springframework.shell.Availability;
import org.springframework.shell.standard.*;

import java.util.Optional;

@ShellComponent
@ShellCommandGroup("Trello commands")
public class TrelloCommands {

    private Optional<String> apiKey = Optional.empty();

    @ShellMethod(key = "api-key", value = "Set Trello API key")
    public String setApiKey(
            @ShellOption(defaultValue = "spring") String newApiKey
    ) {
        apiKey = Optional.of(newApiKey);
        return "API key set.";
    }

    @ShellMethod(key = "list-boards", value = "List boards")
    public String listBoards(
    ) {
        return "Boards";
    }

    @ShellMethodAvailability({"list-boards"})
    public Availability apiKeyCheck()
    {
        return apiKey.isPresent() ? Availability.available() : Availability.unavailable("You must set Trello API key first");
    }

}