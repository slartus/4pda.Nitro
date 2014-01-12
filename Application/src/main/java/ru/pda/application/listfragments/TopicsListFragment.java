package ru.pda.application.listfragments;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;

import ru.pda.interfaces.forum.IListItem;
import ru.pda.interfaces.forum.Topic;

/**
 * Created by slartus on 12.01.14.
 * базовый класс для списков тем
 */
public abstract class TopicsListFragment extends BaseListFragment{

    @Override
    public ArrayList<? extends IListItem> getList() throws ParseException, IOException {
        return getTopicsList();
    }

    public abstract ArrayList<Topic> getTopicsList() throws ParseException, IOException;
}
