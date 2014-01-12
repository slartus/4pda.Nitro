package ru.pda.application;

import android.app.Application;

/**
 * Created by slartus on 12.01.14.
 */
public class App extends Application {
    private static App INSTANCE = null;

    public App() {
        INSTANCE = this;
    }

    public static App getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new App();

        }

        return INSTANCE;
    }
}
