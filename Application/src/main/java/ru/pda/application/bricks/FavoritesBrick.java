package ru.pda.application.bricks;

import android.app.Fragment;

import ru.pda.application.listfragments.FavoritesListFragment;

/**
 * Created by slartus on 12.01.14.
 */
public class FavoritesBrick extends BrickInfo {
    public static final String NAME="favorites";
    public static final String TITLE="Избранное";
    public FavoritesBrick() {
        super(NAME, TITLE);
    }

    @Override
    public Fragment createFragment() {
        return new FavoritesListFragment();
    }
}
