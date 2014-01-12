package ru.pda.application.listfragments;

import java.util.ArrayList;

import ru.pda.api.ForumsApi;
import ru.pda.application.App;
import ru.pda.application.bricks.ForumsBrick;
import ru.pda.http.HttpHelper;
import ru.pda.interfaces.forum.IListItem;

/**
 * Created by slartus on 12.01.14.
 */
public class ForumsListFragment extends BaseListFragment {
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
}
