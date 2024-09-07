package org.example.notetakingapp.notes;

import org.example.notetakingapp.errorhandling.exceptions.NoTitleException;
import org.example.notetakingapp.notes.dto.BaseNoteDto;
import org.example.notetakingapp.notes.dto.NoteWithIdDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotesService {

    private final NotesRepository notesRepository;

    public NotesService(NotesRepository notesRepository) {
        this.notesRepository = notesRepository;
    }

    public List<NoteWithIdDto> getNotes(){
        return notesRepository.findAll()
                .stream()
                .map(note -> new NoteWithIdDto(note.getId(), note.getTitle(), note.getDescription()))
                .toList();
    }

    public NoteWithIdDto createNote(BaseNoteDto baseNoteDto) throws NoTitleException {
        if(baseNoteDto.getTitle() == null || baseNoteDto.getTitle().isBlank()){
            throw new NoTitleException();
        }

        Note note = new Note(null, baseNoteDto.getTitle(), baseNoteDto.getDescription());
        Note savedNote = notesRepository.save(note);

        return new NoteWithIdDto(savedNote.getId(), savedNote.getTitle(), savedNote.getDescription());
    }
}
