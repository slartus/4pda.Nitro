package ru.pda.nitro;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import org.apache.http.client.CookieStore;
import org.apache.http.cookie.Cookie;

import java.io.IOException;
import java.util.ArrayList;

import ru.forpda.api.ProfileApi;
import ru.forpda.api.infos.LoginResult;
import ru.forpda.http.AdvCookieStore;
import ru.forpda.http.HttpHelper;
import ru.forpda.http.HttpSupport;
import ru.forpda.http.SimpleCookie;


/**
 * Created by slartus on 12.01.14.
 */
public class UserProfile {
    String mUser = "гость";
    String mK = "";
    String mUserId = "";
	String mUserAvatar = "";
	
	
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
	
	public String getAvatar(){
		return mUserAvatar;
	}

    public Boolean doLogin(String login, String password, boolean privacy) throws Exception {
        Context context = App.getInstance();

        HttpHelper httpHelper = new HttpHelper(context);
        AdvCookieStore cookieStore = HttpSupport.getCookieStoreInstance(context);
        cookieStore.clear();

        LoginResult loginResult = ProfileApi.login(httpHelper, login, password, privacy);

        mUserId = loginResult.getUserId().toString();
        mUser = loginResult.getUserLogin().toString();
        mK = loginResult.getK().toString();
		mUserAvatar = loginResult.getUserAvatarUrl().toString();

        cookieStore.addCookie(new SimpleCookie("4pda.UserId", mUserId));
        cookieStore.addCookie(new SimpleCookie("4pda.User", mUser));
        cookieStore.addCookie(new SimpleCookie("4pda.K", mK));
		cookieStore.addCookie(new SimpleCookie("4pda.UserAvatar", mUserAvatar));

        cookieStore.writeExternalCookies(context);

        if (!loginResult.isSuccess())
            throw new Exception(loginResult.getLoginError().toString());

        loginedStateChanged();
        return loginResult.isSuccess();
    }

    public void doLogout() throws Throwable {
        Context context = App.getInstance();
        HttpHelper httpHelper = new HttpHelper(context);
        ProfileApi.logout(httpHelper, mK);

        AdvCookieStore cookieStore = HttpSupport.getCookieStoreInstance(context);
        cookieStore.clear();
        cookieStore.writeExternalCookies(context);
        loginedStateChanged();
		mUser = "гость";
		mUserAvatar = "";
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
                   mUser =  cookie.getValue().equals("") ? "гость" : cookie.getValue();
                } else if ("4pda.K".equals(cookie.getName())) {
                    mK = cookie.getValue();
                } else if ("member_id".equals(cookie.getName())) {
                    memberIdCookie = cookie;
                } else if ("4pda.UserAvatar".equals(cookie.getName())){
					mUserAvatar = cookie.getValue();
				}
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
