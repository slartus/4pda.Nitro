package ru.pda.nitro.listfragments;

import android.net.*;
import android.widget.*;
import android.widget.AbsListView.*;
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

public class SubscribesListFragment extends TopicsListFragment implements OnScrollListener
{

	@Override
	public void onScrollStateChanged(AbsListView p1, int p2)
	{

    }


	@Override
    public ArrayList<Topic> getTopicsList(ListInfo listInfo) throws ParseException, IOException
	{
        return TopicsApi.getSubscribes(new HttpHelper(App.getInstance()), listInfo);
    }

    public String getTitle()
	{
        return SubscribesBrick.TITLE;
    }

	@Override
	public Uri getUri()
	{
		return SubscribesBrick.URI;
	}

    public String getName()
	{
        return SubscribesBrick.NAME;
    }

}
