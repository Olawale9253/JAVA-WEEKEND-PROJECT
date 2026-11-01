package com.semicolon.studyvault.model;

import java.time.Instant;

public class NoteSummary {

    private final String title;
    private final long sizeInBytes;
    private final long lineCount;
    private final Instant lastModified;

    public NoteSummary(String title, long sizeInBytes,
                       long lineCount, Instant lastModified) {
        this.title = title;
        this.sizeInBytes = sizeInBytes;
        this.lineCount = lineCount;
        this.lastModified = lastModified;
    }

    public String getTitle() {
        return title;
    }

    public long getSizeInBytes() {
        return sizeInBytes;
    }

    public long getLineCount() {
        return lineCount;
    }

    public Instant getLastModified() {
        return lastModified;
    }
}