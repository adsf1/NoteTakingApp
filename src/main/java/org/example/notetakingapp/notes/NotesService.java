package org.example.notetakingapp.notes;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotesService {

    private final NotesRepository notesRepository;

    public NotesService(NotesRepository notesRepository) {
        this.notesRepository = notesRepository;
    }

    public List<NoteDto> getNotes(){
        return notesRepository.findAll()
                .stream()
                .map(note -> new NoteDto(note.getId(), note.getTitle(), note.getDescription()))
                .toList();
    }
}
