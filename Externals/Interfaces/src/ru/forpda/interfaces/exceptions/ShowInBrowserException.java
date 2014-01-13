package ru.forpda.interfaces.exceptions;

import java.io.IOException;

/**
 * Created by slartus on 12.01.14.
 */
public class ShowInBrowserException extends IOException {
    private String url;

    public ShowInBrowserException(String message, String url) {
        super(message);
        this.url = url;
    }

    public String getUrl() {
        return url;
    }
}
