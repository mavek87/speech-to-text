package org.example.terminal;

import lombok.extern.slf4j.Slf4j;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class TerminalEmulator {

    public record CmdInput(String... command) {
    }

    public record CmdOutput(List<String> inputs, List<String> errors) {
    }

    public CmdOutput execute(CmdInput cmdInput) throws IOException {
        ProcessBuilder pb = new ProcessBuilder(cmdInput.command());
        Process process = pb.start();
        BufferedReader output = new BufferedReader(new InputStreamReader(process.getInputStream()));
        BufferedReader error = new BufferedReader(new InputStreamReader(process.getErrorStream()));

        List<String> lines = new ArrayList<>();
        String line;
        while ((line = output.readLine()) != null) {
            lines.add(line);
            log.debug("line: {}", lines);
        }

        List<String> errors = new ArrayList<>();
        String err;
        while ((err = error.readLine()) != null) {
            lines.add(err);
            log.warn("error: {}", err);
        }

        return new CmdOutput(lines, errors);
    }
}

//        Process process = new ProcessBuilder()
//                .command(
////                        "/home/mavek/anaconda3/envs/speech-to-text/bin/python",
//                        "/home/mavek/anaconda3/bin/python",
//                        "/home/mavek/Documenti/workspace/java/api-speech-to-text/src/main/java/org/example/resources/whisper_example.py",
//                        "/home/mavek/Documenti/workspace/java/api-speech-to-text/src/main/java/org/example/resources/audio.wav"
//                )
////                .directory(workingDirectory)
//                .inheritIO()
//                .start();

//        Writer writer = new OutputStreamWriter(System.out);
//        StreamHandler stdout_handler = new StreamHandler(process.getInputStream(), writer);
//        stdout_handler.start();

//        terminalEmulator.terminalOutput(new TerminalEmulator.TerminalInput(
//                        "/home/mavek/anaconda3/bin/python",
//                        "/home/mavek/Documenti/workspace/java/api-speech-to-text/src/main/java/org/example/resources/whisper_example.py",
//                        filename
//                )
//        );
