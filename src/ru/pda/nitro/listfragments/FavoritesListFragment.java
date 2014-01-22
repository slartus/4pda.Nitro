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


/**
 * Created by slartus on 12.01.14.
 */
public class FavoritesListFragment extends TopicsListFragment implements OnScrollListener
{

	@Override
	public void onScrollStateChanged(AbsListView p1, int p2)
	{

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

        View v = inflater.inflate(R.layout.list_topic, container, false);

        return initialiseListUi(v);
    }

	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{

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

    @Override
    public void setNextPage() {
        from = getFrom() + 31;
        setFrom(from);

    }

    @Override
    public void onScroll(int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if ((firstVisibleItem + visibleItemCount) == totalItemCount && !loadMore && getCount() && !isLoading()) {
            showFooter(true);

        }
        // TODO: Implement this method
    }

    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
		if ((firstVisibleItem + visibleItemCount) == totalItemCount && !loadMore && getCount() && !isLoading())
		{
			showFooter(true);

		}

    }


}
