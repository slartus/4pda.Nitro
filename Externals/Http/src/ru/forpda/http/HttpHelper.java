package ru.forpda.http;

import android.content.Context;
import android.net.http.AndroidHttpClient;
import android.util.Log;

import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.CookieStore;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.ExecutionContext;
import org.apache.http.protocol.HttpContext;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ru.forpda.interfaces.IHttpClient;
import ru.forpda.interfaces.common.ProgressState;
import ru.forpda.interfaces.exceptions.ShowInBrowserException;

/**
 * Created by slinkin on 27.06.13.
 */
public class HttpHelper implements IHttpClient {
    public static String HTTP_CONTENT_CHARSET = "windows-1251";
    public static final String MIME_FORM_ENCODED = "application/x-www-form-urlencoded";
    public static final String ACCEPT_ENCODING = "Accept-Encoding";
    public static final String GZIP = "gzip";
    protected static final String CONTENT_TYPE = "Content-Type";
    protected static final int POST_TYPE = 1;
    protected static final int GET_TYPE = 2;
    private Context mContext;

    private String redirectUrl;

    public HttpHelper(Context context) {

        mContext = context;
    }


    @Override
    public String getRedirectUrl() {
        return redirectUrl;
    }

    @Override
    public String performGet(String link) throws IOException {
        return performRequest(GET_TYPE, link, null);

    }

    @Override
    public String performGetFullVersion(String s) throws IOException {
        return null;
    }

    @Override
    public String performPost(String link, Map<String, String> additionalHeaders) throws IOException {
        return performRequest(POST_TYPE, link, additionalHeaders);
    }

    AndroidHttpClient client = null;

    public String performRequest(int requestType, String link,
                                 Map<String, String> additionalHeaders) throws IOException {
        try {
            client = AndroidHttpClient.newInstance("Android");
            return performRequestClient(requestType, link, additionalHeaders);
        } finally {

            client.close();
        }
    }

    private String performRequestClient(int requestType, String link,
                                        Map<String, String> additionalHeaders) throws IOException {
        return performRequestClient(requestType, link, additionalHeaders, false);
    }

    private String performRequestClient(int requestType, String link,
                                        Map<String, String> additionalHeaders,
                                        boolean redirect) throws IOException {

        redirectUrl = link;
        String encoding = HTTP_CONTENT_CHARSET;

        HttpRequestBase request = requestType == POST_TYPE ? new HttpPost(link) : new HttpGet(link);


        HttpContext http_context = HttpSupport.getHttpContextInstance();
        CookieStore cookie_store = HttpSupport.getCookieStoreInstance(mContext);
        http_context.setAttribute(ClientContext.COOKIE_STORE, cookie_store);


        final Map<String, String> sendHeaders = new HashMap<String, String>();
        // add encoding cat_name for gzip if not present

        sendHeaders.put(HttpHelper.ACCEPT_ENCODING, HttpHelper.GZIP);
        if (requestType == HttpHelper.POST_TYPE) {
            request.addHeader(HttpHelper.CONTENT_TYPE, HttpHelper.MIME_FORM_ENCODED);
        }

        if ((additionalHeaders != null) && (additionalHeaders.size() > 0)) {
            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
            for (Map.Entry<String, String> entry : additionalHeaders.entrySet()) {
                nvps.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
            }
            ((HttpPost) request).setEntity(new UrlEncodedFormEntity(nvps, encoding));
        }

        Log.i("4pdaClient", link);


        return client.execute(request, new MyResponseHandler(http_context), http_context);

    }


    public class MyResponseHandler extends BasicResponseHandler
            implements ResponseHandler<java.lang.String> {

        private HttpContext mContext;

        public MyResponseHandler(HttpContext context) {
            super();

            mContext = context;
        }

        private URI getRedirectLocation(HttpResponse httpResponse) {
            String location = httpResponse.getFirstHeader("location").getValue();
            URI uri = null;

            try {
                uri = new URI(location);
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }

            if (!uri.isAbsolute()) {// если новая ссылка начинается с /forum..
                // Adjust location URI
                HttpHost target = (HttpHost) mContext.getAttribute(
                        ExecutionContext.HTTP_TARGET_HOST);
                if (target == null) {
                    throw new IllegalStateException("Target host not available " +
                            "in the HTTP context");
                }

                HttpRequest request = (HttpRequest) mContext.getAttribute(
                        ExecutionContext.HTTP_REQUEST);

                try {
                    URI requestURI = new URI(request.getRequestLine().getUri());
                    URI absoluteRequestURI = URIUtils.rewriteURI(requestURI, target, true);
                    uri = URIUtils.resolve(absoluteRequestURI, uri);
                } catch (URISyntaxException ex) {

                }
            }

            return uri;
        }

        public java.lang.String handleResponse(HttpResponse httpResponse) throws IOException {
            StatusLine status = httpResponse.getStatusLine();
            if (status.getStatusCode() == 302) {// redirect
                URI redirectUri = getRedirectLocation(httpResponse);
                return performRequestClient(GET_TYPE, redirectUri.toString(), null, true);
            }
            checkStatus(status, "");

            BufferedReader reader = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent(), "windows-1251"));
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + System.getProperty("line.separator"));
            }


            return sb.toString();
        }

        private void checkStatus(StatusLine status, java.lang.String url) throws IOException {
            int statusCode = status.getStatusCode();
            if (statusCode != HttpStatus.SC_OK && statusCode != HttpStatus.SC_PARTIAL_CONTENT) {

                if (statusCode != 200 && statusCode != 300) {
                    if (statusCode >= 500 && statusCode < 600)
                        throw new ShowInBrowserException("Сайт не отвечает: " + statusCode + " " + AppHttpStatus.getReasonPhrase(statusCode, status.getReasonPhrase()), url);
                    else if (statusCode == 404)
                        throw new ShowInBrowserException("Сайт не отвечает: " + statusCode + " " + AppHttpStatus.getReasonPhrase(statusCode, status.getReasonPhrase()), url);
                    else
                        throw new ShowInBrowserException(statusCode + " " + AppHttpStatus.getReasonPhrase(statusCode, status.getReasonPhrase()), url);
                }
            }
        }
    }


    @Override
    public String performPost(String s, Map<String, String> additionalHeaders, String encoding) throws IOException {
        return null;
    }

    @Override
    public String uploadFile(String url, String filePath, Map<String, String> additionalHeaders, ProgressState progress) throws Exception {
        return null;
    }
}
