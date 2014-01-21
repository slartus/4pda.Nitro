package ru.pda.nitro.topicsview;

import android.content.Context;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.LinearLayout;

/**
 * Created by slinkin on 21.01.14.
 */
public class TopicView extends LinearLayout {
    public TopicView(Context context) {
        super(context);
        WebView topicWebView = getWebView(context);
        addView(topicWebView);
    }

    private TopicWebView getWebView(Context context) {
        TopicWebView topicWebView = new TopicWebView(context);
        topicWebView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        topicWebView.addJavascriptInterface(new JsObject(), "injectedObject");
        return topicWebView;
    }

    class JsObject {

    }
}
