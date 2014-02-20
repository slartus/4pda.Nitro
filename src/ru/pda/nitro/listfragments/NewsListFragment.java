package ru.pda.nitro.listfragments;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
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
import ru.pda.nitro.database.LocalDataHelper;
import ru.forpda.api.*;
import ru.forpda.interfaces.*;


/**
 * Created by slartus on 12.01.14.
 */
public class NewsListFragment extends BaseListFragment implements FragmentLifecycle
{
	private ArrayList<News> newsList = null;
	public final static String NEWS_LIST_FRAGMENT = "NEWS_LIST_FRAGMENT";
	private final static String NEWS_URL = "ru.pda.nitro.listfragments.NewsListFragment.NEWS_URL";
	private final static String NEWS_POSITION = "ru.pda.nitro.listfragments.NewsListFragment.NEWS_POSITION";
	private int position = 0;
//    private NewsList newsList = null;
    private String newsUrl = "";
    private NewsListAdapter adapter;

    @Override
    public ArrayList<? extends IListItem> getList()
	{
        try
		{
			return NewsApi.getNews(new HttpHelper(App.getInstance()), newsUrl, listInfo);
		}
		catch (ParseException e)
		{}
		catch (IOException e)
		{}
		return null;
    }
	
	
	@Override
	public String getClassName()
	{
		return NEWS_LIST_FRAGMENT;
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
        args.putString(NEWS_URL, url);
		args.putInt(NEWS_POSITION, position);
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

        newsUrl = getArguments().getString(NEWS_URL);

        position = getArguments().getInt(NEWS_POSITION);
		
		listInfo = new ListInfo();
		newsList = new ArrayList<News>();
     //   newsList = new NewsList(new HttpHelper(App.getInstance()), newsUrl);
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

			if (!isRefresh() && !isLoadmore() && getLocalNewsData())
			{
				setLoading(false);				
				return true;
			}
			else if (isRefresh() && !isLoadmore())
			{
				newsList = new ArrayList<News>();
				//	newsList = new NewsList(new HttpHelper(App.getInstance()), newsUrl);
			}
			else if (isLoadmore())
			{
				for(News item : (ArrayList<News>)getList()){
					newsList.add(item);
				}
			//	newsList.loadNextNewsPage();
				return true;
			}
			newsList = (ArrayList<News>) getList();
			
		//	newsList.loadNextNewsPage();
		if(newsList != null){
			setLocalData(getActivity(),newsList, getUri(), newsUrl);
			return true;
			}
			
		return false;
    }

    @Override
    public void inExecute()
	{
        setDataInAdapter(adapter, newsList);
        updateAdapter(adapter);
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
			if(newsList.size() > 0)
			showFooter(true);
		}

    }


    @Override
    public void onScrollStateChanged(AbsListView p1, int p2)
	{

    }

/*	@Override
	public void getLocalDataOnStart()
	{
		newsList.clear();
		localNews = getLocalNewsData();
		if(localNews){
			setDataInAdapter(adapter, (ArrayList<IListItem>)newsList);
			updateAdapter(adapter);
		}
	}*/

	

    public boolean getLocalNewsData()
	{
		
        if (newsUrl.equals(""))
		{
			Cursor cursor = null;
			try{
			cursor = getActivity().getContentResolver().query(Contract.News.CONTENT_URI, null, null, null, Contract.Favorite.DEFAULT_SORT_ORDER);
			for(News item : LocalDataHelper.getLocalNews(cursor)){
				newsList.add(item);
			}
			
			return true;
			
			} finally {
                if (cursor != null)
                    cursor.close();
            }
		}
        return false;
    }

    public static void setLocalData(Context context, ArrayList<News> news, Uri uri, String url)
	{

		if (url.equals(""))
		{
			deleteAllLocalData(context,uri);
			ContentValues cv = new ContentValues();
			for(News item : news){
			cv.put(Contract.News.description, item.getDescription() != null ? item.getDescription().toString() : "");
			cv.put(Contract.News.title,item.getTitle() != null ? item.getTitle().toString() : "");
			cv.put(Contract.News.id,item.getId() != null ? item.getId().toString() : "");
			cv.put(Contract.News.author, item.getAuthor() != null ? item.getAuthor().toString() : "");
			cv.put(Contract.News.newsDate,item.getNewsDate() != null ? item.getNewsDate().toString() : "");
			cv.put(Contract.News.imgUrl, item.getImgUrl() != null ? item.getImgUrl().toString() : "");
			
			cv.put(Contract.News.commentsCount, item.getCommentsCount());
			
			if(item.getSourceTitle() != null){
			cv.put(Contract.News.sourceTitle, item.getSourceTitle().toString());
			cv.put(Contract.News.sourseUrl, item.getSourceUrl().toString());
			}
			if(item.getTagLink()!= null){
			cv.put(Contract.News.tagLink, item.getTagLink().toString());
			cv.put(Contract.News.tagName, item.getTagName().toString());
			cv.put(Contract.News.tagTitle, item.getTagTitle().toString());
			}
			context.getContentResolver().insert(uri, cv);
			}
		}
    }

}
