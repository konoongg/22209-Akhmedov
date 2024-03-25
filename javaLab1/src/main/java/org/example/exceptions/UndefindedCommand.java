package org.example.exceptions;

public class UndefindedCommand extends Exception{
    public UndefindedCommand (String message) {
        super(message);
    }
}
