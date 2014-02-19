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


/**
 * Created by slartus on 12.01.14.
 */
public class NewsListFragment extends BaseListFragment implements FragmentLifecycle
{
	public final static String NEWS_LIST_FRAGMENT = "NEWS_LIST_FRAGMENT";
	private final static String NEWS_URL = "ru.pda.nitro.listfragments.NewsListFragment.NEWS_URL";
	private final static String NEWS_POSITION = "ru.pda.nitro.listfragments.NewsListFragment.NEWS_POSITION";
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

			if (!isRefresh() && !isLoadmore() && getLocalNewsData())
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
			setLocalData(getActivity(),newsList, getUri(), newsUrl);
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

    public static void setLocalData(Context context, ArrayList<News> topics, Uri uri, String url)
	{

		if (url.equals(""))
		{
			deleteAllLocalData(context,uri);
			ContentValues cv = new ContentValues();
			for(News topic : topics){
			cv.put(Contract.News.description, topic.getDescription() != null ? topic.getDescription().toString() : "");
			cv.put(Contract.News.title,topic.getTitle() != null ? topic.getTitle().toString() : "");
			cv.put(Contract.News.id,topic.getId() != null ? topic.getId().toString() : "");
			cv.put(Contract.News.author, topic.getAuthor() != null ? topic.getAuthor().toString() : "");
			cv.put(Contract.News.newsDate,topic.getNewsDate() != null ? topic.getNewsDate().toString() : "");
			cv.put(Contract.News.imgUrl, topic.getImgUrl() != null ? topic.getImgUrl().toString() : "");
			
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
			context.getContentResolver().insert(uri, cv);
			}
		}
    }

}
