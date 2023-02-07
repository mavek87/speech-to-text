package com.matteoveroni.speechtotext.audio;

import lombok.extern.slf4j.Slf4j;
import javax.sound.sampled.*;
import java.io.*;
import java.util.UUID;

@Slf4j
public class AudioRecorder {
    private final AudioFileFormat.Type fileType = AudioFileFormat.Type.WAVE;
    private final File audioFile = new File("audio-" + UUID.randomUUID() + ".wav");
    private final AudioFormat format = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 44100, 16, 2, 4, 44100, false);
    private TargetDataLine line;

    public String getAudioFileName() {
        return audioFile.getName();
    }

    public void start() {
        try {
            DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);

            if (!AudioSystem.isLineSupported(info)) {
                log.info("Line not supported");
                System.exit(0);
            }
            line = (TargetDataLine) AudioSystem.getLine(info);
            line.open(format);
            line.start();
            log.info("Start recording...");

            AudioInputStream ais = new AudioInputStream(line);
            AudioSystem.write(ais, fileType, audioFile);
        } catch (LineUnavailableException | IOException ex) {
            ex.printStackTrace();
        }
    }

    public void finish() {
        line.stop();
        line.close();
        log.info("Finished");
    }
}