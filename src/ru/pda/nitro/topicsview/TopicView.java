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

/**
 * Created by slinkin on 21.01.14.
 */
public class TopicView extends BaseFragment
implements LoaderManager.LoaderCallbacks<TopicResult> {


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // Prepare the loader.  Either re-connect with an existing one,
        // or start a new one.

        getLoaderManager().initLoader(0, getActivity().getIntent().getExtras(), this);

    }

    @Override
    public android.view.View onCreateView(android.view.LayoutInflater inflater,
                                          android.view.ViewGroup container, android.os.Bundle savedInstanceState) {
        View myFragmentView = inflater.inflate(R.layout.topic_view, container, false);

        WebView webView = (WebView) myFragmentView.findViewById(R.id.webview);
        webView.setWebViewClient(new MyWebViewClient());
        return initialiseUi(myFragmentView);
    }

    private WebView getWebView() {
        return (WebView) getView().findViewById(R.id.webview);
    }

    @Override
    public void getData() {
        showError(false);
        getLoaderManager().restartLoader(0, null, this);
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

            showTopic(TopicApi.normaTopicUrl(url));
            return true;
        }
    }

    private void showTopic(CharSequence topicUrl) {
        Bundle bundle = new Bundle();
        bundle.putCharSequence(TopicActivity.TOPIC_URL_KEY, topicUrl);
        getLoaderManager().restartLoader(0, bundle, this);
    }

    @Override
    public Loader<TopicResult> onCreateLoader(int i, Bundle bundle) {
        if (bundle == null) return null;

		showError(false);
        CharSequence topicUrl = null;
        if (bundle.containsKey(TopicActivity.TOPIC_ID_KEY)) {
            CharSequence topicId = bundle.getCharSequence(TopicActivity.TOPIC_ID_KEY);

            if (bundle.containsKey(TopicActivity.NAVIGATE_ACTION_KEY))
                topicUrl = TopicApi.getTopicUrl(topicId, bundle.getCharSequence(TopicActivity.NAVIGATE_ACTION_KEY));
            else
                topicUrl = TopicApi.getTopicUrl(topicId, null);
        } else if (bundle.containsKey(TopicActivity.TOPIC_URL_KEY))
            topicUrl = bundle.getCharSequence(TopicActivity.TOPIC_URL_KEY);
        return new TopicLoader(getActivity(), topicUrl);
    }

    @Override
    public void onLoadFinished(Loader<TopicResult> topicResultLoader, TopicResult topicResult) {
    //    if (getActivity() != null)
    //        getActivity().setTitle(topicResult.getTitle());
        if (topicResult.getBody() != null) {
            getWebView().loadDataWithBaseURL("http://4pda.ru/forum/", topicResult.getHtml().toString(), "text/html", "UTF-8", null);
            hideProgress();
        } else
            showError(true);
    }

    @Override
    public void onLoaderReset(Loader<TopicResult> topicResultLoader) {

    }

    private static class TopicLoader extends AsyncTaskLoader<TopicResult> {
        Throwable ex;
        private CharSequence mUrl;
        private TopicResult mData;

        public TopicLoader(Context context, CharSequence url) {
            super(context);

            mUrl = url;
        }

        @Override
        public TopicResult loadInBackground() {
            try {
                return TopicApi.getTopic(new HttpHelper(App.getInstance()), mUrl);
            } catch (Throwable e) {
                ex = e;
            }
            return null;
        }

        @Override
        public void deliverResult(TopicResult data) {
            if (isReset()) {

                releaseResources(data);

            }
            TopicResult oldData = mData;
            mData = data;
            if (isStarted()) {
                super.deliverResult(data);
            }

            if (oldData != null && oldData != data) {
                releaseResources(oldData);
            }
        }


        @Override
        protected void onStartLoading() {

            if (mData != null) {
                // Deliver any previously loaded data immediately.
                deliverResult(mData);
            }


            if (takeContentChanged() || mData == null) {
                // When the observer detects a change, it should call onContentChanged()
                // on the Loader, which will cause the next call to takeContentChanged()
                // to return true. If this is ever the case (or if the current data is
                // null), we force a new load.
                forceLoad();
            }
        }


        @Override
        protected void onStopLoading() {
            // Attempt to cancel the current load task if possible.
            cancelLoad();
        }

        @Override
        public void onCanceled(TopicResult data) {
            super.onCanceled(data);

            // At this point we can release the resources associated with 'apps'
            // if needed.
            releaseResources(data);
        }

        @Override
        protected void onReset() {
            onStopLoading();

            // At this point we can release the resources associated with 'mData'.
            if (mData != null) {
                releaseResources(mData);
                mData = null;
            }


        }

        private void releaseResources(TopicResult data) {
            data.setBody(null);
            // For a simple List, there is nothing to do. For something like a Cursor, we
            // would close it in this method. All resources associated with the Loader
            // should be released here.
        }
    }
}
