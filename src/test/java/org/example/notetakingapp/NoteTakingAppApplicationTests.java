package org.example.notetakingapp;

import jakarta.annotation.PostConstruct;
import org.example.notetakingapp.notes.dto.BaseNoteDto;
import org.example.notetakingapp.notes.dto.NoteWithIdDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class NoteTakingAppApplicationTests {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private String baseUrl;

    @PostConstruct
    void setup(){
        baseUrl = "http://localhost:" + port + "/notes";
    }

    @Test
    void createNote_noRequestBody_returnsError(){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<String>(null, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(baseUrl, request, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void createNote_emptyRequestBody_returnsError(){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<String>("{}", headers);

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
}
