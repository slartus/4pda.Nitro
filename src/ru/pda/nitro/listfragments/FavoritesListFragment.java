package ru.pda.nitro.listfragments;

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

import android.widget.*;
import android.view.*;
import android.os.*;

import android.util.*;
import android.database.*;
import android.content.*;
import ru.pda.nitro.*;
import android.view.View.*;
import uk.co.senab.actionbarpulltorefresh.library.*;
import android.app.*;
import ru.pda.nitro.database.*;
import android.preference.*;
import android.widget.AbsListView.OnScrollListener;


/**
 * Created by slartus on 12.01.14.
 */
public class FavoritesListFragment extends TopicsListFragment implements OnScrollListener
{

	@Override
	public void setNextPage()
	{
		from = getFrom() + 31;
		setFrom(from);
		// TODO: Implement this method
	}

	
	@Override
	public void onScrollStateChanged(AbsListView p1, int p2)
	{
		// TODO: Implement this method
	}


	@Override
    public ArrayList<Topic> getTopicsList() throws ParseException, IOException
	{

        return TopicsApi.getFavorites(new HttpHelper(App.getInstance()), listInfo);
    }

    public String getTitle()
	{
        return FavoritesBrick.TITLE;
    }

    public String getName()
	{
        return FavoritesBrick.NAME;
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

		listInfo = new ListInfo();
		adapter = new TopicListAdapter(getActivity(), topics);
		
		listView.addFooterView(initialiseFooter());
		listView.setAdapter(adapter);
		listView.setOnScrollListener(this);
		getActivity().getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		
		getData();
	}

	@Override
	protected boolean getTopics() throws Throwable
	{
		// TODO: Implement this method
		super.getTopics();
	
		if (isRefresh())
		{
			if(isLoadmore()){
				ArrayList<Topic> data = getTopicsList();
				if(data.size() > 0){
				for(Topic topic : data){
					topics.add(topic);
				}
					deleteAllLocalData(Contract.Favorite.CONTENT_URI);
					setLocalData(topics);
				return true;
				}
				from = getFrom() - 31;
				setFrom(from);
				return false;
			}else{
			topics = getTopicsList();
			if (topics.size() > 0)
			{
				setOutCount(listInfo.getOutCount());
				deleteAllLocalData(Contract.Favorite.CONTENT_URI);
				setLocalData(topics);
				return true;
			}
			}
		}
		else
		{
			setFrom(getFrom());
			topics = getLocalData();
			setOutCount(getOutCount());
			if (topics.size() == 0)
			{
				topics = getTopicsList();
				if (topics.size() > 0)
				{
					setOutCount(listInfo.getOutCount());
					setLocalData(topics);
					return true;
				}
			}
			
			else
				return true;
		}
		
		
		return false;
	}
	public void setFrom(int from){
		this.from = from;
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
		SharedPreferences.Editor e = prefs.edit();
		e.putInt("_topics_from", from).commit();
		listInfo.setFrom(from);
	}

	

	public void setOutCount(int count){
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
		SharedPreferences.Editor e = prefs.edit();
		e.putInt("_topics_out_count", count).commit();
	}

	
	
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
		if ((firstVisibleItem + visibleItemCount) == totalItemCount && !loadMore && getCount() && !isLoading())
		{
			showFooter(true);

		}

    }
	

	@Override
	public void onDestroy()
	{
		// TODO: Implement this method
		super.onDestroy();
		if (task != null)
			task.cancel(true);
	}


}
