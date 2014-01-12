package ru.pda.application.listfragments;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;


import ru.pda.api.TopicsApi;
import ru.pda.application.App;
import ru.pda.application.bricks.FavoritesBrick;
import ru.pda.http.HttpHelper;
import ru.pda.interfaces.ListInfo;
import ru.pda.interfaces.forum.Topic;

/**
 * Created by slartus on 12.01.14.
 */
public class FavoritesListFragment extends TopicsListFragment {
    @Override
    public ArrayList<Topic> getTopicsList() throws ParseException, IOException {
        return TopicsApi.getFavorites(new HttpHelper(App.getInstance()), new ListInfo());
    }

    public String getTitle() {
        return FavoritesBrick.TITLE;
    }

    public String getName() {
        return FavoritesBrick.NAME;
    }


}
