package com.matteoveroni.speechtotext.di;

import dagger.Component;
import com.matteoveroni.speechtotext.App;
import javax.inject.Singleton;

@Singleton
@Component(modules =
        {
                ServerModule.class,
                JsonModule.class,
                ServicesModule.class
        }
)
public interface AppComponent {

    public App buildApp();
}
