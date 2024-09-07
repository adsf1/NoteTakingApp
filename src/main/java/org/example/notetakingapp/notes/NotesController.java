package org.example.notetakingapp.notes;

import org.example.notetakingapp.errorhandling.exceptions.NoTitleException;
import org.example.notetakingapp.notes.dto.BaseNoteDto;
import org.example.notetakingapp.notes.dto.NoteWithIdDto;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notes")
public class NotesController {

    private final NotesService notesService;

    public NotesController(NotesService notesService) {
        this.notesService = notesService;
    }

    @GetMapping
    public List<NoteWithIdDto> getNotes(){
        return notesService.getNotes();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public NoteWithIdDto createNote(@RequestBody BaseNoteDto baseNoteDto) throws NoTitleException {
        return notesService.createNote(baseNoteDto);
    }
}
