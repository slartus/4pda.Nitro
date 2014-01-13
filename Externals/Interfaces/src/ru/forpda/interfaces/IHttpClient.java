package ru.forpda.interfaces;

import java.io.IOException;
import java.util.Map;

import ru.forpda.interfaces.common.ProgressState;

/**
 * Created by slartus on 12.01.14.
 */
public interface IHttpClient {


    String performGet(String s) throws IOException;

    String performGetFullVersion(String s) throws IOException;

    String performPost(String s, Map<String, String> additionalHeaders) throws IOException;

    String performPost(String s, Map<String, String> additionalHeaders, String encoding) throws IOException;

    String uploadFile(String url, String filePath, Map<String, String> additionalHeaders, ProgressState progress) throws Exception;
}
