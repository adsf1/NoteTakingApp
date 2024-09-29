package org.example.notetakingapp;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.example.notetakingapp.notes.Note;
import org.example.notetakingapp.notes.NotesRepository;
import org.example.notetakingapp.notes.dto.BaseNoteDto;
import org.example.notetakingapp.notes.dto.NoteWithIdDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.web.util.UriUtils;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class NoteTakingAppApplicationTests {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private NotesRepository notesRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private String baseUrl;

    @PostConstruct
    void setup(){
        baseUrl = "http://localhost:" + port + "/notes";
    }

    @AfterEach
    void teardown(){
        notesRepository.deleteAll();
    }

    @Test
    void getNotes_emptyDatabase_returnsEmptyResponse(){
        assertThat(notesRepository.count()).isEqualTo(0);

        ResponseEntity<String> response = restTemplate.getForEntity(baseUrl, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).isEqualTo("[]");
    }

    @Test
    void getNotes_databaseWithOneRecord_returnsItem() throws JsonProcessingException {
        Note newNote = addNoteToDatabse();

        ResponseEntity<String> response = restTemplate.getForEntity(baseUrl, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();

        List<NoteWithIdDto> notes = objectMapper.readValue(response.getBody(), new TypeReference<>() {});
        assertThat(notes.size()).isEqualTo(1);

        NoteWithIdDto note = notes.get(0);
        assertThat(note.getId()).isEqualTo(newNote.getId());
        assertThat(note.getTitle()).isEqualTo(newNote.getTitle());
        assertThat(note.getDescription()).isEqualTo(newNote.getDescription());
    }

    @Test
    void getNotes_idAndTitleCriteria_returnsError() {
        assertThat(notesRepository.count()).isEqualTo(0);

        String url = UriComponentsBuilder
                .fromHttpUrl(baseUrl)
                .queryParam("id", "123")
                .queryParam("title", "Title")
                .toUriString();

        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isEqualTo("Simultaneous search with id and title isn't supported!");
    }

    @Test
    void getNotes_idCriteriaAndEmptyDatabase_returnsEmptyResponse() {
        assertThat(notesRepository.count()).isEqualTo(0);

        String url = UriComponentsBuilder
                .fromHttpUrl(baseUrl)
                .queryParam("id", "123")
                .toUriString();

        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isEqualTo("Note not found!");
    }

    @Test
    void getNotes_idCriteria_returnsMatchingItem() throws JsonProcessingException {
        Note newNote = addNoteToDatabse();

        String url = UriComponentsBuilder
                .fromHttpUrl(baseUrl)
                .queryParam("id", newNote.getId())
                .toUriString();

        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();

        List<NoteWithIdDto> notes = objectMapper.readValue(response.getBody(), new TypeReference<>() {});
        assertThat(notes.size()).isEqualTo(1);

        NoteWithIdDto note = notes.get(0);
        assertThat(note.getId()).isEqualTo(newNote.getId());
        assertThat(note.getTitle()).isEqualTo(newNote.getTitle());
        assertThat(note.getDescription()).isEqualTo(newNote.getDescription());
    }

    @Test
    void getNotes_titleCriteriaAndEmptyDatabase_returnsEmptyResponse() {
        assertThat(notesRepository.count()).isEqualTo(0);

        String url = UriComponentsBuilder
                .fromHttpUrl(baseUrl)
                .queryParam("title", "Title")
                .toUriString();

        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isEqualTo("Note not found!");
    }

    @Test
    void getNotes_titleCriteria_returnsMatchingItem() throws JsonProcessingException {
        Note newNote = addNoteToDatabse();

        String url = UriComponentsBuilder
                .fromHttpUrl(baseUrl)
                .queryParam("title", newNote.getTitle())
                .toUriString();

        String formattedUrl = UriUtils.decode(url, "UTF-8");

        ResponseEntity<String> response = restTemplate.getForEntity(formattedUrl, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();

        List<NoteWithIdDto> notes = objectMapper.readValue(response.getBody(), new TypeReference<>() {});
        assertThat(notes.size()).isEqualTo(1);

        NoteWithIdDto note = notes.get(0);
        assertThat(note.getId()).isEqualTo(newNote.getId());
        assertThat(note.getTitle()).isEqualTo(newNote.getTitle());
        assertThat(note.getDescription()).isEqualTo(newNote.getDescription());
    }

    @Test
    void createNote_noRequestBody_returnsError(){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(null, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(baseUrl, request, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void createNote_emptyRequestBody_returnsError(){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>("{}", headers);

        ResponseEntity<String> response = restTemplate.exchange(baseUrl, HttpMethod.POST, request, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isEqualTo("Note title is mandatory!");
    }

    @Test
    void createNote_requestBodyWithoutTitle_returnsError(){
        BaseNoteDto note = new BaseNoteDto("", "Note description");

        ResponseEntity<String> response = restTemplate.postForEntity(baseUrl, note, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isEqualTo("Note title is mandatory!");
    }

    @Test
    void createNote_requestBodyWithoutDescription_createsAndReturnsNote(){
        BaseNoteDto note = new BaseNoteDto("Note title", "");

        ResponseEntity<NoteWithIdDto> response = restTemplate.postForEntity(baseUrl, note, NoteWithIdDto.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isNotNull();
        assertThat(response.getBody().getTitle()).isEqualTo("Note title");
        assertThat(response.getBody().getDescription()).isEqualTo("");
    }

    @Test
    void createNote_validRequestBody_createsAndReturnsNote(){
        BaseNoteDto note = new BaseNoteDto("Note title", "Note description");

        ResponseEntity<NoteWithIdDto> response = restTemplate.postForEntity(baseUrl, note, NoteWithIdDto.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isNotNull();
        assertThat(response.getBody().getTitle()).isEqualTo("Note title");
        assertThat(response.getBody().getDescription()).isEqualTo("Note description");
    }

    @Test
    void updateNote_noteDoesntExist_returnsError(){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>("{}", headers);

        ResponseEntity<String> response = restTemplate.exchange(baseUrl + "/0", HttpMethod.PUT, request, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isEqualTo("Note not found!");
    }

    @Test
    void updateNote_blankTitle_doesntUpdateTitle(){
        Note newNote = addNoteToDatabase();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        BaseNoteDto note = new BaseNoteDto("", "New Note description");
        HttpEntity<BaseNoteDto> request = new HttpEntity<>(note, headers);

        ResponseEntity<NoteWithIdDto> response = restTemplate.exchange(baseUrl + "/" + newNote.getId(),
                HttpMethod.PUT, request, NoteWithIdDto.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isEqualTo(newNote.getId());
        assertThat(response.getBody().getTitle()).isEqualTo(newNote.getTitle());
        assertThat(response.getBody().getDescription()).isEqualTo(note.getDescription());
    }

    @Test
    void updateNote_validTitleAndDescription_updatesNote(){
        Note newNote = addNoteToDatabase();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        BaseNoteDto note = new BaseNoteDto("New Note Tile", "");
        HttpEntity<BaseNoteDto> request = new HttpEntity<>(note, headers);

        ResponseEntity<NoteWithIdDto> response = restTemplate.exchange(baseUrl + "/" + newNote.getId(),
                HttpMethod.PUT,

                request, NoteWithIdDto.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isEqualTo(newNote.getId());
        assertThat(response.getBody().getTitle()).isEqualTo(note.getTitle());
        assertThat(response.getBody().getDescription()).isEqualTo(note.getDescription());
    }

    @Test
    void deleteNote_noteDoesntExist_returnsError(){
        addNoteToDatabse();

        ResponseEntity<String> response = restTemplate.exchange(baseUrl + "/99", HttpMethod.DELETE, null, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isEqualTo("Note not found!");
    }

    @Test
    void deleteNote_validId_deletesNote(){
        Note newNote = addNoteToDatabse();

        ResponseEntity<String> response = restTemplate.exchange(baseUrl + "/" +  newNote.getId(), HttpMethod.DELETE, null, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    private Note addNoteToDatabse(){
        Note newNote = new Note(null, "Note title", "Note description");
        return notesRepository.save(newNote);
    }
}
