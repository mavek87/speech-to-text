package org.example.handlers;

import io.javalin.http.Context;
import io.javalin.http.Handler;
import lombok.extern.slf4j.Slf4j;
import org.example.model.ScriptSpeechToTextAPI;
import org.example.model.TranscriptException;
import org.example.terminal.TerminalEmulator;
import org.jetbrains.annotations.NotNull;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Slf4j
public class SpeechToTextApiResource implements Handler {

    private final TerminalEmulator terminalEmulator;

    public SpeechToTextApiResource(TerminalEmulator terminalEmulator) {
        this.terminalEmulator = terminalEmulator;
    }

    @Override
    public void handle(@NotNull Context ctx) throws Exception {
        byte[] fileBytes = ctx.bodyAsBytes();
        String audioFileId = UUID.randomUUID().toString();
        Path audioFilePath = Paths.get(audioFileId);
        try {
            Files.write(audioFilePath, fileBytes);
            String text = new ScriptSpeechToTextAPI(terminalEmulator, audioFileId)
                    .builder()
                    .model("small")
                    .language("italian")
                    .verbose(false)
                    .transcribeText();
            ctx.status(200);
            ctx.json(text);
        } catch (IOException ioe) {
            log.error("{} trying to create the file", IOException.class.getSimpleName(), ioe);
            ctx.status(500);
            ctx.json("Unexpected server error! Try again later...");
        } catch (TranscriptException tre) {
            log.error("{} trying to create the file", TranscriptException.class.getSimpleName(), tre);
            ctx.status(500);
            ctx.json("Upload error!");
        } finally {
            deleteFile(audioFilePath);
        }
    }

    private void deleteFile(Path filePath) {
        new Thread(() -> {
            boolean isFileDeleted = false;
            try {
                isFileDeleted = filePath.toFile().delete();
            } finally {
                if (!isFileDeleted) {
                    log.warn("File {} cannot be deleted", filePath.toAbsolutePath());
                }
            }
        }).start();
    }
}
