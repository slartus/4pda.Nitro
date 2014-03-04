package ru.pda.nitro.listfragments;

import android.app.ActionBar;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;

import java.util.ArrayList;

import ru.forpda.api.ForumsApi;
import ru.forpda.http.HttpHelper;
import ru.forpda.interfaces.forum.Forum;
import ru.forpda.interfaces.forum.IListItem;
import ru.pda.nitro.App;
import ru.pda.nitro.R;
import ru.pda.nitro.adapters.ForumsListAdapter;
import ru.pda.nitro.bricks.ForumsBrick;
import android.net.*;
import android.widget.*;


/**
 * Created by slartus on 12.01.14.
 */
public class ForumsListFragment extends BaseListFragment implements FragmentLifecycle
{
		@Override
	public void onResumeFragment()
	{
	//	getPullToRefreshAttacher(listView);
		// TODO: Implement this method
	}

	

	@Override
	public String getClassName()
	{
		return null;
	}

	@Override
	public Uri getUri()
	{
		return ForumsBrick.URI;
	}

    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
		if ((firstVisibleItem + visibleItemCount) == totalItemCount && !loadMore && !isLoading())
		{
			showFooter(true);

		}

    }

    @Override
    public void setFrom(int from)
	{
		// TODO: Implement this method
	}


	@Override
	public void setNextPage()
	{
		// TODO: Implement this method
	}


	@Override
	public void onScrollStateChanged(AbsListView p1, int p2)
	{
		// TODO: Implement this method
	}


	@Override
	public boolean inBackground()
	{
		return true;
		// TODO: Implement this method
	}

	@Override
	public void inExecute()
	{
		// TODO: Implement this method
	}

    
	private Task task;
	private ForumsListAdapter adapter;
	private ArrayList<Forum> forums = new ArrayList<Forum>();
//	private ListView listView;
	@Override
    public ArrayList<? extends IListItem> getList() {
        return ForumsApi.getForums(new HttpHelper(App.getInstance()));
    }

    @Override
    public String getTitle() {
        return ForumsBrick.TITLE;
    }

    @Override
    public String getName() {
        return ForumsBrick.NAME;
    }
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{

        View v = inflater.inflate(R.layout.list_topic, container, false);
        listView = (ListView) v.findViewById(R.id.listViewTopic);
		
		return initialiseUi(v);
    }

	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{

        super.onActivityCreated(savedInstanceState);
		adapter = new ForumsListAdapter();
		listView.setAdapter(adapter);
		getActivity().getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
	//	getPullToRefreshAttacher(listView);
		
		getData();
	}
	
	public void getData(){
		task = new Task();
		task.execute();
	}

	public class Task extends AsyncTask<Void, Void, Boolean>
	{

		@Override
		protected void onPreExecute()
		{
			// TODO: Implement this method
			super.onPreExecute();
			linearProgress.setVisibility(View.VISIBLE);
		}


		@Override
		protected Boolean doInBackground(Void[] p1)
		{

			forums = (ArrayList<Forum>) getList();
			// TODO: Implement this method
			return false;
		}

		@Override
		protected void onPostExecute(Boolean result)
		{
			// TODO: Implement this method
			super.onPostExecute(result);
			if(result){

				//	adapter.setData(topics);
				adapter.notifyDataSetChanged();
				linearProgress.setVisibility(View.GONE);
			}else
				getData();
		}


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
