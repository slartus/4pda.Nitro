package ru.pda.nitro.topicsview;
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
import ru.pda.nitro.*;
import android.content.*;
import ru.pda.nitro.listfragments.*;


public class TopicView extends BaseFragment
	implements LoaderManager.LoaderCallbacks<TopicResult>{

		@Override
		public String getName()
		{
			return "";
		}

		@Override
		public String getTitle()
		{
			return "";
		}


        public TopicView() {
        }

        private CharSequence topicUrl = null;
        private CharSequence topicTitle = null;
        private CharSequence topicId;
        private Bundle bundle = null;

        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);

            setHasOptionsMenu(true);
            setRetainInstance(true);
            getPullToRefreshAttacher(getWebView());

            if (getArguments().getString(TabsViewActivity.TOPIC_URL_KEY) != null | getArguments().getString(TabsViewActivity.TOPIC_ID_KEY) != null)
                bundle = getArguments();
            else
                bundle = getActivity().getIntent().getExtras();

            getLoaderManager().initLoader(0, bundle, this);
        }

        @Override
        public android.view.View onCreateView(android.view.LayoutInflater inflater,
                                              android.view.ViewGroup container, android.os.Bundle savedInstanceState) {
            View myFragmentView = inflater.inflate(R.layout.topic_view, container, false);

            WebView webView = (WebView) myFragmentView.findViewById(R.id.webview);
            webView.getSettings().setJavaScriptEnabled(true);
            webView.setWebViewClient(new MyWebViewClient());

            return initialiseUi(myFragmentView);
        }

        private WebView getWebView() {
            return (WebView) getView().findViewById(R.id.webview);
        }

        @Override
        public void getData() {
            refreshData();
        }

        @Override
        protected void refreshData() {
            super.refreshData();
            if (!isLoading()) {
                showTopic(topicUrl);
            } else
                setProgress(false);
        }

        class JsObject {
            @JavascriptInterface
            public void jumpToPage(int page) {

            }
        }

        private class MyWebViewClient extends WebViewClient {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, final String url) {
                if (!TopicApi.isTopicUrl(url))
                    return false;
                if (TopicApi.isTopicUrl(url) && TopicApi.getTopicId(url).equals(topicId))
                    showTopic(TopicApi.normaTopicUrl(url));
                else
                    showNewTab(TopicApi.normaTopicUrl(url));
                return true;
            }
        }

        private void showNewTab(CharSequence topicUrl) {
            TabsViewFragment.showTab(getActivity(),TopicView.class, getActivity().getResources().getString(R.string.downloads), topicUrl, null, topicUrl.toString());

        }

        private void showTopic(CharSequence topicUrl) {
            setRefresh(true);
            Bundle bundle = new Bundle();
            bundle.putCharSequence(TabsViewActivity.TOPIC_URL_KEY, topicUrl);
            getLoaderManager().restartLoader(0, bundle, this);
        }

        @Override
        public Loader<TopicResult> onCreateLoader(int i, Bundle bundle) {
            if (bundle == null) return null;

            setLoading(true);
            setProgress(true);

            if (bundle.containsKey(TabsViewActivity.TOPIC_ID_KEY)) {
                CharSequence topicId = bundle.getCharSequence(TabsViewActivity.TOPIC_ID_KEY);

                if (bundle.containsKey(TabsViewActivity.NAVIGATE_ACTION_KEY))
                    topicUrl = TopicApi.getTopicUrl(topicId, bundle.getCharSequence(TabsViewActivity.NAVIGATE_ACTION_KEY));
                else
                    topicUrl = TopicApi.getTopicUrl(topicId, null);
            } else if (bundle.containsKey(TabsViewActivity.TOPIC_URL_KEY))
                topicUrl = bundle.getCharSequence(TabsViewActivity.TOPIC_URL_KEY);

            topicId = TopicApi.getTopicId(topicUrl);
            refreshActionBarMenu(getActivity());

            return new TopicLoader(getActivity(), topicUrl);
        }

        @Override
        public void onLoadFinished(Loader<TopicResult> topicResultLoader, TopicResult topicResult) {

            if (getActivity() != null) {
                if (topicResult != null) {
                    topicTitle = topicResult.getTitle().toString();
                    final ActionBar actionBar = getActivity().getActionBar();

                    for (int index = 0; index < actionBar.getNavigationItemCount(); index++) {
                        Object tag = actionBar.getTabAt(index).getTag();
                        if (tag.equals(TabsViewFragment.DEFAULT_TAG)) {
                            actionBar.getTabAt(index).setTag(topicUrl);
                        }
                        if (tag.equals(topicUrl)) {
                            actionBar.getTabAt(index).setText(BaseState.getSpannable(getActivity(), topicTitle));
                        }
                    }

                    if (topicResult.getBody() != null) {
                        getWebView().loadDataWithBaseURL("http://4pda.ru/forum/", topicResult.getHtml().toString(), "text/html", "UTF-8", null);
                    } else
                        showStatus(linearProgress, linearError, true);

                } else
                    showStatus(linearProgress, linearError, true);

                hideProgress();
                setRefresh(false);
                setLoading(false);
                setProgress(false);

                refreshActionBarMenu(getActivity());

            }
        }


        @Override
        public void onLoaderReset(Loader<TopicResult> topicResultLoader) {

        }

		@Override
		public boolean onOptionsItemSelected(MenuItem item)
		{
			switch(item.getItemId()){
				case R.id.options:
					showThemeOptionsDialog(topicId);
			}
			return super.onOptionsItemSelected(item);
		}
		
	public void showThemeOptionsDialog(CharSequence topicId) {
		DialogFragment dialog = ThemeOptionsDialogFragment.newInstance(topicId, topicTitle);
		dialog.show(getFragmentManager().beginTransaction(), "dialog");
	}


        @Override
        public void onResume() {
            super.onResume();
            setProgress(isLoading());
        }
		
    }
