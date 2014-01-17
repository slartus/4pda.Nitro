package ru.pda.nitro.bricks;

import android.support.v4.app.Fragment;

import ru.pda.nitro.listfragments.FavoritesListFragment;
import android.content.*;


/**
 * Created by slartus on 12.01.14.
 */
public class FavoritesBrick extends BrickInfo
{



    public static final String NAME="favorites";
    public static final String TITLE="Избранное";
    public FavoritesBrick(SharedPreferences prefs) {
        super(NAME, TITLE, prefs.getBoolean("mainMenu_" + NAME, true), prefs.getString("mainFavorite_", NAME).equals(NAME)); }

    @Override
    public Fragment createFragment() {
        return new FavoritesListFragment();
    }
}
