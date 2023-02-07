package com.matteoveroni.speechtotext.audio;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class SoundRecorder {
    private static final long DEFAULT_UNSET_RECORD_TIME = 10000;
    private final long defaultRecordTime;

    private final List<SoundRecorderSubscriber> subscribers = new ArrayList<>();
    private AudioRecorder recordAudio;

    public SoundRecorder() {
        this.defaultRecordTime = DEFAULT_UNSET_RECORD_TIME;
    }

    public SoundRecorder(int defaultRecordTime) {
        this.defaultRecordTime = defaultRecordTime;
    }

    public void addSubscriber(SoundRecorderSubscriber subscriber) {
        subscribers.add(subscriber);
    }

    public void addSubscribers(SoundRecorderSubscriber... subscriber) {
        subscribers.addAll(List.of(subscriber));
    }

    public void startRecord() {
        recordAudio = new AudioRecorder();
        recordAudio.start();
    }

    public void finishRecord() {
        if (recordAudio != null) {
            recordAudio.finish();
            notifyListeners(recordAudio);
        }
    }

    public Future<Boolean> startRecord(long recordTime) {
        recordAudio = new AudioRecorder();

        CompletableFuture<Boolean> futureResult = new CompletableFuture<>();

        // creates a new thread that waits for a specified
        // of time before stopping
        Thread stopperThread = new Thread(() -> {
            try {
                Thread.sleep(recordTime);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
                futureResult.isCompletedExceptionally();
            }
            finishRecord();
            futureResult.complete(true);
        });

        stopperThread.start();
        recordAudio.start();
        return futureResult;
    }

    public void recordNTimesForRecordTime(int nTimes, long recordTime) throws ExecutionException, InterruptedException {
        for (int i = 0; i < nTimes; i++) {
            Future<Boolean> future = startRecord(recordTime);
            future.get();
        }
    }

    public void startRecordForever() {
        while (true) {
            startRecord(defaultRecordTime);
        }
    }

    @Deprecated
    // TODO: da sistemare
    public void startRecordUntilIsDoneOrCancelled(Future<Boolean> future) {
        while (!future.isDone() || !future.isCancelled()) {
            startRecord(defaultRecordTime);
        }
    }

    private void notifyListeners(AudioRecorder recordAudio) {
        final String recordedAudioFileName = recordAudio.getAudioFileName();
        subscribers.forEach(subscriber -> subscriber.notifyRecordedAudioFileName(recordedAudioFileName));
    }
}