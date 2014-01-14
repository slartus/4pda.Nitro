package ru.pda.nitro.listfragments;

import java.util.ArrayList;

import ru.forpda.api.ForumsApi;
import ru.forpda.http.HttpHelper;
import ru.forpda.interfaces.forum.IListItem;
import ru.pda.nitro.App;
import ru.pda.nitro.bricks.ForumsBrick;
import android.view.*;
import android.widget.*;
import ru.pda.nitro.*;
import android.os.*;
import ru.pda.nitro.adapters.*;
import ru.forpda.interfaces.forum.*;


/**
 * Created by slartus on 12.01.14.
 */
public class ForumsListFragment extends BaseListFragment {
    
	private Task task;
	private ForumsListAdapter adapter;
	private ArrayList<Forum> forums = new ArrayList<Forum>();
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
		// TODO: Implement this method
		View v = inflater.inflate(R.layout.list_topic, container, false);
		listView = (ListView)v.findViewById(R.id.listViewTopic);
		linearProgress = (LinearLayout)getActivity().findViewById(R.id.linearProgress);
		return v;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		// TODO: Implement this method
		super.onActivityCreated(savedInstanceState);
		adapter = new ForumsListAdapter();
		listView.setAdapter(adapter);
		getData();
	}



	private void getData(){
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
