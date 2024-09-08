package org.example.notetakingapp.errorhandling.exceptions;

public class NoteNotFoundException extends Exception {
    public NoteNotFoundException() {
        super("Note  not found!");
    }
}
