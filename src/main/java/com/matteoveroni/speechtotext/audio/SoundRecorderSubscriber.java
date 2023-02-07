package com.matteoveroni.speechtotext.audio;

public interface SoundRecorderSubscriber {
    void notifyRecordedAudioFileName(String audioFileName);
}
