package org.example.notetakingapp.notes.dto;

import lombok.Getter;

@Getter
public class NoteWithIdDto extends BaseNoteDto {

    private final String id;

    public NoteWithIdDto(String id, String title, String description) {
        super(title, description);
        this.id = id;
    }
}
