package org.example.notetakingapp.notes;

import org.example.notetakingapp.errorhandling.exceptions.IncorrectSearchParametersException;
import org.example.notetakingapp.errorhandling.exceptions.MissingIdException;
import org.example.notetakingapp.errorhandling.exceptions.NoTitleException;
import org.example.notetakingapp.errorhandling.exceptions.NoteNotFoundException;
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
    @ResponseStatus(HttpStatus.OK)
    public List<NoteWithIdDto> getNotes(@RequestParam(name = "id", required = false) String id,
                                        @RequestParam(name = "title", required = false) String title)
            throws IncorrectSearchParametersException, NoteNotFoundException {
        return notesService.getNotes(id, title);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public NoteWithIdDto createNote(@RequestBody BaseNoteDto baseNoteDto) throws NoTitleException {
        return notesService.createNote(baseNoteDto);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public NoteWithIdDto updateNote(@PathVariable("id") String id, @RequestBody BaseNoteDto baseNoteDto)
            throws MissingIdException, NoteNotFoundException {
        return notesService.updateNote(id, baseNoteDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteNote(@PathVariable("id") String id) throws MissingIdException, NoteNotFoundException {
        notesService.deleteNote(id);
    }
}
