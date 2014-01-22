package ru.pda.nitro.listfragments;

import android.content.ContentValues;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.preference.PreferenceManager;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;

import ru.forpda.interfaces.forum.IListItem;
import ru.forpda.interfaces.forum.Topic;
import ru.pda.nitro.adapters.TopicListAdapter;
import ru.pda.nitro.database.Contract;
import ru.pda.nitro.topicsview.TopicActivity;


/**
 * Created by slartus on 12.01.14.
 * базовый класс для списков тем
 */
public abstract class TopicsListFragment extends BaseListFragment {
    public ArrayList<Topic> topics = new ArrayList<Topic>();
    public TopicListAdapter adapter;
    public Task task;


    @Override
    public ArrayList<? extends IListItem> getList() throws ParseException, IOException {
        return getTopicsList();
    }

    @Override
    public void onItemClick(android.widget.AdapterView<?> adapterView, android.view.View view,
                            int i, long l) {
        if (l < 0) return;
        Object item = adapter.getItem((int) l);
        if (item == null) return;
        Topic topic = (Topic) item;
        TopicActivity.show(getActivity(), topic.getId(), topic.getTitle());
    }

    public abstract ArrayList<Topic> getTopicsList() throws ParseException, IOException;

    protected boolean getTopics() throws Throwable {

        return false;
    }

    @Override
    public boolean inBackground() {
        try {
            return getTopics();
        } catch (Throwable e) {
        }

        return false;
        // TODO: Implement this method
    }

    @Override
    public void inExecute() {

        if (!getCount())
            showFooter(false);

        adapter.setData(topics);
        adapter.notifyDataSetChanged();
        // TODO: Implement this method
    }

    public int getFrom() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        return prefs.getInt("_topics_from", 0);
    }

    public int getOutCount() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        return prefs.getInt("_topics_out_count", 0);
    }

    public boolean getCount() {
        if (getOutCount() == 0 | getFrom() <= getOutCount()) {
            return true;
        }
        return false;
    }


    public void getData() {
        if (!isLoading()) {

            task = new Task();
            task.execute();
        } else
            setProgress(false);

    }

    public ArrayList<Topic> getLocalData() {
        topics = new ArrayList<Topic>();
        Cursor cursor = getActivity().getContentResolver().query(Contract.Favorite.CONTENT_URI, null, null, null, Contract.Favorite.DEFAULT_SORT_ORDER);
        if (cursor.moveToFirst()) {
            do {
                Topic topic = new Topic(null, null);
                topic.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(Contract.Favorite.description)));
                topic.setForumTitle(cursor.getString(cursor.getColumnIndexOrThrow(Contract.Favorite.forumTitle)));
                topic.setHasUnreadPosts(cursor.getInt(cursor.getColumnIndexOrThrow(Contract.Favorite.hasUnreadPosts)) == 1 ? true : false);
                topic.setId(cursor.getString(cursor.getColumnIndexOrThrow(Contract.Favorite.id)));
                topic.setLastPostAuthor(cursor.getString(cursor.getColumnIndexOrThrow(Contract.Favorite.lastAvtor)));
                topic.setLastPostDate(cursor.getString(cursor.getColumnIndexOrThrow(Contract.Favorite.lastDate)));
                topic.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(Contract.Favorite.title)));
                topics.add(topic);
            } while (cursor.moveToNext());

        }
        cursor.close();
        return topics;
    }

    public void setLocalData(ArrayList<Topic> topics) {

        for (Topic topic : topics) {
            ContentValues cv = new ContentValues();
            cv.put(Contract.Favorite.description, topic.getDescription().toString());
            cv.put(Contract.Favorite.forumTitle, topic.getTitle().toString());
            cv.put(Contract.Favorite.hasUnreadPosts, topic.getHasUnreadPosts() ? 1 : 0);
            cv.put(Contract.Favorite.id, topic.getId().toString());
            cv.put(Contract.Favorite.lastAvtor, topic.getLastPostAuthor().toString());
            cv.put(Contract.Favorite.lastDate, topic.getLastPostDate().toString());
            cv.put(Contract.Favorite.title, topic.getTitle().toString());
            getActivity().getContentResolver().insert(Contract.Favorite.CONTENT_URI, cv);
        }
    }
}
