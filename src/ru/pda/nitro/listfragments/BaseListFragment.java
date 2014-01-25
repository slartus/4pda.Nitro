package ru.pda.nitro.listfragments;

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
import ru.pda.nitro.IRefreshActivity;
import ru.pda.nitro.R;
import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshAttacher;


/**
 * Created by slartus on 12.01.14.
 * Базовый класс для списков
 * Здесь общие свойства и методы для фрагментов списков
 */
public abstract class BaseListFragment extends BaseFragment
        implements AbsListView.OnScrollListener,
        AdapterView.OnItemClickListener,
        android.view.View.OnCreateContextMenuListener {
	public Task task;
    private boolean loadmore = false;
    public int from = -1;
	private int old_from;
    public Button buttonError;
    public RelativeLayout relativeMore;
    public TextView textMore;
    public ProgressBar progressMore;
    public boolean loadMore = false;
    public ListInfo listInfo;
	public ListView listView;


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


    @Override
    public void onActivityCreated(Bundle savedInstanceState)
	{
        super.onActivityCreated(savedInstanceState);
        listView.setOnItemClickListener(this);
        registerForContextMenu(listView);
        listView.setOnCreateContextMenuListener(this);
		
		
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
    public void onItemClick(android.widget.AdapterView<?> adapterView, android.view.View view,
                            int i, long l)
	{

    }

    @Override
    public void onCreateContextMenu(android.view.ContextMenu contextMenu, android.view.View view,
                                    android.view.ContextMenu.ContextMenuInfo contextMenuInfo) {

    }

    public View initialiseListUi(View v)
	{
		listView = (ListView) v.findViewById(R.id.listViewTopic);
		return initialiseUi(v);
    }


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


    public void showFooter(boolean show)
	{
        relativeMore.setVisibility(show ? View.VISIBLE : View.GONE);
    }
	

    public class Task extends AsyncTask<Void, Void, Boolean>
	{

        @Override
        protected void onPreExecute()
		{
            super.onPreExecute();
            setLoading(true);
            showError(false);
        }


        @Override
        protected Boolean doInBackground(Void[] p1)
		{
            return inBackground();
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
					showError(true);
					showFooter(false);
				}
            }
            setProgressMore(false);
			if (getActivity() != null)
				setProgress(false);

				
            setLoading(false);
            setLoadmore(false);
            loadMore = false;

        }


    }

    public void deleteAllLocalData(Uri mUri)
	{
        if (mUri != null)
			getActivity().getContentResolver().delete(mUri, null, null);
    }

	
	@Override
	public void onDestroy()
	{
		super.onDestroy();
		if(task != null)
			task.cancel(true);
	}
	
	
	

    protected void setFrom(int from)
	{}

    protected void setNextPage()
	{}

    public abstract ArrayList<? extends IListItem> getList() throws ParseException, IOException;

	public abstract Uri getUri();
	
    public abstract String getName();

    public abstract String getTitle();

    public abstract void getData();

    public abstract boolean inBackground();

    public abstract void inExecute();


}
