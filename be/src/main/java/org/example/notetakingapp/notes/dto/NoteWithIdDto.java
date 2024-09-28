package org.example.notetakingapp.notes.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class NoteWithIdDto extends BaseNoteDto {

    private String id;

    public NoteWithIdDto(String id, String title, String description) {
        super(title, description);
        this.id = id;
    }
}
