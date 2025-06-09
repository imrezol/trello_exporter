package com.github.imrezol.trelloexporter;

import com.github.imrezol.trelloexporter.exporters.BoardsExporter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TrelloExporterApplication
		implements CommandLineRunner {

	@Autowired
	private BoardsExporter boards;


	public static void main(String[] args) {
		SpringApplication.run(TrelloExporterApplication.class, args);
	}

	@Override
	public void run(String... args) {

		boards.export();

	}

}
