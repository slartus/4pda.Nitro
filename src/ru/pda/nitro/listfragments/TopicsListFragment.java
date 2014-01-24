package ru.pda.nitro.listfragments;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.preference.PreferenceManager;
import android.provider.BaseColumns;
import android.view.View;
import android.widget.AdapterView;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;

import ru.forpda.interfaces.forum.IListItem;
import ru.forpda.interfaces.forum.Topic;
import ru.pda.nitro.adapters.TopicListAdapter;
import ru.pda.nitro.database.Contract;
import ru.pda.nitro.topicsview.TopicActivity;
import android.net.*;
import android.widget.*;


/**
 * Created by slartus on 12.01.14.
 * базовый класс для списков тем
 */
public abstract class TopicsListFragment extends BaseListFragment {
    
	public ArrayList<Topic> topics = new ArrayList<Topic>();
    public TopicListAdapter adapter;
    
    @Override
    public ArrayList<? extends IListItem> getList() throws ParseException, IOException {
        return getTopicsList();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if (l < 0) return;

        Topic topic = topics.get(i);
        topic.setHasUnreadPosts(false);
        updateItem(i);
        adapter.notifyDataSetChanged();

        TopicActivity.show(getActivity(), topic.getId(), topic.getTitle());

    }


    public abstract ArrayList<Topic> getTopicsList() throws ParseException, IOException;

    protected boolean getTopics() throws Throwable {

        return false;
    }

    public void updateItem(int i) {
        Cursor cursor = getActivity().getContentResolver().query(Contract.Favorite.CONTENT_URI, null, null, null, Contract.Favorite.DEFAULT_SORT_ORDER);
        cursor.moveToPosition(i);
        long l = cursor.getLong(cursor.getColumnIndexOrThrow(BaseColumns._ID));
        ContentValues cv = new ContentValues();
        cv.put(Contract.Favorite.hasUnreadPosts, false);

        getActivity().getContentResolver().update(ContentUris.withAppendedId(Contract.Favorite.CONTENT_URI, l), cv, null, null);

		cursor.close();
    }

    @Override
    public boolean inBackground() {
        try {
            return getTopics();
        } catch (Throwable e) {
        }

        return false;
    }

    @Override
    public void inExecute() {

        if (!getCount())
            showFooter(false);

        adapter.setData(topics);
        adapter.notifyDataSetChanged();

    }

    public int getFrom() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        return prefs.getInt("_topics_from" + getName(), 0);
    }

    public int getOutCount() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        return prefs.getInt("_topics_out_count"+ getName(), 0);
    }

    public boolean getCount() {
        if (getOutCount() == 0 | getFrom() < getOutCount()) {
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
		if(getUri() != null){
        Cursor cursor = getActivity().getContentResolver().query(getUri(), null, null, null, Contract.Favorite.DEFAULT_SORT_ORDER);
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
		}
        return topics;
    }

    public void setLocalData(ArrayList<Topic> topics) {

		if(getUri() != null)
        for (Topic topic : topics) {
            ContentValues cv = new ContentValues();
            cv.put(Contract.Favorite.description, topic.getDescription().toString());
            cv.put(Contract.Favorite.forumTitle, topic.getTitle().toString());
            cv.put(Contract.Favorite.hasUnreadPosts, topic.getHasUnreadPosts() ? 1 : 0);
            cv.put(Contract.Favorite.id, topic.getId().toString());
            cv.put(Contract.Favorite.lastAvtor, topic.getLastPostAuthor().toString());
            cv.put(Contract.Favorite.lastDate, topic.getLastPostDate().toString());
            cv.put(Contract.Favorite.title, topic.getTitle().toString());
            getActivity().getContentResolver().insert(getUri(), cv);
        }
    }

}
