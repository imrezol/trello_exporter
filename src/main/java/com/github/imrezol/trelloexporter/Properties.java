package com.github.imrezol.trelloexporter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class Properties {

    public static final ZonedDateTime exportDate = ZonedDateTime.now();

    public static final String baseDir = String.format(
            "exports/%s",
            DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"
            ).format(exportDate));

}
