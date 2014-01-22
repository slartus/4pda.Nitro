package ru.pda.nitro.topicsview;

import android.app.Fragment;
import android.app.LoaderManager;
import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.Loader;
import android.os.Bundle;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import ru.forpda.api.TopicApi;
import ru.forpda.api.TopicResult;
import ru.forpda.http.HttpHelper;
import ru.pda.nitro.App;
import ru.pda.nitro.R;

/**
 * Created by slinkin on 21.01.14.
 */
public class TopicView extends Fragment
        implements LoaderManager.LoaderCallbacks<TopicResult> {
    public static final String TOPIC_ID_KEY = "TopicIdKey";

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // Prepare the loader.  Either re-connect with an existing one,
        // or start a new one.

        getLoaderManager().initLoader(0, savedInstanceState, this);
    }

    @Override
    public android.view.View onCreateView(android.view.LayoutInflater inflater,
                                          android.view.ViewGroup container, android.os.Bundle savedInstanceState) {
        View myFragmentView = inflater.inflate(R.layout.topic_view, container, false);


        return myFragmentView;
    }

    private WebView getWebView() {
        return (WebView) getView().findViewById(R.id.webview);
    }


    @Override
    public Loader<TopicResult> onCreateLoader(int i, Bundle bundle) {
        Bundle extras = this.getActivity().getIntent().getExtras();
        return new TopicLoader(getActivity(), "http://4pda.ru/forum/index.php?showtopic=" + extras.getString(TOPIC_ID_KEY));
    }

    @Override
    public void onLoadFinished(Loader<TopicResult> topicResultLoader, TopicResult topicResult) {
        getWebView().loadDataWithBaseURL("http://4pda.ru/forum/", topicResult.getHtml().toString(), "text/html", "UTF-8", null);
    }

    @Override
    public void onLoaderReset(Loader<TopicResult> topicResultLoader) {

    }

    class JsObject {
        @JavascriptInterface
        public void jumpToPage(int page) {

        }
    }

    private static class TopicLoader extends AsyncTaskLoader<TopicResult> {
        Throwable ex;
        private String mUrl;
        private TopicResult mData;

        public TopicLoader(Context context, String url) {
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
