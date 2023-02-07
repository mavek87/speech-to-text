package com.matteoveroni.speechtotext;

import com.matteoveroni.speechtotext.audio.SoundRecorder;
import com.matteoveroni.speechtotext.audio.SoundRecorderSubscriber;
import lombok.extern.slf4j.Slf4j;
import java.util.concurrent.Future;

@Slf4j
public class App2 {
    public static void main(String[] args) throws Exception {
        SoundRecorder soundRecorder = new SoundRecorder();
        SoundRecorderSubscriber s1 = audioFileName -> log.info("s1: audio file " + audioFileName);
        SoundRecorderSubscriber s2 = audioFileName -> log.info("s2: audio file " + audioFileName);
        SoundRecorderSubscriber s3 = audioFileName -> log.info("s3: audio file " + audioFileName);
        SoundRecorderSubscriber s4 = audioFileName -> log.info("s4: audio file " + audioFileName);
        soundRecorder.addSubscribers(s1, s2);
        soundRecorder.addSubscribers(s3);
        soundRecorder.addSubscriber(s4);

        // OK
        Future<Boolean> future = soundRecorder.startRecord(6000);
        future.get();

        // OK
//        soundRecorder.recordNTimesForRecordTime(3, 4000);

        // KO
//        CompletableFuture<Boolean> completableFuture = new CompletableFuture<>();
//        soundRecorder.startRecordUntilIsDoneOrCancelled(completableFuture);
//
//        Thread.sleep(5000);
//        System.out.println("completed");
//        completableFuture.complete(true);

//        Executors.newCachedThreadPool().submit(() -> {
//            Thread.sleep(2000);
//            completableFuture.complete(true);
//            return null;
//        });
    }
}