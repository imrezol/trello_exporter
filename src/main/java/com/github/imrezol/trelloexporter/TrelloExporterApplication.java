package com.github.imrezol.trelloexporter;

import com.github.imrezol.trelloexporter.trello.dto.Card;
import com.github.imrezol.trelloexporter.trello.service.API;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

@SpringBootApplication
public class TrelloExporterApplication
		implements CommandLineRunner {

	@Autowired
	private Chooser chooser;

	@Autowired
	private API trelloAPI;


	private static Logger LOG = LoggerFactory
			.getLogger(TrelloExporterApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(TrelloExporterApplication.class, args);
	}

	@Override
	public void run(String... args) {

		chooser.chooseBoard();
		chooser.chooseList(chooser.getBoardId());

		List<Card> cards = trelloAPI.getCards(chooser.getListId());

		cards.forEach(card -> {
			System.out.printf("%s, %s%n",card.name, card.desc);
			trelloAPI.getCard(card.id);
		});

	}

}
