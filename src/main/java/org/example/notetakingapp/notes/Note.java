package org.example.notetakingapp.notes;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("notes")
@Getter
@Setter
public class Note {

    @Id
    private final String id;
    private String title;
    private String description;

    public Note(String id, String title, String description){
        this.id = id;
        this.title = title;
        this.description = description;
    }
}
