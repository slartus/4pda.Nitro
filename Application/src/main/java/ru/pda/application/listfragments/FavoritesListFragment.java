package ru.pda.application.listfragments;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;


import ru.pda.api.TopicsApi;
import ru.pda.application.App;
import ru.pda.application.bricks.FavoritesBrick;
import ru.pda.http.HttpHelper;
import ru.pda.interfaces.ListInfo;
import ru.pda.interfaces.forum.Topic;
import android.widget.*;
import android.view.*;
import android.os.*;
import ru.pda.application.*;

import ru.pda.application.adapters.ListAdapter;


/**
 * Created by slartus on 12.01.14.
 */
public class FavoritesListFragment extends TopicsListFragment {
    private ListView listView;
	private ListAdapter adapter;
	private Task task;
	private ArrayList<Topic> topic = new ArrayList<Topic>();
	
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
		adapter = new ListAdapter(getActivity(), topic);
		listView.setAdapter(adapter);
		getData();
	}
	
	private void getData(){
		task = new Task();
		task.execute();
	}
	
	public class Task extends AsyncTask<Void, Void, Void>
	{

		@Override
		protected void onPreExecute()
		{
			// TODO: Implement this method
			super.onPreExecute();
		}

		
		@Override
		protected Void doInBackground(Void[] p1)
		{
			try
			{
				topic = getTopicsList();
			}
			catch (IOException e)
			{}
			catch (ParseException e)
			{}
			// TODO: Implement this method
			return null;
		}

		@Override
		protected void onPostExecute(Void result)
		{
			// TODO: Implement this method
			super.onPostExecute(result);
			adapter.notifyDataSetChanged();
		}

		
	}
}
