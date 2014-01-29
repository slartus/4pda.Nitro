package ru.pda.nitro.topicsview;
import ru.forpda.api.TopicApi;
import android.support.v4.content.AsyncTaskLoader;
import android.content.Context;
import ru.forpda.http.HttpHelper;
import ru.pda.nitro.App;
import ru.forpda.api.TopicResult;

public class TopicLoader extends AsyncTaskLoader<TopicResult> {
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
