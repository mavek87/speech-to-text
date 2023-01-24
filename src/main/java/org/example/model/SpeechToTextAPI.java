package org.example.model;

public interface SpeechToTextAPI {

    String transcribeText(String audioFile, String model, String language, Boolean verbose) throws TranscriptException;

}
