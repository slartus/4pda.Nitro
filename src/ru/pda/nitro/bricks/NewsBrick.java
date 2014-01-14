package ru.pda.nitro.bricks;

import android.app.Fragment;

import ru.pda.nitro.listfragments.NewsListFragment;
import android.content.*;

/**
 * Created by slartus on 12.01.14.
 */
public class NewsBrick extends BrickInfo
{


    public static final String NAME="news";
    public static final String TITLE="Новости";
    public NewsBrick(SharedPreferences prefs) {
        super(NAME, TITLE, prefs.getBoolean("mainMenu_" + NAME, true), prefs.getString("mainFavorite_", "favorites").equals(NAME));
    }

    @Override
    public Fragment createFragment() {
        return new NewsListFragment();
    }
}
