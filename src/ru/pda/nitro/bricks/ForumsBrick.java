package ru.pda.nitro.bricks;

import android.app.Fragment;

import ru.pda.nitro.listfragments.ForumsListFragment;
import android.content.*;


/**
 * Created by slartus on 12.01.14.
 */
public class ForumsBrick extends BrickInfo
{


    public static final String NAME="forums";
    public static final String TITLE="Форумы";
	public ForumsBrick(SharedPreferences prefs) {
		super(NAME, TITLE, prefs.getBoolean("mainMenu_" + NAME, true), prefs.getString("mainFavorite_", "favorites").equals(NAME));   
		}

    @Override
    public Fragment createFragment() {
        return new ForumsListFragment();
    }
}