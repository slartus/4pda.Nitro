package ru.pda.nitro.listfragments;

import android.net.*;
import android.widget.*;
import java.io.*;
import java.text.*;
import java.util.*;
import ru.forpda.api.*;
import ru.forpda.http.*;
import ru.forpda.interfaces.*;
import ru.forpda.interfaces.forum.*;
import ru.pda.nitro.*;
import ru.pda.nitro.bricks.*;

import java.text.ParseException;


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
        return TopicsApi.getFavorites(new HttpHelper(App.getInstance()), listInfo);
    }

    public String getTitle()
	{
        return FavoritesBrick.TITLE;
    }
	
	@Override
	public Uri getUri()
	{
		return FavoritesBrick.URI;
	}

    public String getName()
	{
        return FavoritesBrick.NAME;
    }
	
}
