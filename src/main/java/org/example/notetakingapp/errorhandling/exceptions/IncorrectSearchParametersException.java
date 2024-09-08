package org.example.notetakingapp.errorhandling.exceptions;

public class IncorrectSearchParametersException extends Exception {
    public IncorrectSearchParametersException() {
        super("Simultaneous search with id and title isn't supported!");
    }
}
