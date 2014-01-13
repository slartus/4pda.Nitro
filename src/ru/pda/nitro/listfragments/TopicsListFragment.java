package ru.pda.nitro.listfragments;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;

import ru.forpda.interfaces.forum.IListItem;
import ru.forpda.interfaces.forum.Topic;
import android.os.*;
import android.view.*;
import ru.pda.nitro.adapters.*;
import ru.forpda.interfaces.*;
import ru.pda.nitro.*;


/**
 * Created by slartus on 12.01.14.
 * базовый класс для списков тем
 */
public abstract class TopicsListFragment extends BaseListFragment{
	public ArrayList<Topic> topics = new ArrayList<Topic>();
	public TopicListAdapter adapter;
	public Task task;
	public ListInfo listInfo;
	
	
	
    @Override
    public ArrayList<? extends IListItem> getList() throws ParseException, IOException {
        return getTopicsList();
    }

    public abstract ArrayList<Topic> getTopicsList() throws ParseException, IOException;
	
	protected boolean getTopics() throws Throwable {

		return false;
    }
	
	public void showError(boolean isError){
		if(isError){
			linearProgress.setVisibility(View.GONE);
			linearError.setVisibility(View.VISIBLE);
			}else{
				if(!isRefresh())
				linearProgress.setVisibility(View.VISIBLE);
				linearError.setVisibility(View.GONE);
				
			}
	}
	
	public void setProgress(boolean loading) {

		((IRefreshActivity) getActivity()).getPullToRefreshAttacher().setRefreshing(loading);

	}
	
	public void getData(){
		if(!isLoading()){
		
		task = new Task();
		task.execute();
		}else
		setProgress(false);
		
	}
	
	public class Task extends AsyncTask<Void, Void, Boolean>
	{

		@Override
		protected void onPreExecute()
		{
			// TODO: Implement this method
			super.onPreExecute();
			setLoading(true);
			showError(false);
			setRefresh(false);
		}

		
		@Override
		protected Boolean doInBackground(Void[] p1)
		{
			try
			{
				return getTopics();
			}
			catch (Throwable e)
			{}
			// TODO: Implement this method
			return false;
		}

		@Override
		protected void onPostExecute(Boolean result)
		{
			// TODO: Implement this method
			super.onPostExecute(result);
			if(result){
			adapter.setData(topics);
			adapter.notifyDataSetChanged();
			linearProgress.setVisibility(View.GONE);
			}else
			showError(true);
			
			setProgress(false);
			setLoading(false);
		}

		
	}
}
