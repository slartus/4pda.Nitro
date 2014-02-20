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
import ru.pda.nitro.bricks.*;


/**
 * Created by slartus on 12.01.14.
 */
public class SubscribesListFragment extends TopicsListFragment implements OnScrollListener
{

	@Override
	public void onScrollStateChanged(AbsListView p1, int p2)
	{

    }


	@Override
    public ArrayList<Topic> getTopicsList() throws ParseException, IOException
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
		getActivity().getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);

		listInfo = new ListInfo();
		adapter = new TopicListAdapter(getActivity(), topics);

		listView.addFooterView(initialiseFooter());
		listView.setAdapter(adapter);
//		registerForContextMenu(listView);
		listView.setOnScrollListener(this);

		getPullToRefreshAttacher(listView);

	}

	@Override
	public void onResume()
	{
		getData();
		super.onResume();
	}



	@Override
	protected boolean getTopics() throws Throwable
	{
		super.getTopics();

		if (isRefresh())
		{
			if(isLoadmore()){
				ArrayList<Topic> data = getTopicsList();
				if(data.size() > 0){
					for(Topic topic : data){
						topics.add(topic);
					}
					deleteAllLocalData(getActivity(), Contract.Favorite.CONTENT_URI);
					setLocalData(getActivity(),topics, getUri());
					return true;
				}
			}else{
				topics = getTopicsList();
				if (topics.size() > 0)
				{
					setOutCount(listInfo.getOutCount());
					deleteAllLocalData(getActivity(),getUri());
					setLocalData(getActivity(),topics, getUri());
					return true;
				}
			}
		}
		else
		{
			setFrom(getFrom());
			setOutCount(getOutCount());
			if (topics.size() == 0)
			{
				topics = getTopicsList();
				if (topics.size() > 0)
				{
					setOutCount(listInfo.getOutCount());
					setLocalData(getActivity(),topics, getUri());
					return true;
				}
			}

			else
				setLoading(false);
			return true;
		}

		setFrom(getOld_from());
		return false;
	}

	@Override
	protected void setFrom(int from){
		this.from = from;
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
		SharedPreferences.Editor e = prefs.edit();
		e.putInt("_topics_from" + getName(), from).commit();
		listInfo.setFrom(from);
	}



	public void setOutCount(int count){
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
		SharedPreferences.Editor e = prefs.edit();
		e.putInt("_topics_out_count"+ getName(), count).commit();
	}

    @Override
    protected void setNextPage() {
		setOld_from(from);
        setFrom(topics.size());
    }

    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
		if ((firstVisibleItem + visibleItemCount) == totalItemCount && !loadMore && getCount() && !isLoading())
		{
			showFooter(true);

		}

    }


}
