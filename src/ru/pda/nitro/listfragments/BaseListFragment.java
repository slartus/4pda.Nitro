package ru.pda.nitro.listfragments;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;

import ru.forpda.interfaces.ListInfo;
import ru.forpda.interfaces.forum.IListItem;
import ru.pda.nitro.BaseFragment;
import ru.pda.nitro.BaseState;
import ru.pda.nitro.R;
import ru.pda.nitro.WidgetsHelper;
import ru.pda.nitro.adapters.BaseListAdapter;
import ru.pda.nitro.bricks.FavoritesBrick;
import ru.pda.nitro.bricks.NewsBrick;
import ru.pda.nitro.bricks.*;
import android.widget.*;
import ru.forpda.common.*;
import android.content.*;
import android.preference.*;
import android.app.*;

/**
 * Created by slartus on 12.01.14.
 * Базовый класс для списков
 * Здесь общие свойства и методы для фрагментов списков
 */
public abstract class BaseListFragment extends BaseFragment
        implements AbsListView.OnScrollListener,
        AdapterView.OnItemClickListener,
        android.view.View.OnCreateContextMenuListener{
	
	
	public final static String QUICK_START_LIST_ITEM_SELECT = "ru.pda.nitro.listfragments.BaseListFragment.QUICK_START_LIST_ITEM_SELECT";
	private Task task;
    private boolean loadmore = false;
    public int from = -1;
	private int old_from;
    public Button buttonError;
    public RelativeLayout relativeMore;
    public TextView textMore;
    public ProgressBar progressMore;
    public boolean loadMore = false;
	public boolean download = false;
	public boolean reload = false;
    public ListView listView;

	public void setReload(boolean reload)
	{
		this.reload = reload;
	}

	public boolean isReload()
	{
		return reload;
	}

	public void setDownload(boolean download)
	{
		this.download = download;
	}

	public boolean isDownload()
	{
		return download;
	}
	@Override
    public void onActivityCreated(Bundle savedInstanceState)
	{
        super.onActivityCreated(savedInstanceState);
     //   BaseState.setMTitle(getTitle());
	//	getActivity().getActionBar().setTitle(BaseState.getSpannable(getActivity(), getTitle()));
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
		final String favorite = prefs.getString("mainFavorite_", "favorites");
		if (getName().equals(favorite)){
		//	getPullToRefreshAttacher(listView);
			setCurrentFragmentMenu();
			if(getName().equals(NewsBrick.NAME))
				getActivity().getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
			
		}
		
    }

	@Override
	protected void refreshData()
	{
		super.refreshData();
		if(!isLoading()){
			setOld_from(from);
			setFrom(0);
			setRefresh(true);
		}
		getData();
	}
	

    @Override
    public void onItemClick(android.widget.AdapterView<?> adapterView, android.view.View view, int i, long l){}

    @Override
    public void onCreateContextMenu(android.view.ContextMenu contextMenu, android.view.View view, android.view.ContextMenu.ContextMenuInfo contextMenuInfo) {}


    public View initialiseFooter()
	{
        View footer = getActivity().getLayoutInflater().inflate(R.layout.footer, null, false);
        relativeMore = (RelativeLayout) footer.findViewById(R.id.relativeMore);
        progressMore = (ProgressBar) footer.findViewById(R.id.progressMore);
        textMore = (TextView) footer.findViewById(R.id.textMore);
        textMore.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View p1)
				{
					setProgressMore(true);
					setLoadmore(true);
					setRefresh(true);
					loadMore = true;
					setNextPage();
					getData();

				}
			});
        return footer;
    }
	
	public void setProgressStatus(){
		if(!isLoadmore() && !isDownload())
		showFooter(isRefresh());
		
		if(isLoading() && !isDownload() && !isLoadmore())
			mSwipeRefreshLayout.setRefreshing(true);
	}

	@Override
	protected void getData()
	{
		if(!isLoading()){
			task = new Task();
			task.execute();
		}else if(isLoadmore() | isDownload()){
			mSwipeRefreshLayout.setRefreshing(false);
		}
		super.getData();
	}

	

    public void setProgressMore(boolean show)
	{
        if (show)
		{
            progressMore.setVisibility(View.VISIBLE);
            textMore.setVisibility(View.GONE);
        }
		else
		{
            progressMore.setVisibility(View.GONE);
            textMore.setVisibility(View.VISIBLE);

        }
    }
	
	public void updateAdapter(BaseListAdapter adapter){
		adapter.notifyDataSetChanged();
		WidgetsHelper.updateAllWidgets(getActivity());

	}

    public void setDataInAdapter(BaseListAdapter adapter, ArrayList<? extends IListItem> data) {
        adapter.setData(data);
	}


    public void showFooter(boolean show)
	{
        relativeMore.setVisibility(show ? View.GONE : View.VISIBLE);
    }
	
	

    public class Task extends AsyncTask<Void, Void, Boolean>
	{

        @Override
        protected void onPreExecute()
		{
            super.onPreExecute();
            setLoading(true);
            showStatus(linearProgress, linearError,false);
			
			setProgressStatus();
        }


        @Override
        protected Boolean doInBackground(Void[] p1)
		{
			
            return inBackground();
        }

		@Override
		protected void onCancelled()
		{
			// TODO: Implement this method
			super.onCancelled();
		}
		
		

        @Override
        protected void onPostExecute(Boolean result)
		{

            super.onPostExecute(result);
            if (result)
			{
                inExecute();
                hideProgress();
            }
			else
			{
				if (isLoadmore())
				{
					textMore.setText(R.string.error);
				}
				else
				{
					textMore.setText("Загрузить еще...");
					showStatus(linearProgress,linearError,true);
					showFooter(true);
				}
            }
            setProgressMore(false);
			if (getActivity() != null)
				mSwipeRefreshLayout.setRefreshing(false);

			/*	if(!isLoading() && checkStatus()){
					setProgress(true);
					refreshData();
				}else{*/
			setDownload(false);
            setLoading(false);
            setLoadmore(false);
            loadMore = false;
	//		}

        }


    }
	
	private boolean checkStatus(){
		if(NewsBrick.NAME.equals(getName()) && BaseState.isRefresh_news()){
			BaseState.setRefresh_news(false);
			return true;
		}else if(FavoritesBrick.NAME.equals(getName()) && BaseState.isRefresh_favorite()){
			BaseState.setRefresh_favorite(false);
			return true;
		}else if(SubscribesBrick.NAME.equals(getName()) && BaseState.isRefresh_subscribe()){
			BaseState.setRefresh_subscribe(false);
			return true;
		}
		return false;
	}

    public static void deleteAllLocalData(Context context, Uri mUri)
	{
        if (mUri != null)
			context.getContentResolver().delete(mUri, null, null);
    }

	@Override
	public void onDestroyView()
	{
		Log.d("onDestroyView");
		super.onDestroyView();
	}

	@Override
	public void onDetach()
	{
		Log.e("onDetach");
		super.onDetach();
	}

	
	
	@Override
	public void onDestroy()
	{
		Log.e("onDestroy");
		if(task != null)
			task.cancel(true);
		super.onDestroy();
	
	}
	
	protected void getLocalDataOnStart(){}

    protected void setFrom(int from){}

    protected void setNextPage(){}
	
	public abstract String getClassName();

    public abstract ArrayList<? extends IListItem> getList() throws ParseException, IOException;

	protected Uri getUri(){return null;}

    public abstract boolean inBackground();

    public abstract void inExecute();

	public void setOld_from(int old_from)
	{
		this.old_from = old_from;
	}

	public int getOld_from()
	{
		return old_from;
	}

    public void setLoadmore(boolean loadmore)
	{
        this.loadmore = loadmore;
    }

    public boolean isLoadmore()
	{
        return loadmore;
    }
}
