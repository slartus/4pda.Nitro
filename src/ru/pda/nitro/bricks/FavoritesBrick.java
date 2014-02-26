package ru.pda.nitro.bricks;

import android.support.v4.app.Fragment;

import ru.pda.nitro.listfragments.FavoritesListFragment;
import android.content.*;
import android.net.*;
import ru.pda.nitro.database.*;
import ru.pda.nitro.listfragments.*;


/**
 * Created by slartus on 12.01.14.
 */
public class FavoritesBrick extends BrickInfo
{



    public static final String NAME="favorites";
    public static final String TITLE="Избранное";
	public static final Uri URI = Contract.Favorite.CONTENT_URI;
    public FavoritesBrick(SharedPreferences prefs) {
        super(NAME, TITLE, prefs.getBoolean("mainMenu_" + NAME, true), prefs.getString("mainFavorite_", NAME).equals(NAME)); }

    @Override
    public Fragment createFragment() {
        return new FavoritesListFragment();
    }
	
	public Class<? extends Fragment> getClassList(){
		return FavoritesListFragment.class;
	}
}
