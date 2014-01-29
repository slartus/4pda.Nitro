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


/**
 * Created by slinkin on 21.01.14.
 */
public class TopicView extends Fragment
{

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		return inflater.inflate(R.layout.topic_layout, container, false);
	}


	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
		showTab(getActivity().getIntent().getStringExtra(TopicActivity.TOPIC_TITLE_KEY), getActivity().getIntent().getStringExtra(TopicActivity.TOPIC_URL_KEY));
	}

	public class TabListener<T extends Fragment> implements ActionBar.TabListener
	{
		private Fragment mFragment;
		private final Activity mActivity;
		private final CharSequence topicUrl;

		public TabListener(Activity activity, CharSequence url)
		{
			mActivity = activity;
			topicUrl = url;

		}

		public void onTabSelected(Tab tab, FragmentTransaction ft)
		{
			FragmentManager fm = getActivity().getSupportFragmentManager();
			if (mFragment == null)
			{
				Topic topic = new Topic();
				Bundle args = new Bundle();
				args.putCharSequence(TopicActivity.TOPIC_URL_KEY, topicUrl);

				mFragment = topic;

				mFragment.setArguments(args);

				fm.beginTransaction()
					.add(R.id.topic, mFragment)
					.commit();

			}
			else
			{
				fm.beginTransaction()
					.attach(mFragment)
					.commit();
			}

		}

		public void onTabUnselected(Tab tab, FragmentTransaction ft)
		{
			if (mFragment != null)
			{
				getActivity().getSupportFragmentManager().beginTransaction()
					.detach(mFragment)
					.commit();
			}
		}

		public void onTabReselected(Tab tab, FragmentTransaction ft)
		{
		}
	}

	private void addNewTab(String text, CharSequence url)
	{ 
		final ActionBar actionBar = getActivity().getActionBar(); 

		/* пока отложено, до прихода музы или просто прихода.
		 View tab = getActivity().getLayoutInflater().inflate(R.layout.action_bar_tab, null);
		 TextView textTab = (TextView)tab.findViewById(R.id.textViewTab);
		 textTab.setText(text);
		 ImageView imageTab = (ImageView)tab.findViewById(R.id.imageViewTab);
		 imageTab.setOnClickListener(new OnClickListener(){

		 @Override
		 public void onClick(View p1)
		 {
		 closeTab();
		 }
		 });*/

		actionBar
			.addTab(actionBar.newTab() 
					.setText(text)
					.setTag(url)
					.setTabListener(new TabListener(getActivity(), url)));
	}

	private void showTab(String text, CharSequence url)
	{ 
		final ActionBar actionBar = getActivity().getActionBar(); 
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		/*	for (int index = 0; index <actionBar.getNavigationItemCount(); index++) {
		 if (actionBar.getTabAt(index).getTag().equals(url)) 
		 { 
		 actionBar.setSelectedNavigationItem (index); 
		 return; 
		 } 
		 } */
		addNewTab(text, url); 
		actionBar.setSelectedNavigationItem(actionBar.getNavigationItemCount() - 1);
	}

	private void closeTab()
	{ 
		final ActionBar actionBar = getActivity().getActionBar(); 
		if (actionBar.getTabCount() > 1)
			actionBar.removeTab(actionBar.getSelectedTab()); 
	}

	/*	private void setTabText(String text){
	 final ActionBar actionBar = getActivity().getActionBar(); 

	 }*/


	public class Topic extends BaseFragment
	implements LoaderManager.LoaderCallbacks<TopicResult>/* , FragmentLifecycle*/
	{

		private CharSequence topicUrl = null;
		private CharSequence m_Id;
		private boolean attach = true;


		@Override
		public void onActivityCreated(Bundle savedInstanceState)
		{
			super.onActivityCreated(savedInstanceState);
			// Prepare the loader.  Either re-connect with an existing one,
			// or start a new one.
			Bundle bundle;
			if (getArguments().getString(TopicActivity.TOPIC_URL_KEY) != null)
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
					if(TopicApi.isTopicUrl(url) && TopicApi.getTopicId(url).equals(m_Id))
				showTopic(TopicApi.normaTopicUrl(url));
				else
				showNewTab(TopicApi.normaTopicUrl(url));
				return true;
			}
		}
		private void showNewTab(CharSequence topicUrl)
		{
			showTab("Загрузка...", topicUrl);

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
			
				m_Id = TopicApi.getTopicId(topicUrl);
				return new TopicLoader(getActivity(), topicUrl);
		}

		@Override
		public void onLoadFinished(Loader<TopicResult> topicResultLoader, TopicResult topicResult)
		{
			  if (getActivity() != null)
			     getActivity().setTitle(topicResult.getTitle());

			if (topicResult != null && topicResult.getBody() != null)
			{
				getWebView().loadDataWithBaseURL("http://4pda.ru/forum/", topicResult.getHtml().toString(), "text/html", "UTF-8", null);
			}
			else
				showStatus(true);

			setRefresh(false);
			setLoading(false);
			hideProgress();
			setProgress(false);
		}

		@Override
		public void onLoaderReset(Loader<TopicResult> topicResultLoader)
		{

		}
		@Override
		public void onResume()
		{
			super.onResume();
			getPullToRefreshAttacher(getWebView());
		}

		public void setAttach(boolean attach)
		{
			this.attach = attach;
		}

		public boolean isAttach()
		{
			return attach;
		}

		@Override
		public void onAttach(Activity activity)
		{
			super.onAttach(activity);
			setAttach(true);
		}

		@Override
		public void onDetach()
		{
			super.onDetach();
			setAttach(false);
		}


	}



}
