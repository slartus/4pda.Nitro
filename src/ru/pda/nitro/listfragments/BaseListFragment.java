package ru.pda.nitro.listfragments;

import android.support.v4.app.Fragment;

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
import android.app.*;
import android.util.*;
import android.net.*;
import ru.forpda.interfaces.*;
import android.content.*;
import android.preference.*;


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
	private boolean loadmore = false;
	public int from = -1;
	public Button buttonError;
	public RelativeLayout relativeMore;
	public TextView textMore;
	public ProgressBar progressMore;
	public boolean loadMore = false;
	public Handler mHandler = new Handler();
	public ListView listView;
	public ListInfo listInfo;

	public void setLoadmore(boolean loadmore)
	{
		this.loadmore = loadmore;
	}

	public boolean isLoadmore()
	{
		return loadmore;
	}


	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		// TODO: Implement this method
		super.onActivityCreated(savedInstanceState);
		
		
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
					setFrom(0);
					setRefresh(true);
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
	
	
		
	public View initialiseUi(View v){
		listView = (ListView)v.findViewById(R.id.listViewTopic);
		linearProgress = (LinearLayout)v.findViewById(R.id.linearProgress);
		linearError = (LinearLayout)v.findViewById(R.id.linearError);
		buttonError = (Button)v.findViewById(R.id.buttonError);
		
		return v;
	}
	
	public View initialiseFooter(){
		View footer = getActivity().getLayoutInflater().inflate(R.layout.footer, null, false);
		relativeMore = (RelativeLayout)footer.findViewById(R.id.relativeMore);
		progressMore = (ProgressBar)footer.findViewById(R.id.progressMore);
		textMore = (TextView)footer.findViewById(R.id.textMore);
		textMore.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View p1)
				{
					setProgressMore(true);
					setLoadmore(true);
					setRefresh(true);
					loadMore = true;
					setNextPage();
					getData();

					// TODO: Implement this method
				}
			});
			return footer;
	}
	
	
	
	public void setProgressMore(boolean show){
		if(show){
			progressMore.setVisibility(View.VISIBLE);
			textMore.setVisibility(View.GONE);
		}else{
			progressMore.setVisibility(View.GONE);
			textMore.setVisibility(View.VISIBLE);
			
		}
	}
	
	public void showFooter(boolean show){
		relativeMore.setVisibility(show ? View.VISIBLE : View.GONE);
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
			}else{
				showError(true);
				showFooter(false);
				}
			setProgressMore(false);
			setProgress(false);
			setLoading(false);
			setLoadmore(false);
			loadMore = false;
					
		}


	}
	
	public void deleteAllLocalData(Uri mUri){
		getActivity().getContentResolver().delete(mUri, null, null);
	}

    public abstract void onScrollStateChanged(AbsListView p1, int p2);

	public abstract void setFrom(int from);
	
	public abstract void setNextPage();
	
	public abstract ArrayList<? extends IListItem> getList() throws ParseException, IOException;

    public abstract String getName();

    public abstract String getTitle();
	
	public abstract void getData();
	
	public abstract boolean inBackground();
	
	public abstract void inExecute();


}
