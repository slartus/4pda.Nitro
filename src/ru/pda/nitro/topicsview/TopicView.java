package ru.pda.nitro.topicsview;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import ru.forpda.api.TopicApi;
import ru.forpda.api.TopicResult;
import ru.forpda.http.HttpHelper;
import ru.pda.nitro.App;
import ru.pda.nitro.BaseFragment;
import ru.pda.nitro.R;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ActionBar.Tab;
import android.app.ActionBar;
import android.app.Activity;
import ru.pda.nitro.listfragments.*;
import android.view.*;
import android.widget.*;
import android.view.View.*;
import ru.pda.nitro.database.*;
import android.database.*;
import android.util.*;
import android.os.*;
import android.net.*;
import android.provider.*;
import android.content.*;
import android.support.v4.app.*;
import ru.pda.nitro.dialogs.*;


/**
 * Created by slinkin on 21.01.14.
 */
public class TopicView extends Fragment
{
	private final static String TOPIC_BASEID_KEY = "ru.pda.nitro.topicsview.TopicView.TOPIC_BASEID_KEY";
	private final String DEFAULT_TAG = "tag";
	private Handler handler = new Handler();
	private Uri mUri;
	private boolean groop = false;

	public void setGroop(boolean menuGroop)
	{
		this.groop = menuGroop;
	}

	public boolean isGroop()
	{
		return groop;
	}


	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
		final ActionBar ab = getActivity().getActionBar();
		ab.setDisplayHomeAsUpEnabled(true);
		ab.setDisplayShowHomeEnabled(true);
		ab.setDisplayShowTitleEnabled(true);
		if(getActivity().getIntent().getStringExtra(TopicActivity.TOPIC_ID_KEY ) != null)
		showTab(getActivity().getIntent().getStringExtra(TopicActivity.TOPIC_TITLE_KEY), getActivity().getIntent().getStringExtra(TopicActivity.TOPIC_URL_KEY),null, DEFAULT_TAG);
		else
			showGroup();
	}
	
	private void showGroup(){
		setGroop(true);
		handler.post(new Runnable(){

				@Override
				public void run()
				{
				
					mUri = getActivity().getIntent().getParcelableExtra(TopicActivity.TOPIC_GROOP_URI_KEY);
					Cursor cursor = getActivity().getContentResolver().query(mUri, null, null, null, Contract.Groop.DEFAULT_SORT_ORDER);

					if(cursor.moveToFirst()){
						final ActionBar actionBar = getActivity().getActionBar(); 
						actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

						do{
							CharSequence topicId = cursor.getString(cursor.getColumnIndexOrThrow(Contract.Groop.id));
							String text =	cursor.getString(cursor.getColumnIndexOrThrow(Contract.Groop.title));
							long baseId = cursor.getLong(cursor.getColumnIndexOrThrow(BaseColumns._ID));

							addNewTab(text, null, topicId, baseId, topicId.toString()); 

						}while(cursor.moveToNext());
				
						cursor.close();
					}else
						cursor.close();
				}
			});
		
	}

	public class TabListener<T extends Fragment> implements ActionBar.TabListener
	{
		private Fragment mFragment;
		private final Activity mActivity;
		private final CharSequence topicUrl;
		private final CharSequence topicId;
		private final String mTag;
		private final long baseId;
		

		public TabListener(Activity activity, CharSequence url, CharSequence id, long lid, String tag)
		{
			mActivity = activity;
			topicUrl = url;
			topicId = id;
			baseId = lid;
			mTag = tag;
		}

		public void onTabSelected(Tab tab, FragmentTransaction ft)
		{
			FragmentManager fm = getActivity().getSupportFragmentManager();
			if (mFragment == null)
			{
				
				mFragment = new Topic();
				Bundle args = new Bundle();
				
				if(topicUrl != null)
				args.putCharSequence(TopicActivity.TOPIC_URL_KEY, topicUrl);
				
				if(topicId != null){
				args.putCharSequence(TopicActivity.TOPIC_ID_KEY, topicId);
				args.putCharSequence(TopicActivity.NAVIGATE_ACTION_KEY, TopicApi.NAVIGATE_VIEW_NEW_POST);
				args.putLong(TOPIC_BASEID_KEY, baseId);
				}
				

				mFragment.setArguments(args);

				fm.beginTransaction()
					.add(R.id.topic, mFragment, mTag)
					.commit();

			}
			else
			{
				if(tab.getText().equals(R.string.stoping))
					tab.setText(R.string.downloads);
				
				fm.beginTransaction()
					.attach(mFragment)
					.commit();
			}

		}

		public void onTabUnselected(Tab tab, FragmentTransaction ft)
		{
			if (getActivity() != null && mFragment != null)
			{
				if(tab.getText().equals(R.string.downloads))
					tab.setText(R.string.stoping);
				
					getActivity().getSupportFragmentManager().beginTransaction()
					.detach(mFragment)
					.commit();
			}
		}

		public void onTabReselected(Tab tab, FragmentTransaction ft)
		{
		}
	}

	private void addNewTab(String text, CharSequence topicUrl, CharSequence topicId, long baseId, String tag)
	{ 
		final ActionBar actionBar = getActivity().getActionBar(); 

		actionBar
			.addTab(actionBar.newTab() 
					.setText(text)
					.setTag(tag)
					.setTabListener(new TabListener(getActivity(), topicUrl, topicId, baseId, tag)));
	}

	private void showTab(String text, CharSequence url, CharSequence id, String tag)
	{ 
		final ActionBar actionBar = getActivity().getActionBar(); 
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);


		for (int index = 0; index < actionBar.getNavigationItemCount(); index++)
		{
			if (actionBar.getTabAt(index).getTag().equals(tag)) 
			{ 
				actionBar.setSelectedNavigationItem(index); 
				return; 
			} 
		} 
		addNewTab(text, url, id, -1, tag); 
		actionBar.setSelectedNavigationItem(actionBar.getNavigationItemCount() - 1);
	}

	private void closeTab()
	{ 
	if(getActivity() != null){
		final ActionBar actionBar = getActivity().getActionBar(); 
		if (actionBar.getTabCount() > 1)
			actionBar.removeTab(actionBar.getSelectedTab()); 
			else{
		//	actionBar.removeTab(actionBar.getSelectedTab()); 
			getActivity().finish();
				}
	}
	}
	
	private void removeFromGroup(long id){
		getActivity().getContentResolver().delete(ContentUris.withAppendedId(mUri, id), null, null);
//		closeTab();
	}
	
	private void showGroopsDialog(CharSequence title, CharSequence id){

        DialogFragment dialogFrag = GroopsDialogFragment.newInstance(id, title);
        dialogFrag.show(getFragmentManager().beginTransaction(), "dialog");

	}
	

	public class Topic extends BaseFragment
	implements LoaderManager.LoaderCallbacks<TopicResult>/* , FragmentLifecycle*/
	{

		private CharSequence topicUrl = null;
		private CharSequence topicTitle = null;
		private CharSequence topicId;
		private long topicBaseId = -1;
		private Bundle bundle = null;
		
		@Override
		public void onActivityCreated(Bundle savedInstanceState)
		{
			super.onActivityCreated(savedInstanceState);
		
			setHasOptionsMenu(true);
			setRetainInstance(true);
			getPullToRefreshAttacher(getWebView());
			
			if (getArguments().getString(TopicActivity.TOPIC_URL_KEY) != null | getArguments().getString(TopicActivity.TOPIC_ID_KEY) != null
			)
				bundle = getArguments();
			else
				bundle = getActivity().getIntent().getExtras();

			getLoaderManager().initLoader(0, bundle, this);
	}

		@Override
		public android.view.View onCreateView(android.view.LayoutInflater inflater,
											  android.view.ViewGroup container, android.os.Bundle savedInstanceState)
		{
			View myFragmentView = inflater.inflate(R.layout.topic_view, container, false);

			WebView webView = (WebView) myFragmentView.findViewById(R.id.webview);
			webView.getSettings().setJavaScriptEnabled(true);
			webView.setWebViewClient(new MyWebViewClient());

			return initialiseUi(myFragmentView);
		}

		private WebView getWebView()
		{
			return (WebView) getView().findViewById(R.id.webview);
		}

		@Override
		public void getData()
		{
			refreshData();
		}

		@Override
		protected void refreshData()
		{
			super.refreshData();
			if (!isLoading())
			{
				showTopic(topicUrl);
			}
			else
				setProgress(false);
		}

		class JsObject
		{
			@JavascriptInterface
			public void jumpToPage(int page)
			{

			}
		}

		private class MyWebViewClient extends WebViewClient
		{
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, final String url)
			{
				if (!TopicApi.isTopicUrl(url))
					return false;
				if (TopicApi.isTopicUrl(url) && TopicApi.getTopicId(url).equals(topicId))
					showTopic(TopicApi.normaTopicUrl(url));
				else
					showNewTab(TopicApi.normaTopicUrl(url));
				return true;
			}
		}
		private void showNewTab(CharSequence topicUrl)
		{
			showTab(getActivity().getResources().getString(R.string.downloads), topicUrl, null, topicUrl.toString());

		}

		private void showTopic(CharSequence topicUrl)
		{
			setRefresh(true);
			Bundle bundle = new Bundle();
			bundle.putCharSequence(TopicActivity.TOPIC_URL_KEY, topicUrl);
			getLoaderManager().restartLoader(0, bundle, this);
		}

		@Override
		public Loader<TopicResult> onCreateLoader(int i, Bundle bundle)
		{
			if (bundle == null) return null;

			setLoading(true);
			setProgress(true);

			if (bundle.containsKey(TopicActivity.TOPIC_ID_KEY))
			{
				CharSequence topicId = bundle.getCharSequence(TopicActivity.TOPIC_ID_KEY);

				if (bundle.containsKey(TopicActivity.NAVIGATE_ACTION_KEY))
					topicUrl = TopicApi.getTopicUrl(topicId, bundle.getCharSequence(TopicActivity.NAVIGATE_ACTION_KEY));
				else
					topicUrl = TopicApi.getTopicUrl(topicId, null);
			}
			else if (bundle.containsKey(TopicActivity.TOPIC_URL_KEY))
				topicUrl = bundle.getCharSequence(TopicActivity.TOPIC_URL_KEY);

			topicId = TopicApi.getTopicId(topicUrl);
			RefreshMenu.refreshActionBarMenu(getActivity());
			
			return new TopicLoader(getActivity(), topicUrl);
		}

		@Override
		public void onLoadFinished(Loader<TopicResult> topicResultLoader, TopicResult topicResult)
		{
			
			if(getActivity() != null){
				if(topicResult != null){
					topicTitle = topicResult.getTitle().toString();
			final ActionBar actionBar = getActivity().getActionBar();
			
				for (int index = 0; index < actionBar.getNavigationItemCount(); index++)
				{
					Object tag = actionBar.getTabAt(index).getTag();
					if (tag.equals(DEFAULT_TAG))
					{
						actionBar.getTabAt(index).setTag(topicUrl);
					}
					if (tag.equals(topicUrl)) 
					{ 
						actionBar.getTabAt(index).setText(topicTitle);
					} 
				}

			if (topicResult.getBody() != null)
			{
				getWebView().loadDataWithBaseURL("http://4pda.ru/forum/", topicResult.getHtml().toString(), "text/html", "UTF-8", null);
			}
			else
				showStatus(true);
			
			}else
			showStatus(true);
			
			hideProgress();
			setRefresh(false);
			setLoading(false);
			setProgress(false);
			
			RefreshMenu.refreshActionBarMenu(getActivity());
				
			}
		}
		

		@Override
		public void onLoaderReset(Loader<TopicResult> topicResultLoader)
		{

		}

		@Override
		public void onPrepareOptionsMenu(Menu menu)
		{
			super.onPrepareOptionsMenu(menu);
			menu.findItem(R.id.menu_add_groops).setVisible(false);
			menu.findItem(R.id.menu_remove_groops).setVisible(false);
			
			topicBaseId = GroopsDialogFragment.isAddGroup(getActivity(),topicId);
			
			if(isGroop()){
				if(topicBaseId > 0 && mUri != null){
					menu.findItem(R.id.menu_add_groops).setVisible(false);
					menu.findItem(R.id.menu_remove_groops).setVisible(true);
					
			}else{
				menu.findItem(R.id.menu_add_groops).setVisible(true);
				menu.findItem(R.id.menu_remove_groops).setVisible(false);
			}
			}else if(topicTitle != null && topicBaseId < 0){
				menu.findItem(R.id.menu_add_groops).setVisible(true);
				}
		}
		
		

		@Override
		public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
		{
			super.onCreateOptionsMenu(menu, inflater);
			inflater.inflate(R.menu.topic_view, menu);
		}

		@Override
		public boolean onOptionsItemSelected(MenuItem item)
		{
			switch(item.getItemId()){
				case android.R.id.home:
					getActivity().finish();
					break;
				case R.id.close_tab:
					closeTab();
					break;
				case R.id.menu_remove_groops:
					removeFromGroup(topicBaseId);
					break;
				case R.id.menu_add_groops:
					showGroopsDialog(topicTitle, topicId);
					break;
				case R.id.options:
					showThemeOptionsDialog(topicId);
					break;
				
			}
			return super.onOptionsItemSelected(item);
		}
		
		public void showThemeOptionsDialog(CharSequence topicId){
			DialogFragment dialog = new ThemeOptionsDialogFragment().newInstance(topicId);
			dialog.show(getFragmentManager().beginTransaction(), "dialog");
		}
		
		
		@Override
		public void onResume()
		{
			super.onResume();
			setProgress(isLoading());
		}
		

	}



}
