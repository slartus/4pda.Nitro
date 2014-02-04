package ru.pda.nitro.bricks;
import android.net.*;
import ru.pda.nitro.database.*;
import android.content.*;
import android.support.v4.app.*;
import ru.pda.nitro.topicsview.*;
import ru.pda.nitro.listfragments.*;

public class GroopsBrick extends BrickInfo
{
	public static final String NAME="groops";
    public static final String TITLE="Группы";
	public static final Uri URI = Contract.Groops.CONTENT_URI;
    public GroopsBrick(SharedPreferences prefs) {
        super(NAME, TITLE, prefs.getBoolean("mainMenu_" + NAME, true), prefs.getString("mainFavorite_", "favorites").equals(NAME)); }

    @Override
    public Fragment createFragment() {
        return new GroopsListFragment();
    }
}
