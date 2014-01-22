package ru.pda.nitro.listfragments;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;

import ru.forpda.http.HttpHelper;
import ru.forpda.interfaces.forum.IListItem;
import ru.forpda.interfaces.forum.News;
import ru.forpda.interfaces.forum.NewsList;
import ru.pda.nitro.App;
import ru.pda.nitro.R;
import ru.pda.nitro.adapters.NewsListAdapter;
import ru.pda.nitro.bricks.NewsBrick;
import ru.pda.nitro.database.Contract;


/**
 * Created by slartus on 12.01.14.
 */
public class NewsListFragment extends BaseListFragment {
    @Override
    public void onScroll(int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if ((firstVisibleItem + visibleItemCount) == totalItemCount && !loadMore && !isLoading()) {
            showFooter(true);

        }
        // TODO: Implement this method
    }


    @Override
    public void setFrom(int from) {
        // TODO: Implement this method
    }


    @Override
    public void setNextPage() {
        newsUrl = newsUrl + "page/" + ++from;

    }


    @Override
    public void onScrollStateChanged(AbsListView p1, int p2) {
        // TODO: Implement this method
    }

    private NewsList newsList;
    private String newsUrl;
    private Task task;
    private NewsListAdapter adapter;
    private ArrayList<News> news = new ArrayList<News>();

    @Override
    public ArrayList<? extends IListItem> getList() {


        return news;
    }

    @Override
    public String getTitle() {
        return NewsBrick.TITLE;
    }

    @Override
    public String getName() {
        return NewsBrick.NAME;
    }

    public static NewsListFragment newInstance(String url) {
        final NewsListFragment f = new NewsListFragment();

        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putString("_url", url);
        f.setArguments(args);

        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: Implement this method
        View v = inflater.inflate(R.layout.list_topic, container, false);
        return initialiseListUi(v);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // TODO: Implement this method
        super.onActivityCreated(savedInstanceState);
        adapter = new NewsListAdapter(getActivity(), news);
        listView.addFooterView(initialiseFooter());
        listView.setAdapter(adapter);
        listView.setOnScrollListener(this);
        newsUrl = getArguments().getString("_url");
        getData();

    }

    @Override
    public boolean inBackground() {
        try {
            if (newsUrl.equals(""))
                if (isRefresh()) {
                    news = new ArrayList<News>();
                    newsList = new NewsList(new HttpHelper(App.getInstance()), newsUrl);

                    newsList.loadNextNewsPage();
                    for (News data : newsList) {
                        news.add(data);
                    }
                    if (news.size() > 0) {
                        if (!isLoadmore()) {
                            deleteAllLocalData(Contract.News.CONTENT_URI);
                            setLocalData(news);
                        }
                        return true;
                    }
                } else {
                    news = getLocalData();
                    if (news.size() == 0) {
                        newsList = new NewsList(new HttpHelper(App.getInstance()), newsUrl);

                        newsList.loadNextNewsPage();
                        for (News data : newsList) {
                            news.add(data);
                        }
                        if (news.size() > 0) {
                            setLocalData(news);
                            return true;
                        }
                    } else
                        return true;
                }
            else {
                newsList = new NewsList(new HttpHelper(App.getInstance()), newsUrl);

                newsList.loadNextNewsPage();
                for (News data : newsList) {
                    news.add(data);
                }
                return true;
            }

        } catch (ParseException e) {
        } catch (IOException e) {
        }

        return false;
        // TODO: Implement this method
    }

    @Override
    public void inExecute() {
        adapter.setData(news);
        adapter.notifyDataSetChanged();
        // TODO: Implement this method
    }

    public void getData() {
        if (!isLoading()) {

            task = new Task();
            task.execute();
        } else
            setProgress(false);

    }


    public ArrayList<News> getLocalData() {
        news = new ArrayList<News>();
        Cursor cursor = getActivity().getContentResolver().query(Contract.News.CONTENT_URI, null, null, null, Contract.Favorite.DEFAULT_SORT_ORDER);
        if (cursor.moveToFirst()) {
            do {
                News topic = new News(null, null);
                topic.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(Contract.News.description)));
                topic.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(Contract.News.title)));
                topic.setId(cursor.getString(cursor.getColumnIndexOrThrow(Contract.News.id)));
                topic.setAuthor(cursor.getString(cursor.getColumnIndexOrThrow(Contract.News.author)));
                topic.setNewsDate(cursor.getString(cursor.getColumnIndexOrThrow(Contract.News.newsDate)));
                topic.setImgUrl(cursor.getString(cursor.getColumnIndexOrThrow(Contract.News.imgUrl)));
                news.add(topic);
            } while (cursor.moveToNext());

        }
        cursor.close();
        return news;
    }

    public void setLocalData(ArrayList<News> topics) {

        for (News topic : topics) {
            ContentValues cv = new ContentValues();
            cv.put(Contract.News.description, topic.getDescription().toString());
            cv.put(Contract.News.title, topic.getTitle().toString());
            cv.put(Contract.News.id, topic.getId().toString());
            cv.put(Contract.News.author, topic.getAuthor().toString());
            cv.put(Contract.News.newsDate, topic.getNewsDate().toString());
            cv.put(Contract.News.imgUrl, topic.getImgUrl().toString());
            getActivity().getContentResolver().insert(Contract.News.CONTENT_URI, cv);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

}
