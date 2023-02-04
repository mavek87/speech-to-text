package com.matteoveroni.speechtotext.di;

import com.matteoveroni.speechtotext.terminal.TerminalEmulator;
import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;

@Module
public class ServicesModule {

    @Singleton
    @Provides
    public static TerminalEmulator providesTerminalEmulator() {
        return new TerminalEmulator();
    }
}
