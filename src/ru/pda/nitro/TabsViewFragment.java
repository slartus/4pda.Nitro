package ru.pda.nitro;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.BaseColumns;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import ru.forpda.api.TopicApi;
import ru.forpda.api.infos.TopicResult;
import ru.pda.nitro.BaseFragment;
import ru.pda.nitro.R;
import ru.pda.nitro.database.Contract;
import ru.pda.nitro.dialogs.ThemeOptionsDialogFragment;
import ru.pda.nitro.topicsview.*;
import android.content.*;
import android.support.v4.app.*;
import ru.pda.nitro.listfragments.*;
import ru.pda.nitro.dialogs.*;


public class TabsViewFragment extends Fragment
{
	public final static int QUICK_START_DIALOG_FRAGMENT = 100500;
	private final static String TOPIC_BASEID_KEY = "ru.pda.nitro.topicsview.TopicView.TOPIC_BASEID_KEY";
    public final static String DEFAULT_TAG = "tag";
    private Handler handler = new Handler();
    private Uri mUri;
    private boolean groop = false;

    public void setGroop(boolean menuGroop) {
        this.groop = menuGroop;
    }

    public boolean isGroop() {
        return groop;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
		setRetainInstance(true);
		final ActionBar ab = getActivity().getActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setDisplayShowHomeEnabled(true);
        ab.setDisplayShowTitleEnabled(true);
        if (getActivity().getIntent().getStringExtra(TabsViewActivity.TOPIC_ID_KEY) != null)
            showTab(getActivity(), getCurrentClass(), getActivity().getIntent().getStringExtra(TabsViewActivity.TOPIC_TITLE_KEY), getActivity().getIntent().getStringExtra(TabsViewActivity.TOPIC_URL_KEY), null, DEFAULT_TAG);
        else
            showGroup();
    }
	
	private Class<? extends BaseFragment> getCurrentClass(){
		String classGroupName = getActivity().getIntent().getStringExtra(TabsViewActivity.TOPIC_ACTIVITY_CLASS_NAME_KEY);
		switch(classGroupName){
			case TopicsListFragment.TOPICS_LIST_FRAGMENT:
				return TopicView.class;
		}
		return null;
	}

    private void showGroup() {
        setGroop(true);
        handler.post(new Runnable() {

				@Override
				public void run() {

					mUri = getActivity().getIntent().getParcelableExtra(TabsViewActivity.TOPIC_GROOP_URI_KEY);
					Cursor cursor = null;
					try {
						cursor = getActivity().getContentResolver().query(mUri, null, null, null, Contract.Groop.DEFAULT_SORT_ORDER);

						if (cursor != null && cursor.moveToFirst()) {
							final ActionBar actionBar = getActivity().getActionBar();
							actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

							do {
								CharSequence topicId = cursor.getString(cursor.getColumnIndexOrThrow(Contract.Groop.id));
								String text = cursor.getString(cursor.getColumnIndexOrThrow(Contract.Groop.title));
								long baseId = cursor.getLong(cursor.getColumnIndexOrThrow(BaseColumns._ID));

								addNewTab(getActivity(),TopicView.class, text, null, topicId, baseId, topicId.toString());

							} while (cursor.moveToNext());


						}
					} finally {
						if (cursor != null)
							cursor.close();
					}

				}
			});

    }

    public static class TabListener<T extends Fragment> implements ActionBar.TabListener {
        private Class<T>mClass;
		private Fragment mFragment;
        private final FragmentActivity mActivity;
        private final CharSequence topicUrl;
        private final CharSequence topicId;
        private final String mTag;
        private final long baseId;


        public TabListener(FragmentActivity activity,Class<T>clz, CharSequence url, CharSequence id, long lid, String tag) {
            mActivity = activity;
			mClass = clz;
            topicUrl = url;
            topicId = id;
            baseId = lid;
            mTag = tag;
        }

        public void onTabSelected(Tab tab, FragmentTransaction ft) {
            FragmentManager fm = mActivity.getSupportFragmentManager();
			if ( mFragment == null) {
				mFragment = Fragment. instantiate( mActivity , mClass .getName ());
				Bundle args = new Bundle();

                if (topicUrl != null)
                    args.putCharSequence(TabsViewActivity.TOPIC_URL_KEY, topicUrl);

                if (topicId != null) {
                    args.putCharSequence(TabsViewActivity.TOPIC_ID_KEY, topicId);
                    args.putCharSequence(TabsViewActivity.NAVIGATE_ACTION_KEY, TopicApi.NAVIGATE_VIEW_NEW_POST);
                    args.putLong(TOPIC_BASEID_KEY, baseId);
                }
                mFragment.setArguments(args);
				
				fm.beginTransaction()
					.add(R.id.topic, mFragment, mTag)
					.commit();
			} else {
				if (tab.getText().equals(R.string.stoping))
                    tab.setText(BaseState.getSpannable(mActivity, mActivity.getResources().getString( R.string.downloads)));
				
				fm.beginTransaction()
					.attach(mFragment)
					.commit();
			}

        }

        public void onTabUnselected(Tab tab, FragmentTransaction ft) {
            if (mActivity != null && mFragment != null) {
                if (tab.getText().equals(R.string.downloads))
                    tab.setText(BaseState.getSpannable(mActivity, mActivity.getResources().getString( R.string.stoping)));

                mActivity.getSupportFragmentManager().beginTransaction()
					.detach(mFragment)
					.commit();
            }
        }

        public void onTabReselected(Tab tab, FragmentTransaction ft) {
        }
    }

    private static void addNewTab(FragmentActivity activity, Class clz, String text, CharSequence topicUrl, CharSequence topicId, long baseId, String tag) {
        final ActionBar actionBar = activity.getActionBar();

        actionBar
			.addTab(actionBar.newTab()
					.setText(BaseState.getSpannable(activity, text))
					.setTag(tag)
					.setTabListener(new TabListener(activity,clz , topicUrl, topicId, baseId, tag)));
    }

    public static void showTab(FragmentActivity activity,Class clz, String text, CharSequence url, CharSequence id, String tag) {
        final ActionBar actionBar = activity.getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);


        for (int index = 0; index < actionBar.getNavigationItemCount(); index++) {
            if (actionBar.getTabAt(index).getTag().equals(tag)) {
                actionBar.setSelectedNavigationItem(index);
                return;
            }
        }
        addNewTab(activity,clz,text, url, id, -1, tag);
        actionBar.setSelectedNavigationItem(actionBar.getNavigationItemCount() - 1);
    }

    public static void closeTab(Activity activity) {
        if (activity != null) {
            final ActionBar actionBar = activity.getActionBar();
            if (actionBar.getTabCount() > 1)
                actionBar.removeTab(actionBar.getSelectedTab());
            else {
               activity.finish();
            }
        }
    }
	
	private void showQuickStartDialog() {
      


        DialogFragment dialogFrag = QuickStartDialogFragment.newInstance();
        dialogFrag.setTargetFragment(this, QUICK_START_DIALOG_FRAGMENT);
        dialogFrag.show(getFragmentManager().beginTransaction(), "dialog");
    }
	
	@Override
    public void onActivityResult(int requestCode, final int resultCode, final Intent data) {
        switch (requestCode) {
            case QUICK_START_DIALOG_FRAGMENT:
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {

						@Override
						public void run() {
							
						}
					}, 1000);
        }
    }
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.topic_view, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				getActivity().finish();
				break;
			case R.id.close_tab:
				closeTab(getActivity());
				break;
			case R.id.quickstart:
				showQuickStartDialog();
				break;

		}
		return super.onOptionsItemSelected(item);
	}
}
