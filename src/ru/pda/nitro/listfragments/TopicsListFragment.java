package ru.pda.nitro.listfragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.BaseColumns;
import android.support.v4.app.DialogFragment;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;

import ru.forpda.api.TopicApi;
import ru.forpda.interfaces.forum.IListItem;
import ru.forpda.interfaces.forum.Topic;
import ru.pda.nitro.App;
import ru.pda.nitro.R;
import ru.pda.nitro.adapters.TopicListAdapter;
import ru.pda.nitro.database.Contract;
import ru.pda.nitro.topicsview.TopicActivity;
import android.widget.*;
import android.widget.AdapterView.*;
import android.net.*;
import ru.pda.nitro.dialogs.*;
import ru.pda.nitro.bricks.*;
import ru.pda.nitro.*;


/**
 * Created by slartus on 12.01.14.
 * базовый класс для списков тем
 */
public abstract class TopicsListFragment extends BaseListFragment {

    public ArrayList<Topic> topics = new ArrayList<Topic>();
    public TopicListAdapter adapter;
    public static final int NAVIGATE_DIALOG_FRAGMENT = 1;
    private int selectedItem;


    @Override
    public ArrayList<? extends IListItem> getList() throws ParseException, IOException {
        return getTopicsList();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if (l < 0) return;
        Topic topic = topics.get(i);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(App.getInstance());
        String navigateAction = prefs.getString(getName() + ".navigate_action", null);
        if (navigateAction == null) {
            setSelectedItem(i);
            showNavigateDialog(topic);
            return;
        }
		
        showTopicActivity(i, topic, navigateAction);
    }
	

    public void setSelectedItem(int selectedItem) {
        this.selectedItem = selectedItem;
    }

    public int getSelectedItem() {
        return selectedItem;
    }

    public void onCreateContextMenu(android.view.ContextMenu contextMenu, android.view.View view,
                                    android.view.ContextMenu.ContextMenuInfo contextMenuInfo) {
        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.topic_context_menu, contextMenu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        
		AdapterContextMenuInfo info = (AdapterContextMenuInfo)
			item.getMenuInfo();
		Topic topic = topics.get(info.position);

        switch (item.getItemId()) {
            case R.id.navigate_getfirstpost:
                prepareShowTopicActivity(getSelectedItem(), topic, TopicApi.NAVIGATE_VIEW_FIRST_POST);
                break;
            case R.id.navigate_getlastpost:
                prepareShowTopicActivity(getSelectedItem(), topic, TopicApi.NAVIGATE_VIEW_LAST_POST);
                break;
            case R.id.navigate_getnewpost:
                prepareShowTopicActivity(getSelectedItem(), topic, TopicApi.NAVIGATE_VIEW_NEW_POST);
                break;
			case R.id.add_to_group:
				showGroopsDialog(topic);
				break;
        }
        return super.onContextItemSelected(item);
    }
	
	
    private void prepareShowTopicActivity(final int itemId, final Topic topic, final CharSequence navigateAction) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(App.getInstance());
        String savedNavigateAction = prefs.getString(getName() + ".navigate_action", null);

        if (!navigateAction.equals(savedNavigateAction)) {
            new AlertDialog.Builder(getActivity())
                    .setTitle(R.string.default_action)
                    .setCancelable(true)
                    .setMessage("Назначить действием по умолчанию?")
                    .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            saveDefaultAction(navigateAction);
                            showTopicActivity(itemId, topic, navigateAction);
                        }
                    })
                    .setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();

                            showTopicActivity(itemId, topic, navigateAction);
                        }
                    })
                    .create().show();
        } else {
            showTopicActivity(itemId, topic, navigateAction);
        }
    }

    private void showTopicActivity(int i, Topic topic, CharSequence navigateAction) {
        topic.setHasUnreadPosts(false);
        updateItem(i);
        adapter.notifyDataSetChanged();

        TopicActivity.show(getActivity(), topic.getId(), topic.getTitle(), getTitle(), navigateAction);
    }

    public abstract ArrayList<Topic> getTopicsList() throws ParseException, IOException;

    protected boolean getTopics() throws Throwable {
        return false;
    }

    public void updateItem(final int i) {
        handler = new Handler();
        handler.post(new Runnable() {

            @Override
            public void run() {
                Cursor cursor = getActivity().getContentResolver().query(Contract.Favorite.CONTENT_URI, null, null, null, Contract.Favorite.DEFAULT_SORT_ORDER);
                if(cursor.moveToPosition(i)){
                long l = cursor.getLong(cursor.getColumnIndexOrThrow(BaseColumns._ID));
                ContentValues cv = new ContentValues();
                cv.put(Contract.Favorite.hasUnreadPosts, false);

                getActivity().getContentResolver().update(ContentUris.withAppendedId(Contract.Favorite.CONTENT_URI, l), cv, null, null);
				}
				cursor.close();
            }
        });
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
        return prefs.getInt("_topics_out_count" + getName(), 0);
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
        if (getUri() != null) {
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

        if (getUri() != null)
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

    @Override
    public void onActivityResult(int requestCode, final int resultCode, final Intent data) {
        switch (requestCode) {
            case NAVIGATE_DIALOG_FRAGMENT:
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        CharSequence topicId = data.getExtras().getCharSequence(SelectNavigateDialogFragment.TOPIC_ID_KEY);
                        CharSequence navigateAction = data.getExtras().getCharSequence(SelectNavigateDialogFragment.NAVIGATE_ACTION_KEY);
                        if (resultCode == SelectNavigateDialogFragment.RESULT_ALWAYS) {
                            saveDefaultAction(navigateAction);
                        } else if (resultCode != SelectNavigateDialogFragment.RESULT_JUST_NOW) {
                            return;
                        }

                        Topic topic = topics.get(getSelectedItem());
                        showTopicActivity(getSelectedItem(), topic, navigateAction);

                    }
                }, 1000);
        }
    }

    private void saveDefaultAction(CharSequence navigateAction) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(App.getInstance());
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(getName() + ".navigate_action", navigateAction.toString());
        editor.commit();
    }
	
	void showGroopsDialog(Topic topic){
		FragmentTransaction ft = getActivity().getFragmentManager().beginTransaction();
        Fragment prev = getActivity().getFragmentManager().findFragmentByTag("dialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);


        DialogFragment dialogFrag = GroopsDialogFragment.newInstance(topic.getId(), topic.getTitle());
        dialogFrag.setTargetFragment(this, NAVIGATE_DIALOG_FRAGMENT);
        dialogFrag.show(getFragmentManager().beginTransaction(), "dialog");
		
	}

    void showNavigateDialog(Topic topic) {
        FragmentTransaction ft = getActivity().getFragmentManager().beginTransaction();
        Fragment prev = getActivity().getFragmentManager().findFragmentByTag("dialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);


        DialogFragment dialogFrag = SelectNavigateDialogFragment.newInstance(topic.getId());
        dialogFrag.setTargetFragment(this, NAVIGATE_DIALOG_FRAGMENT);
        dialogFrag.show(getFragmentManager().beginTransaction(), "dialog");
    }

    public static class SelectNavigateDialogFragment extends DialogFragment {
        public static final int RESULT_ALWAYS = 123;
        public static final int RESULT_JUST_NOW = 321;
        public static final String TOPIC_ID_KEY = "ru.pda.nitro.listfragments.TopicsListFragment.TOPIC_ID_KEY";
        public static final String NAVIGATE_ACTION_KEY = "ru.pda.nitro.listfragments.TopicsListFragment.NAVIGATE_ACTION_KEY";

        static SelectNavigateDialogFragment newInstance(CharSequence topicId) {
            SelectNavigateDialogFragment f = new SelectNavigateDialogFragment();

            // Supply num input as an argument.
            Bundle args = new Bundle();
            args.putCharSequence(TOPIC_ID_KEY, topicId);
            f.setArguments(args);

            return f;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final CharSequence topicId = getArguments().getCharSequence(TOPIC_ID_KEY);
            CharSequence[] titles = new CharSequence[]{getActivity().getString(R.string.navigate_getfirstpost),
                    getActivity().getString(R.string.navigate_getlastpost), getActivity().getString(R.string.navigate_getnewpost)};
            final CharSequence[] values = new CharSequence[]{TopicApi.NAVIGATE_VIEW_FIRST_POST,
                    TopicApi.NAVIGATE_VIEW_LAST_POST, TopicApi.NAVIGATE_VIEW_NEW_POST};
            final int[] selected = {2};
            return new AlertDialog.Builder(getActivity())
                    .setSingleChoiceItems(titles, selected[0], new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            selected[0] = i;
                        }
                    })
                    .setTitle(R.string.default_action)
                    .setPositiveButton("Всегда",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    Intent intent = getIntent(topicId, values[selected[0]]);
                                    getTargetFragment().onActivityResult(getTargetRequestCode(),
                                            RESULT_ALWAYS, intent);
                                }
                            }
                    )
                    .setNeutralButton("Только сейчас",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    Intent intent = getIntent(topicId, values[selected[0]]);
                                    getTargetFragment().onActivityResult(getTargetRequestCode(),
                                            RESULT_JUST_NOW, intent);
                                }
                            }
                    )
                    .create();
        }

        private Intent getIntent(CharSequence topicId, CharSequence navigateAction) {
            Intent intent = getActivity().getIntent();
            intent.putExtra(NAVIGATE_ACTION_KEY, navigateAction);
            intent.putExtra(TOPIC_ID_KEY, topicId);
            return intent;
        }
		
		
    }
	
	}
