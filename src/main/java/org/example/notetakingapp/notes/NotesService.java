package org.example.notetakingapp.notes;

import org.example.notetakingapp.errorhandling.exceptions.IncorrectSearchParametersException;
import org.example.notetakingapp.errorhandling.exceptions.MissingIdException;
import org.example.notetakingapp.errorhandling.exceptions.NoTitleException;
import org.example.notetakingapp.errorhandling.exceptions.NoteNotFoundException;
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

    public List<NoteWithIdDto> getNotes(String id, String title) throws IncorrectSearchParametersException, NoteNotFoundException {
        if(id != null && title != null){
            throw new IncorrectSearchParametersException();
        }

        List<Note> result;
        if(id != null){
            Note note = notesRepository.findById(id).orElseThrow(NoteNotFoundException::new);
            result = List.of(note);
        } else if(title != null){
            System.out.println(title);
            result = notesRepository.findByTitle(title);
            if(result.isEmpty()){
                throw new NoteNotFoundException();
            }
        } else {
            result = notesRepository.findAll();
        }

        return result
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

    public NoteWithIdDto updateNote(String id, BaseNoteDto baseNoteDto) throws MissingIdException, NoteNotFoundException {
        if(id == null || id.isBlank()){
            throw new MissingIdException();
        }

        Note note = notesRepository.findById(id).orElseThrow(NoteNotFoundException::new);

        if(baseNoteDto.getTitle() != null && !baseNoteDto.getTitle().isBlank()){
            note.setTitle(baseNoteDto.getTitle());
        }
        if(baseNoteDto.getDescription() != null){
            note.setDescription(baseNoteDto.getDescription());
        }

        Note savedNote = notesRepository.save(note);

        return new NoteWithIdDto(savedNote.getId(), savedNote.getTitle(), savedNote.getDescription());
    }
}
