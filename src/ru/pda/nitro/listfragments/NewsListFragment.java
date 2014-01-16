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


/**
 * Created by slartus on 12.01.14.
 */
public class NewsListFragment extends BaseListFragment
{

	@Override
	public boolean inBackground()
	{
		try
		{
			news = (ArrayList<News>) getList();

		}
		catch (Throwable e)
		{}
		// TODO: Implement this method
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

    
	private Task task;
	private NewsListAdapter adapter;
	private ArrayList<News> news = new ArrayList<News>();
	@Override
    public ArrayList<? extends IListItem> getList() {
        return NewsApi.getNews(new HttpHelper(App.getInstance()), new ListInfo());
    }

    @Override
    public String getTitle() {
        return NewsBrick.TITLE;
    }

    @Override
    public String getName() {
        return NewsBrick.NAME;
    }
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		// TODO: Implement this method
		View v = inflater.inflate(R.layout.list_topic, container, false);
		listView = (ListView)v.findViewById(R.id.listViewTopic);
//		linearProgress = (LinearLayout)getActivity().findViewById(R.id.linearProgress);
		return v;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		// TODO: Implement this method
		super.onActivityCreated(savedInstanceState);
		adapter = new NewsListAdapter(getActivity(), news);
		listView.setAdapter(adapter);
		getData();
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
