package org.example.notetakingapp.notes;

import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("notes")
@Getter
public class Note {

    @Id
    private String id;
    private String title;
    private String description;

    public Note(String id, String title, String description){
        this.id = id;
        this.title = title;
        this.description = description;
    }
}
