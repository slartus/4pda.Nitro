package ru.forpda.interfaces;

import org.apache.http.client.CookieStore;

import java.io.IOException;
import java.util.Map;

import ru.forpda.interfaces.common.ProgressState;

/**
 * Created by slartus on 12.01.14.
 */
public interface IHttpClient {
    CookieStore getCookieStore();

    String getRedirectUrl();

    String performGet(String s) throws IOException;

    String performGetFullVersion(String s) throws IOException;

    String performPost(String s, Map<String, String> additionalHeaders) throws IOException;

    String performPost(String s, Map<String, String> additionalHeaders, String encoding) throws IOException;

    String uploadFile(String url, String filePath, Map<String, String> additionalHeaders, ProgressState progress) throws Exception;
}
