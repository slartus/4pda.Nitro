package ru.pda.nitro.bricks;

import android.support.v4.app.Fragment;

import ru.pda.nitro.listfragments.ForumsListFragment;
import android.content.*;
import android.net.*;


/**
 * Created by slartus on 12.01.14.
 */
public class ForumsBrick extends BrickInfo
{


    public static final String NAME="forums";
    public static final String TITLE="Форумы";
	public static final Uri URI = null;
	public ForumsBrick(SharedPreferences prefs) {
		super(NAME, TITLE, prefs.getBoolean("mainMenu_" + NAME, true), prefs.getString("mainFavorite_", "favorites").equals(NAME));   
		}

    @Override
    public Fragment createFragment() {
        return new ForumsListFragment();
    }
	
	public Class<? extends Fragment> getClassList(){
		return ForumsListFragment.class;
	}
}
