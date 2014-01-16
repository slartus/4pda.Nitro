package ru.pda.nitro.listfragments;

import android.app.Fragment;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;

import ru.forpda.interfaces.forum.IListItem;
import android.widget.*;
import android.view.*;
import android.os.*;
import android.view.View.*;
import ru.pda.nitro.R;
import ru.pda.nitro.*;
import uk.co.senab.actionbarpulltorefresh.library.*;
import ru.pda.nitro.database.*;


/**
 * Created by slartus on 12.01.14.
 * Базовый класс для списков
 * Здесь общие свойства и методы для фрагментов списков
 */
public abstract class BaseListFragment extends Fragment {
    public LinearLayout linearProgress;
	public LinearLayout linearError;
	private boolean loading = false;
	private boolean refresh = false;
	public Button buttonError;
	
	public ListView listView;

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
			
		((IRefreshActivity) getActivity()).getPullToRefreshAttacher().setRefreshableView(listView, new PullToRefreshAttacher.OnRefreshListener() {
				@Override
				public void onRefreshStarted(View view) {
					setRefresh(true);
					getActivity().getContentResolver().delete(Contract.Favorite.CONTENT_URI, null, null);
					getData();
				}
			});
	}
	
	public void setProgress(boolean loading) {

		((IRefreshActivity) getActivity()).getPullToRefreshAttacher().setRefreshing(loading);

	}
	

	public void setRefresh(boolean refresh)
	{
		this.refresh = refresh;
	}

	public boolean isRefresh()
	{
		return refresh;
	}

	public void setLoading(boolean loading)
	{
		this.loading = loading;
	}

	public boolean isLoading()
	{
		return loading;
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
		 return	inBackground();
			// TODO: Implement this method
		}

		@Override
		protected void onPostExecute(Boolean result)
		{
			// TODO: Implement this method
			super.onPostExecute(result);
			if(result){
				inExecute();
				linearProgress.setVisibility(View.GONE);
			}else
				showError(true);
			setProgress(false);
			setLoading(false);
		}


	}
	
	
	public abstract ArrayList<? extends IListItem> getList() throws ParseException, IOException;

    public abstract String getName();

    public abstract String getTitle();
	
	public abstract void getData();
	
	public abstract boolean inBackground();
	
	public abstract void inExecute();


}
