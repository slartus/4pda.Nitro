package ru.pda.nitro.listfragments;

import java.util.ArrayList;

import ru.forpda.api.NewsApi;
import ru.forpda.http.HttpHelper;
import ru.forpda.interfaces.ListInfo;
import ru.forpda.interfaces.forum.IListItem;
import ru.pda.nitro.App;
import ru.pda.nitro.bricks.NewsBrick;


/**
 * Created by slartus on 12.01.14.
 */
public class NewsListFragment extends BaseListFragment {
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
}
