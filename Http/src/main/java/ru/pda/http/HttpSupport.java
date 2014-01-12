package ru.pda.http;

import android.content.Context;

import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

import java.io.IOException;

/**
 * Created by slinkin on 27.06.13.
 */
public class HttpSupport {

    private static HttpContext _context;
    private static AdvCookieStore _cookieStore;

    public static synchronized HttpContext getHttpContextInstance() {
        if (_context == null) {
            _context = new BasicHttpContext();
        }
        return _context;
    }

    public static synchronized AdvCookieStore getCookieStoreInstance(Context context) throws IOException {
        if (_cookieStore == null) {
            _cookieStore = new AdvCookieStore();
            _cookieStore.readExternalCookies(context);
        }
        return _cookieStore;
    }


}
