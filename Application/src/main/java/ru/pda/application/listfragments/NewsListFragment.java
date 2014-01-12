package ru.pda.application.listfragments;

import java.util.ArrayList;

import ru.pda.api.ForumsApi;
import ru.pda.api.NewsApi;
import ru.pda.application.App;
import ru.pda.application.bricks.ForumsBrick;
import ru.pda.application.bricks.NewsBrick;
import ru.pda.http.HttpHelper;
import ru.pda.interfaces.ListInfo;
import ru.pda.interfaces.forum.IListItem;

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
