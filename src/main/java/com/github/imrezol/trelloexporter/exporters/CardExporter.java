package com.github.imrezol.trelloexporter.exporters;

import com.github.imrezol.trelloexporter.Properties;
import com.github.imrezol.trelloexporter.trello.dto.Card;
import com.github.imrezol.trelloexporter.trello.service.TrelloApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class CardExporter {

    private static final Logger logger = LoggerFactory.getLogger(CardExporter.class);

    @Autowired
    private Properties properties;

    @Autowired
    private TrelloApi trelloApi;

    public void export(Card card) {
        logger.info("Exporting Card:{}", card.name);
    }
}
