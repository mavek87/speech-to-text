package com.matteoveroni.speechtotext.model;

import com.matteoveroni.speechtotext.terminal.TerminalEmulator;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import java.io.IOException;

@Slf4j
public class ScriptSpeechToTextAPI implements SpeechToTextAPI {

    private static final String PYTHON_SYS_PATH = System.getProperty("user.home") + "/anaconda3/bin/python";
    private static final String PYTHON_SCRIPT_FOLDER = "scripts";
    private static final String SPEECH_TO_TEXT_SCRIPT = PYTHON_SCRIPT_FOLDER + "/whisper_speech_to_text.py";
    private static final String SPEECH_TO_TEXT_SCRIPT_PATH = ScriptSpeechToTextAPI.class.getClassLoader().getResource(SPEECH_TO_TEXT_SCRIPT).getFile();

    private final TerminalEmulator terminalEmulator;
    private final String audioFile;

    public ScriptSpeechToTextAPI(TerminalEmulator terminalEmulator, String audioFile) {
        this.terminalEmulator = terminalEmulator;
        this.audioFile = audioFile;
    }

    @Builder(buildMethodName = "transcribeText")
    public String transcribeText(String audioFile, String model, String language, Boolean verbose) throws TranscriptException {
        try {
            if (audioFile == null) {
                audioFile = this.audioFile;
            }
            if (model == null || model.trim().isBlank()) {
                model = "base";
            }
            if (language == null || language.trim().isBlank()) {
                language = "italian";
            }
            String verbosity;
            if (verbose == null) {
                verbosity = "False";
            } else {
                verbosity = verbose ? "True" : "False";
            }

            TerminalEmulator.CmdInput cmdInput = new TerminalEmulator.CmdInput(
                    PYTHON_SYS_PATH,
                    SPEECH_TO_TEXT_SCRIPT_PATH,
                    model,
                    audioFile,
                    language,
                    verbosity
            );
            TerminalEmulator.CmdOutput cmdOut = terminalEmulator.execute(cmdInput);

            if (!cmdOut.errors().isEmpty()) {
                log.warn("There are some errors from the command-line");
            }

            log.info("cmdOut inputs: " + cmdOut.inputs());
            log.info("cmdOut errors: " + cmdOut.errors());

            return cmdOut.inputs().get(0);
        } catch (IOException e) {
            log.error("IOException during transcription", e);
            throw new TranscriptException();
        }
    }
}
