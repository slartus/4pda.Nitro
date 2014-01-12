package ru.pda.application.bricks;

import android.app.Fragment;

/**
 * Created by slartus on 12.01.14.
 */
public abstract class BrickInfo {
    private String name;
    private String title;

    public BrickInfo(String name,String title){
        this.name=name;
        this.title=title;
    }


    public String getName() {
        return name;
    }


    public String getTitle() {
        return title;
    }

    public abstract Fragment createFragment();
}
