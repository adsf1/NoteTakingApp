package org.example.notetakingapp.notes;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface NotesRepository extends MongoRepository<Note, String> {
    List<Note> findByTitle(String title);
}
