package ru.pda.nitro.listfragments;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;

import ru.forpda.http.HttpHelper;
import ru.forpda.interfaces.forum.IListItem;
import ru.forpda.interfaces.forum.News;
import ru.forpda.interfaces.forum.NewsList;
import ru.pda.nitro.App;
import ru.pda.nitro.R;
import ru.pda.nitro.adapters.NewsListAdapter;
import ru.pda.nitro.bricks.NewsBrick;
import ru.pda.nitro.database.Contract;
import android.content.*;
import android.preference.*;
import android.util.*;
import android.net.*;
import uk.co.senab.actionbarpulltorefresh.library.*;
import ru.pda.nitro.database.*;


/**
 * Created by slartus on 12.01.14.
 */
public class NewsListFragment extends BaseListFragment implements FragmentLifecycle
{
	private int position = 0;
    private NewsList newsList = null;
    private String newsUrl = "";
    private NewsListAdapter adapter;

    @Override
    public ArrayList<? extends IListItem> getList()
	{
        return null;
    }

	@Override
	public Uri getUri()
	{
		return NewsBrick.URI;
	}

    @Override
    public String getTitle()
	{
        return NewsBrick.TITLE;
    }

    @Override
    public String getName()
	{
        return NewsBrick.NAME;
    }

    public static NewsListFragment newInstance(String url, int position)
	{
        final NewsListFragment f = new NewsListFragment();

        Bundle args = new Bundle();
        args.putString("_url", url);
		args.putInt("_position", position);
        f.setArguments(args);

        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
        View v = inflater.inflate(R.layout.list_topic, container, false);
        return initialiseListUi(v);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
	{
        super.onActivityCreated(savedInstanceState);
		newsUrl = getArguments().getString("_url");
		position = getArguments().getInt("_position");
		newsList = new NewsList(new HttpHelper(App.getInstance()), newsUrl);
		adapter = new NewsListAdapter(getActivity(), newsList, imageLoader);
        listView.addFooterView(initialiseFooter());
        listView.setAdapter(adapter);
        listView.setOnScrollListener(this);
		if (position == 0){
			getPullToRefreshAttacher(listView);
			getData();
		}
    }


	@Override
	public void onResumeFragment()
	{
		getPullToRefreshAttacher(listView);
		if (newsList != null && newsList.size() == 0)
			getData();
	}

    @Override
    public boolean inBackground()
	{
        try
		{

			if (!isRefresh() && !isLoadmore() && getLocalData())
			{
				setLoading(false);				
				return true;
			}
			else if (isRefresh() && !isLoadmore())
			{
				newsList = new NewsList(new HttpHelper(App.getInstance()), newsUrl);
			}
			else if (isLoadmore())
			{
				newsList.loadNextNewsPage();
				return true;
			}
			
			newsList.loadNextNewsPage();
			setLocalData(newsList);
			return true;

        }
		catch (ParseException e)
		{
        }
		catch (IOException e)
		{
        }

        return false;
    }

    @Override
    public void inExecute()
	{
        adapter.setData(newsList);
        adapter.notifyDataSetChanged();
    }

    public void getData()
	{
        if (!isLoading())
		{
            task = new Task();
            task.execute();
        }
		else
            setProgress(false);
    }

	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount)
	{
		if ((firstVisibleItem + visibleItemCount) == totalItemCount && !loadMore && !isLoading())
		{
			showFooter(true);
		}

    }


    @Override
    public void onScrollStateChanged(AbsListView p1, int p2)
	{

    }


    public boolean getLocalData()
	{
        if (newsUrl.equals(""))
		{
			Cursor cursor = getActivity().getContentResolver().query(Contract.News.CONTENT_URI, null, null, null, Contract.Favorite.DEFAULT_SORT_ORDER);
			if(LocalDataHelper.getLocalNews(cursor) != null){
			for(News item : LocalDataHelper.news){
				newsList.add(item);
			}
			
			cursor.close();
			return true;
			}else{
				cursor.close();
				return false;
			}
			
			
		}
        return false;
    }

    public void setLocalData(NewsList topics)
	{

		if (newsUrl.equals(""))
		{
			deleteAllLocalData(getActivity(),getUri());
			ContentValues cv = new ContentValues();
			for(News topic : newsList){
			cv.put(Contract.News.description, topic.getDescription().toString());
			cv.put(Contract.News.title, topic.getTitle().toString());
			cv.put(Contract.News.id, topic.getId().toString());
			cv.put(Contract.News.author, topic.getAuthor().toString());
			cv.put(Contract.News.newsDate, topic.getNewsDate().toString());
			cv.put(Contract.News.imgUrl, topic.getImgUrl().toString());
			
			cv.put(Contract.News.commentsCount, topic.getCommentsCount());
			
			if(topic.getSourceTitle() != null){
			cv.put(Contract.News.sourceTitle, topic.getSourceTitle().toString());
			cv.put(Contract.News.sourseUrl, topic.getSourceUrl().toString());
			}
			if(topic.getTagLink()!= null){
			cv.put(Contract.News.tagLink, topic.getTagLink().toString());
			cv.put(Contract.News.tagName, topic.getTagName().toString());
			cv.put(Contract.News.tagTitle, topic.getTagTitle().toString());
			}
			getActivity().getContentResolver().insert(getUri(), cv);
			}
		}
    }

}
