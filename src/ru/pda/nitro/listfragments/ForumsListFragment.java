package ru.pda.nitro.listfragments;

import java.util.ArrayList;

import ru.forpda.api.ForumsApi;
import ru.forpda.http.HttpHelper;
import ru.forpda.interfaces.forum.IListItem;
import ru.pda.nitro.App;
import ru.pda.nitro.bricks.ForumsBrick;


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
