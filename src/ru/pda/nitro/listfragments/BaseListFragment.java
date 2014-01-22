package ru.pda.nitro.listfragments;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
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
public abstract class BaseListFragment extends BaseFragment implements AbsListView.OnScrollListener,
        AdapterView.OnItemClickListener {
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

    public void setLoadmore(boolean loadmore) {
        this.loadmore = loadmore;
    }

    public boolean isLoadmore() {
        return loadmore;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        buttonError.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View p1) {
                getData();
            }
        });
        listView.setOnItemClickListener(this);

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

    @Override
    public void onItemClick(android.widget.AdapterView<?> adapterView, android.view.View view,
                            int i, long l) {

    }


    public void setRefresh(boolean refresh) {
        this.refresh = refresh;
    }

    public boolean isRefresh() {
        return refresh;
    }

    public void setLoading(boolean loading) {
        this.loading = loading;
    }

    public boolean isLoading() {
        return loading;
    }

    public void showError(boolean isError) {
        if (isError) {
            linearProgress.setVisibility(View.GONE);
            linearError.setVisibility(View.VISIBLE);
        } else {
            if (!isRefresh())
                linearProgress.setVisibility(View.VISIBLE);
            linearError.setVisibility(View.GONE);

        }
    }


    public View initialiseListUi(View v) {
        listView = (ListView) v.findViewById(R.id.listViewTopic);
        initialiseUi(v);
        return v;
    }

    public View initialiseUi(View v) {
        listView = (ListView) v.findViewById(R.id.listViewTopic);
        linearProgress = (LinearLayout) v.findViewById(R.id.linearProgress);
        linearError = (LinearLayout) v.findViewById(R.id.linearError);
        buttonError = (Button) v.findViewById(R.id.buttonError);

        return v;
    }

    public View initialiseFooter() {
        View footer = getActivity().getLayoutInflater().inflate(R.layout.footer, null, false);
        relativeMore = (RelativeLayout) footer.findViewById(R.id.relativeMore);
        progressMore = (ProgressBar) footer.findViewById(R.id.progressMore);
        textMore = (TextView) footer.findViewById(R.id.textMore);
        textMore.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View p1) {
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


    public void setProgressMore(boolean show) {
        if (show) {
            progressMore.setVisibility(View.VISIBLE);
            textMore.setVisibility(View.GONE);
        } else {
            progressMore.setVisibility(View.GONE);
            textMore.setVisibility(View.VISIBLE);

        }
    }

    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        onScroll(firstVisibleItem, visibleItemCount, totalItemCount);
    }

    public void showFooter(boolean show) {
        relativeMore.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    public class Task extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            setLoading(true);
            showError(false);
        }


        @Override
        protected Boolean doInBackground(Void[] p1) {
            return inBackground();
        }

        @Override
        protected void onPostExecute(Boolean result) {

            super.onPostExecute(result);
            if (result) {
                inExecute();
                linearProgress.setVisibility(View.GONE);
            } else {
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

    public void deleteAllLocalData(Uri mUri) {
        getActivity().getContentResolver().delete(mUri, null, null);
    }

    public abstract void onScroll(int firstVisibleItem, int visibleItemCount, int totalItemCount);

    public abstract void setFrom(int from);

    public abstract void setNextPage();

    public abstract ArrayList<? extends IListItem> getList() throws ParseException, IOException;

    public abstract String getName();

    public abstract String getTitle();

    public abstract void getData();

    public abstract boolean inBackground();

    public abstract void inExecute();


}
