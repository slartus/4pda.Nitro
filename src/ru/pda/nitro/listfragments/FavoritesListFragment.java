package ru.pda.nitro.listfragments;

import android.app.ActionBar;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;

import ru.forpda.api.TopicsApi;
import ru.forpda.http.HttpHelper;
import ru.forpda.interfaces.ListInfo;
import ru.forpda.interfaces.forum.Topic;
import ru.pda.nitro.App;
import ru.pda.nitro.R;
import ru.pda.nitro.adapters.TopicListAdapter;
import ru.pda.nitro.bricks.FavoritesBrick;
import ru.pda.nitro.database.Contract;
import android.util.*;
import android.net.*;
import uk.co.senab.actionbarpulltorefresh.library.*;
import android.view.View.*;
import ru.pda.nitro.*;
import android.widget.*;


/**
 * Created by slartus on 12.01.14.
 */
public class FavoritesListFragment extends TopicsListFragment 
{

	@Override
	public void onScrollStateChanged(AbsListView p1, int p2)
	{
		// TODO: Implement this method
	}
	

	@Override
    public ArrayList<Topic> getTopicsList(ListInfo listInfo) throws ParseException, IOException
	{
		super.getTopicsList(listInfo);
        return TopicsApi.getFavorites(new HttpHelper(App.getInstance()), listInfo);
    }

    public String getTitle()
	{
		super.getTitle();
        return FavoritesBrick.TITLE;
    }
	
	@Override
	public Uri getUri()
	{
		super.getUri();
		return FavoritesBrick.URI;
	}

    public String getName()
	{
		super.getName();
        return FavoritesBrick.NAME;
    }
	
}
