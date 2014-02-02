package ru.pda.nitro;

import android.content.Context;
import android.text.TextUtils;

import org.apache.http.client.CookieStore;
import org.apache.http.cookie.Cookie;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import ru.forpda.api.ProfileApi;
import ru.forpda.http.AdvCookieStore;
import ru.forpda.http.HttpHelper;
import ru.forpda.http.HttpSupport;
import ru.forpda.http.SimpleCookie;
import android.util.*;


/**
 * Created by slartus on 12.01.14.
 */
public class UserProfile {
    String mUser = "гость";
    String mK = "";
    String mUserId = "";
	
	
    public interface LoginedStateChangedListener {
        void changed();
    }

    ArrayList<LoginedStateChangedListener> m_Listeners = new ArrayList<LoginedStateChangedListener>();

    public void addLoginedStateChangedListener(LoginedStateChangedListener loginedStateChangedListener) {
        m_Listeners.add(loginedStateChangedListener);
    }

    protected void loginedStateChanged() {
        for (LoginedStateChangedListener listener : m_Listeners) {
            listener.changed();
        }
    }

    public String getLogin() {
        return mUser;
    }
	
	public String getAutchKey(){
		return mK;
	}
	

    public Boolean doLogin(String login, String password) throws Exception {
        Context context = App.getInstance();
        Map<String, String> outParams = new HashMap<String, String>();
        HttpHelper httpHelper = new HttpHelper(context);
        AdvCookieStore cookieStore = HttpSupport.getCookieStoreInstance(context);
        cookieStore.clear();

        Boolean logined = ProfileApi.login(httpHelper, login, password, false, outParams);


        String loginFailedReason = outParams.get(ProfileApi.LOGIN_FAILED_REASON_KEY);

        mUserId = outParams.get(ProfileApi.USER_ID_KEY);
        mUser = outParams.get(ProfileApi.USER_KEY);
        mK = outParams.get(ProfileApi.K_KEY);

        cookieStore.addCookie(new SimpleCookie("4pda.UserId", mUserId));
        cookieStore.addCookie(new SimpleCookie("4pda.User", mUser));
        cookieStore.addCookie(new SimpleCookie("4pda.K", mK));

        cookieStore.writeExternalCookies(context);
        if (!TextUtils.isEmpty(loginFailedReason))
            throw new Exception(loginFailedReason);
        loginedStateChanged();
        return logined;
    }

    public void doLogout() throws Throwable {
        Context context = App.getInstance();
        HttpHelper httpHelper = new HttpHelper(context);
        ProfileApi.logout(httpHelper, mK);

        AdvCookieStore cookieStore = HttpSupport.getCookieStoreInstance(context);
        cookieStore.clear();
        cookieStore.writeExternalCookies(context);
        loginedStateChanged();
    }


    public Boolean isLogined() {
        return checkLogin();
    }

    public Boolean checkLogin() {
        mUserId = "";
        mUser = "";
        mK = "";

        try {
            Context context = App.getInstance();
            CookieStore cookies = HttpSupport.getCookieStoreInstance(context);
            Cookie memberIdCookie = null;
            for (Cookie cookie : cookies.getCookies()) {
                if ("4pda.UserId".equals(cookie.getName())) {
                    mUserId = cookie.getValue();
                } else if ("4pda.User".equals(cookie.getName())) {
                    mUser = cookie.getValue();
                } else if ("4pda.K".equals(cookie.getName())) {
                    mK = cookie.getValue();
                } else if ("member_id".equals(cookie.getName())) {
                    memberIdCookie = cookie;
                }
				Log.e("nitro", "nik: " + mUser + " token: " + mK + " id: "+ mUserId);
            }

            if (!TextUtils.isEmpty(mUser)
                    && !TextUtils.isEmpty(mUserId)
                    && !TextUtils.isEmpty(mK)
                    && memberIdCookie != null) {
                return mUserId.equals(memberIdCookie.getValue());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;

    }
}
