package ru.pda.application.bricks;

import android.app.Fragment;

import ru.pda.application.listfragments.NewsListFragment;

/**
 * Created by slartus on 12.01.14.
 */
public class NewsBrick extends BrickInfo {
    public static final String NAME="news";
    public static final String TITLE="Новости";
    public NewsBrick() {
        super(NAME, TITLE);
    }

    @Override
    public Fragment createFragment() {
        return new NewsListFragment();
    }
}
