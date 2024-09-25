package org.example.notetakingapp.notes.dto;

import lombok.Getter;

@Getter
public class BaseNoteDto {

    private final String title;
    private final String description;

    public BaseNoteDto(String title, String description) {
        this.title = title;
        this.description = description;
    }
}
