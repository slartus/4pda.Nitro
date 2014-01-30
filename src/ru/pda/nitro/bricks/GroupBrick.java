package ru.pda.nitro.bricks;
import android.net.*;
import ru.pda.nitro.database.*;
import android.content.*;
import android.support.v4.app.*;
import ru.pda.nitro.topicsview.*;

public class GroupBrick extends BrickInfo
{
	public static final String NAME="group";
    public static final String TITLE="Группа";
	public static final Uri URI = Contract.Group.CONTENT_URI;
    public GroupBrick(SharedPreferences prefs) {
        super(NAME, TITLE, prefs.getBoolean("mainMenu_" + NAME, true), prefs.getString("mainFavorite_", NAME).equals(NAME)); }

    @Override
    public Fragment createFragment() {
        return new TopicView();
    }
}
