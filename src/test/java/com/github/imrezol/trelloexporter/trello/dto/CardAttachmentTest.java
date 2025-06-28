package com.github.imrezol.trelloexporter.trello.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CardAttachmentTest {

    @Test
    void testFieldsAndGetLocalFilename() {
        CardAttachment attachment = new CardAttachment();
        attachment.id = "123abc";

        attachment.fileName = "screenshot.png";

        String expectedLocalFilename = "screenshot_123abc.png";
        assertEquals(expectedLocalFilename, attachment.getLocalFilename());
    }


    @Test
    void testGetLocalFilenameWithNoExtension() {
        CardAttachment attachment = new CardAttachment();
        attachment.id = "id456";
        attachment.fileName = "noextension";

        String expected = "noextension_id456";
        assertEquals(expected, attachment.getLocalFilename());
    }

    @Test
    void testGetLocalFilenameWithDuplicatedDots() {
        CardAttachment attachment = new CardAttachment();
        attachment.id = "id789";
        attachment.fileName = "archive..gz";

        String expected = "archive._id789.gz";
        assertEquals(expected, attachment.getLocalFilename());
    }

    @Test
    void testGetLocalFilenameWithMultipleDots() {
        CardAttachment attachment = new CardAttachment();
        attachment.id = "id789";
        attachment.fileName = "archive.tar.gz";

        String expected = "archive.tar_id789.gz";
        assertEquals(expected, attachment.getLocalFilename());
    }

}
