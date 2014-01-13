package ru.pda.nitro.listfragments;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;

import ru.forpda.interfaces.forum.IListItem;
import ru.forpda.interfaces.forum.Topic;
import android.os.*;
import android.view.*;
import ru.pda.nitro.adapters.*;


/**
 * Created by slartus on 12.01.14.
 * базовый класс для списков тем
 */
public abstract class TopicsListFragment extends BaseListFragment{
	public ArrayList<Topic> topics = new ArrayList<Topic>();
	public TopicListAdapter adapter;
	public Task task;
	
    @Override
    public ArrayList<? extends IListItem> getList() throws ParseException, IOException {
        return getTopicsList();
    }

    public abstract ArrayList<Topic> getTopicsList() throws ParseException, IOException;
	
	protected boolean getTopics() throws Throwable {

		return false;
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
			}
		}

		
	}
}
