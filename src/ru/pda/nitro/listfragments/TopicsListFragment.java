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
	
	@Override
	public boolean inBackground()
	{
		try
		{
			return getTopics();
		}
		catch (Throwable e)
		{}
		
		return false;
		// TODO: Implement this method
	}

	@Override
	public void inExecute()
	{
		adapter.setData(topics);
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
	
	
}
