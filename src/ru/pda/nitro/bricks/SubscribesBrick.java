package ru.pda.nitro.bricks;

import android.support.v4.app.Fragment;

import android.content.*;
import android.net.*;
import ru.pda.nitro.database.*;
import ru.pda.nitro.listfragments.SubscribesListFragment;
import ru.pda.nitro.listfragments.*;


/**
 * Created by slartus on 12.01.14.
 */
public class SubscribesBrick extends BrickInfo
{



    public static final String NAME="subscribes";
    public static final String TITLE="Подписки";
	public static final Uri URI = Contract.Subscribes.CONTENT_URI;
    public SubscribesBrick(SharedPreferences prefs) {
        super(NAME, TITLE, prefs.getBoolean("mainMenu_" + NAME, true), prefs.getString("mainFavorite_", "favorites").equals(NAME)); }

    @Override
    public Fragment createFragment() {
        return new SubscribesListFragment();
    }
	
	public Class<? extends Fragment> getClassList(){
		return SubscribesListFragment.class;
	}
}
