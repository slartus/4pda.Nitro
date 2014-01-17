package ru.pda.nitro.listfragments;

import java.util.ArrayList;

import ru.forpda.api.NewsApi;
import ru.forpda.http.HttpHelper;
import ru.forpda.interfaces.ListInfo;
import ru.forpda.interfaces.forum.IListItem;
import ru.pda.nitro.App;
import ru.pda.nitro.bricks.NewsBrick;
import android.view.*;
import android.widget.*;
import android.os.*;
import ru.pda.nitro.*;
import ru.pda.nitro.adapters.*;
import ru.forpda.interfaces.forum.*;
import org.apache.http.client.*;
import ru.forpda.interfaces.*;
import java.text.*;
import java.io.*;
import android.util.*;
import android.app.*;
import android.support.v4.view.*;


/**
 * Created by slartus on 12.01.14.
 */
public class NewsListFragment extends BaseListFragment
{
	private NewsList newsList;
	private String newsUrl;
	private Task task;
	private NewsListAdapter adapter;
	private ArrayList<News> news = new ArrayList<News>();
	
	@Override
    public ArrayList<? extends IListItem> getList() {
        
		
		return news;
    }

    @Override
    public String getTitle() {
        return NewsBrick.TITLE;
    }

    @Override
    public String getName() {
        return NewsBrick.NAME;
    }
	
	public static NewsListFragment newInstance(String url)
	{
		NewsListFragment f = new NewsListFragment();

		// Supply num input as an argument.
		Bundle args = new Bundle();
		args.putString("_url", url);
		f.setArguments(args);

		return f;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		// TODO: Implement this method
		View v = inflater.inflate(R.layout.list_topic, container, false);
		return initialiseUi(v);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		// TODO: Implement this method
		super.onActivityCreated(savedInstanceState);
		adapter = new NewsListAdapter(getActivity(), news);
		listView.setAdapter(adapter);
		newsUrl = getArguments().getString("_url");
		getData();
		
		
	}

	@Override
	public boolean inBackground()
	{
		try
		{
			newsList = new NewsList(new HttpHelper(App.getInstance()), newsUrl);
			
			newsList.loadNextNewsPage();
			for(News data : newsList){
				news.add(data);
			}
			return true;
		}
		catch (ParseException e)
		{}
		catch (IOException e)
		{}
		
		return false;
		// TODO: Implement this method
	}

	@Override
	public void inExecute()
	{
		adapter.setData(news);
		adapter.notifyDataSetChanged();
		// TODO: Implement this method
	}

	public void getData(){
		if(!isLoading()){

			task = new Task();
			task.execute();
		}else
			setProgress(false);

	}

	@Override
	public void onDestroy()
	{
		// TODO: Implement this method
		super.onDestroy();
		if(task != null)
			task.cancel(true);
	}
}
