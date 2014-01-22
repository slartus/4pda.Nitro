package ru.pda.nitro.webview;

import android.content.Context;
import android.webkit.WebView;

/**
 * Базовый класс для всех WebView
 */
public class WebViewBase extends WebView {
    public WebViewBase(Context context) {
        super(context);
        init();
    }

    public WebViewBase(android.content.Context context, android.util.AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public WebViewBase(android.content.Context context, android.util.AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {

        getSettings().setJavaScriptEnabled(true);
    }
}
