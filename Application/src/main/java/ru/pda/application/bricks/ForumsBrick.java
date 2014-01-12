package ru.pda.application.bricks;

import android.app.Fragment;

import ru.pda.application.listfragments.FavoritesListFragment;
import ru.pda.application.listfragments.ForumsListFragment;

/**
 * Created by slartus on 12.01.14.
 */
public class ForumsBrick extends BrickInfo {
    public static final String NAME="forums";
    public static final String TITLE="Форумы";
    public ForumsBrick() {
        super(NAME, TITLE);
    }

    @Override
    public Fragment createFragment() {
        return new ForumsListFragment();
    }
}
