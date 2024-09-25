package org.example.notetakingapp.errorhandling.exceptions;

public class MissingIdException extends Exception {
    public MissingIdException() {
        super("Missing Note Id!");
    }
}
