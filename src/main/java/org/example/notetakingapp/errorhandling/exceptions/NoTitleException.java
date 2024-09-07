package org.example.notetakingapp.errorhandling.exceptions;

public class NoTitleException extends Exception {
    public NoTitleException() {
        super("Note title is mandatory!");
    }
}
