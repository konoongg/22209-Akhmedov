package org.example.exceptions;

public class EmptyConfig extends Exception{
    public EmptyConfig (String message) {
        super(message);
    }
}