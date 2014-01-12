package ru.pda.http;

import android.content.Context;
import android.os.Environment;

import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.pda.common.FileExternals;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.util.List;

/**
 * Created by slinkin on 01.07.13.
 */
public class AdvCookieStore extends BasicCookieStore {
    @Override
    public void addCookie(Cookie cookie) {
        //if(!"4pda.ru".equals(cookie.getDomain()))return;
        super.addCookie(cookie);
    }

    private static String getSystemCookiesPath(Context context, Boolean readOnly) throws IOException {
        checkExternalStorageState(readOnly);
        String defaultFile =  Environment.getExternalStorageDirectory() + "/data/4pda.Nitro/4pda_cookies.cks";
        return defaultFile;
    }

    private static void checkExternalStorageState(Boolean readOnly) throws IOException {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
           return;
        } else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            if(!readOnly)
                throw new IOException("Нет доступа для записи в хранилище");
        } else {
            throw new IOException("Нет доступа в хранилище");
        }
    }

    public void readExternalCookies(Context context) throws IOException {
        readExternalCookies(this, getSystemCookiesPath(context,true));
    }

    public static void readExternalCookies(org.apache.http.client.CookieStore cookieStore,
                                           String cookieFile) throws IOException {
        try {
            FileInputStream fw = new FileInputStream(cookieFile);
            ObjectInput input = new ObjectInputStream(fw);

            while (true) {
                try {
                    SerializableCookie serializableCookie = new SerializableCookie();
                    serializableCookie.readExternal(input);
                    cookieStore.addCookie(serializableCookie);
                } catch (Exception ex) {
                    break;
                }

            }
            input.close();
            fw.close();
        } catch (IOException ex) {
            throw ex;
        }
    }

    public void writeExternalCookies(Context context) throws IOException {
        writeExternalCookies(getSystemCookiesPath(context,false));
    }

    public void writeExternalCookies(String cookiesFile) throws IOException {
        if (!FileExternals.mkDirs(cookiesFile))
            throw new IOException("Не могу создать директорию '" + cookiesFile + "' для cookies");

        new File(cookiesFile).createNewFile();
        FileOutputStream fw = new FileOutputStream(cookiesFile, false);

        ObjectOutput out = new ObjectOutputStream(fw);
        final List<Cookie> cookies = getCookies();
        for (Cookie cookie : cookies) {
            new SerializableCookie(cookie).writeExternal(out);
        }
        out.close();
        fw.close();
    }

}
