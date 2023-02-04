package com.matteoveroni.speechtotext.model;

public class TranscriptException extends Exception{

    public TranscriptException() {
        super("An error occurred during transcription");
    }
}
