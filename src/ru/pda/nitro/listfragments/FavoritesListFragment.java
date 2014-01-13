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


/**
 * Created by slartus on 12.01.14.
 */
public class FavoritesListFragment extends TopicsListFragment {
	private Button buttonError;

	@Override
    public ArrayList<Topic> getTopicsList() throws ParseException, IOException {
        return TopicsApi.getFavorites(new HttpHelper(App.getInstance()), new ListInfo());
    }

    public String getTitle() {
        return FavoritesBrick.TITLE;
    }

    public String getName() {
        return FavoritesBrick.NAME;
    }

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		// TODO: Implement this method
		View v = inflater.inflate(R.layout.list_topic, container, false);
		listView = (ListView)v.findViewById(R.id.listViewTopic);
		
		return v;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		// TODO: Implement this method
		super.onActivityCreated(savedInstanceState);
		linearProgress = (LinearLayout)getActivity().findViewById(R.id.linearProgress);
		linearError = (LinearLayout)getActivity().findViewById(R.id.linearError);
		buttonError = (Button)getActivity().findViewById(R.id.buttonError);
		buttonError.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View p1)
				{
					getData();
				}
			});
		
		adapter = new TopicListAdapter(getActivity(), topics);
		listView.setAdapter(adapter);
		
		getData();
		
		((IRefreshActivity) getActivity()).getPullToRefreshAttacher().setRefreshableView(listView, new PullToRefreshAttacher.OnRefreshListener() {
				@Override
				public void onRefreshStarted(View view) {
					setRefresh(true);
					getData();
				}
			});
	}

	@Override
	protected boolean getTopics() throws Throwable
	{
		// TODO: Implement this method
		super.getTopics();
		topics = getTopicsList();
		if(topics.size() > 0)
			return true;
			
		return false;
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
