package ru.pda.nitro.listfragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.BaseColumns;
import android.support.v4.app.DialogFragment;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;

import ru.forpda.api.TopicApi;
import ru.forpda.interfaces.forum.IListItem;
import ru.forpda.interfaces.forum.Topic;
import ru.pda.nitro.App;
import ru.pda.nitro.R;
import ru.pda.nitro.TabsViewActivity;
import ru.pda.nitro.adapters.TopicListAdapter;
import ru.pda.nitro.database.Contract;
import ru.pda.nitro.dialogs.ThemeOptionsDialogFragment;
import android.view.*;
import ru.forpda.interfaces.*;
import android.widget.*;
import ru.forpda.common.*;


/**
 * Created by slartus on 12.01.14.
 * базовый класс для списков тем
 */
public class TopicsListFragment extends BaseListFragment/* implements FragmentLifecycle*/{

	public final static String TOPICS_LIST_FRAGMENT = "TOPICS_LIST_FRAGMENT";
    private ArrayList<Topic> topics = new ArrayList<Topic>();
    private TopicListAdapter adapter;
    public static final int NAVIGATE_DIALOG_FRAGMENT = 1;
    private int selectedItem;
	private ListInfo listInfo;
//	private ListView listView;
	
	@Override
	public void onResumeFragment()
	{
		getPullToRefreshAttacher(listView);
		setProgressStatus();
		super.onResumeFragment();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{

        View v = inflater.inflate(R.layout.list_topic, container, false);
		listView = (ListView) v.findViewById(R.id.listViewTopic);
		
        return initialiseUi(v);
    }


	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
		
		listInfo = new ListInfo();
		listView.setOnItemClickListener(this);
		listView.setOnCreateContextMenuListener(this);
		listView.addFooterView(initialiseFooter());
		adapter = new TopicListAdapter(getActivity(), topics);
		listView.setAdapter(adapter);

		listView.setOnScrollListener(this);
		
		getLocalDataOnStart();
		getData();
	}

	@Override
	public void onScrollStateChanged(AbsListView p1, int p2)
	{
		// TODO: Implement this method
	}
	
    @Override
    public ArrayList<? extends IListItem> getList() throws ParseException, IOException {
        return getTopicsList(listInfo);
    }
	
	protected ArrayList<Topic> getTopicsList(ListInfo listInfo) throws ParseException, IOException{
		return null;
	}
	
	@Override
	public static String getClassName()
	{
		return TOPICS_LIST_FRAGMENT;
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


    private void setSelectedItem(int selectedItem) {
        this.selectedItem = selectedItem;
    }

    private int getSelectedItem() {
        return selectedItem;
    }


    public void onCreateContextMenu(android.view.ContextMenu contextMenu, android.view.View view,
                                    android.view.ContextMenu.ContextMenuInfo contextMenuInfo) {
        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.topic_context_menu, contextMenu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
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
            case R.id.options:
                showThemeOptionsDialog(topic.getId(), topic.getTitle());
                break;
        }
        return super.onContextItemSelected(item);
    }

    public void showThemeOptionsDialog(CharSequence topicId, CharSequence topicTitle) {
        DialogFragment dialog = ThemeOptionsDialogFragment.newInstance(topicId, topicTitle);
        dialog.show(getFragmentManager().beginTransaction(), "dialog");
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
        updateItem(getActivity(),i);
        updateAdapter(adapter);
        TabsViewActivity.show(getActivity(), topic.getId(), topic.getTitle(), getTitle(), navigateAction, getClassName());
    }
	
    public static void updateItem(final Context context, final int i) {
      Handler  handler = new Handler();
        handler.post(new Runnable() {

            @Override
            public void run() {
                Cursor cursor = null;
                try {
                    cursor = context.getContentResolver().query(Contract.Favorite.CONTENT_URI, null, null, null, Contract.Favorite.DEFAULT_SORT_ORDER);
                    if (cursor != null && cursor.moveToPosition(i)) {
                        long l = cursor.getLong(cursor.getColumnIndexOrThrow(BaseColumns._ID));
                        ContentValues cv = new ContentValues();
                        cv.put(Contract.Favorite.hasUnreadPosts, false);

                        context.getContentResolver().update(ContentUris.withAppendedId(Contract.Favorite.CONTENT_URI, l), cv, null, null);
                    }
                } finally {
                    if (cursor != null)
                        cursor.close();
                }


            }
        });
    }
	
    @Override
    public boolean inBackground() {
        try {
            if (isRefresh())
			{
				if(isLoadmore()){
					ArrayList<Topic> data = (ArrayList<Topic>) getList();
					if(data.size() > 0){
						for(Topic topic : data){
							topics.add(topic);
						}
						deleteAllLocalData(getActivity(), getUri());
						setLocalData(getActivity(),topics, getUri());
						return true;
					}
				}else{
					topics = (ArrayList<Topic>) getList();
					if (topics.size() > 0)
					{
						setOutCount(listInfo.getOutCount());
						deleteAllLocalData(getActivity(),getUri());
						setLocalData(getActivity(),topics, getUri());
						return true;
					}
				}
			}
			else
			{
				setFrom(getFrom());
				setOutCount(getOutCount());
				if (topics.size() == 0)
				{
					setDownload(true);
					topics = (ArrayList<Topic>) getList();
					if (topics.size() > 0)
					{
						setOutCount(listInfo.getOutCount());
						setLocalData(getActivity(),topics, getUri());
						return true;
					}
				}

				else
					setLoading(false);
				return true;
			}

			
        } catch (Throwable e) {

        }

        setFrom(getOld_from());
		return false;
    }

    @Override
    public void inExecute() {
        showFooter(getCount());
        setDataInAdapter(adapter, topics);
        updateAdapter(adapter);	
    }

    private int getFrom() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        return prefs.getInt("_topics_from_" + getName(), 0);
    }

    private int getOutCount() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        return prefs.getInt("_topics_out_count_" + getName(), 0);
    }

    private boolean getCount() {
        return getOutCount() == 0 | getFrom() < getOutCount();
    }
	
	@Override
	protected void setFrom(int from){
		this.from = from;
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
		SharedPreferences.Editor e = prefs.edit();
		e.putInt("_topics_from_" + getName(), from).commit();
		listInfo.setFrom(from);
	}



	private void setOutCount(int count){
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
		SharedPreferences.Editor e = prefs.edit();
		e.putInt("_topics_out_count_"+ getName(), count).commit();
	}

    @Override
    protected void setNextPage() {
		setOld_from(from);
        setFrom(topics.size());
    }

    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
		if ((firstVisibleItem + visibleItemCount) == totalItemCount && !loadMore && getCount() && !isLoading())
		{
			showFooter(false);

		}

    }

    public static ArrayList<Topic> getLocalTopicsData(Context context, Uri uri) {
        ArrayList<Topic> topics = new ArrayList<Topic>();
        if (uri != null) {
            Cursor cursor = null;
            try {
                cursor = context.getContentResolver().query(uri, null, null, null, Contract.Favorite.DEFAULT_SORT_ORDER);
                if (cursor != null && cursor.moveToFirst()) {
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
            } finally {
                if (cursor != null)
                    cursor.close();
            }

        }
        return topics;
    }

    public static void setLocalData(Context context, ArrayList<Topic> topics, Uri uri) {

        if (uri != null)
            for (Topic topic : topics) {
                ContentValues cv = new ContentValues();
                cv.put(Contract.Favorite.description, topic.getDescription().toString());
                cv.put(Contract.Favorite.forumTitle, topic.getTitle().toString());
                cv.put(Contract.Favorite.hasUnreadPosts, topic.getHasUnreadPosts() ? 1 : 0);
                cv.put(Contract.Favorite.id, topic.getId().toString());
                cv.put(Contract.Favorite.lastAvtor, topic.getLastPostAuthor().toString());
                cv.put(Contract.Favorite.lastDate, topic.getLastPostDate());
                cv.put(Contract.Favorite.title, topic.getTitle().toString());
                context.getContentResolver().insert(uri, cv);
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
                        if (data.getExtras() == null
                                || !data.getExtras().containsKey(SelectNavigateDialogFragment.NAVIGATE_ACTION_KEY))
                            return;
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
	
	@Override
	public void onResume()
	{
		super.onResume();
		

	}
	
	@Override
	public void onStart()
	{
		super.onStart();
		
	}
	
	@Override
	public void getLocalDataOnStart()
	{
		topics = getLocalTopicsData(getActivity(), getUri());
        setDataInAdapter(adapter, topics);
        updateAdapter(adapter);
	}
}
