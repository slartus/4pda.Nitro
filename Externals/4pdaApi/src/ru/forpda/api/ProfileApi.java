package ru.forpda.api;
import android.text.Html;
import android.text.TextUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ru.forpda.interfaces.IHttpClient;

/**
 * Created by slartus on 12.01.14.
 */
public class ProfileApi {
    public static final String USER_ID_KEY="UserId";
    public static final String USER_KEY="User";
    public static final String K_KEY="K";
    public static final String LOGIN_FAILED_REASON_KEY="LoginFailedReason";

    /**
     * Проверка логина на странице
     * @param pageBody
     * @param outParams
     * USER_ID_KEY - идентификатор пользователя
     * USER_KEY - логин пользователя
     * K -  ключ
     * @return
     * если залогинен - true
     */
    public static boolean checkLogin(String pageBody, Map<String, String> outParams) {
        final Pattern checkLoginPattern = Pattern.compile("<a href=\"(http://4pda.ru)?/forum/index.php\\?showuser=(\\d+)\">(.*?)</a></b> \\( <a href=\"(http://4pda.ru)?/forum/index.php\\?act=Login&amp;CODE=03&amp;k=([a-z0-9]{32})\">Выход</a>");
        Matcher m = checkLoginPattern.matcher(pageBody);
        String outUserId;
        String outUser;
        String outK;
        Boolean res = false;
        if (m.find()) {
            res = true;
            outUserId = m.group(2);
            outUser = m.group(3);
            outK = m.group(5);
        } else {
            outUserId = "";
            outUser = "гость";
            outK = "";
        }

        outParams.put(USER_ID_KEY, outUserId);
        outParams.put(USER_KEY, outUser);
        outParams.put(K_KEY, outK);
        return res;
    }

    /**
     * @param httpClient
     * @param login
     * @param password
     * @param privacy
     * @param outParams  LoginFailedReason - текст ошибки, в случае провала логина,
     *                   UserId - id пользователя,
     *                   User - логин пользователя
     *                   K - еще какой-то идентификатор сессии
     * @return
     * @throws Exception
     */
    public static Boolean login(IHttpClient httpClient, String login, String password, Boolean privacy,
                                Map<String, String> outParams) throws Exception {
        String outLoginFailedReason = "";

        Boolean logined = false;
        outParams.put(USER_ID_KEY, "");
        outParams.put(USER_KEY, "");
        outParams.put(K_KEY, "");
        try {
            outLoginFailedReason = null;

            Map<String, String> additionalHeaders = new HashMap<String, String>();

            additionalHeaders.put("UserName", login);
            additionalHeaders.put("PassWord", password);
            additionalHeaders.put("CookieDate", "1");
            additionalHeaders.put("Privacy", privacy ? "1" : "0");
            UUID uuid = UUID.randomUUID();

            String outSessionId = uuid.toString().replace("-", "");
            additionalHeaders.put("s", outSessionId);
            additionalHeaders.put("act", "Login");
            additionalHeaders.put("CODE", "01");

            additionalHeaders.put("referer", "http://4pda.ru/forum/index.php?s=" + outSessionId + "&amp;amp;s=" + outSessionId + "&amp;act=Login&amp;CODE=01");


            String res = httpClient.performPost("http://4pda.ru/forum/index.php", additionalHeaders);

            if (TextUtils.isEmpty(res)) {
                outLoginFailedReason = "Сервер вернул пустую страницу";
                return false;
            }

            logined = checkLogin(res, outParams);

            if (!logined) {
                Pattern checkPattern = Pattern.compile("\t\t<h4>Причина:</h4>\n" +
                        "\n" +
                        "\t\t<p>(.*?)</p>", Pattern.MULTILINE);
                Matcher m = checkPattern.matcher(res);
                if (m.find()) {
                    outLoginFailedReason = m.group(1);
                } else {
                    checkPattern = Pattern.compile("\t<div class=\"formsubtitle\">Обнаружены следующие ошибки:</div>\n" +
                            "\t<div class=\"tablepad\"><span class=\"postcolor\">(.*?)</span></div>");
                    m = checkPattern.matcher(res);
                    if (m.find()) {
                        outLoginFailedReason = m.group(1);
                    } else {
                        outLoginFailedReason = Html.fromHtml(res).toString();
                    }
                }
            }
        } finally {
            outParams.put(LOGIN_FAILED_REASON_KEY, outLoginFailedReason);
        }


        return logined;
    }

    /**
     * ЛОгаут
     *
     * @param httpClient
     * @param k          идентификатор, полученный при логине
     * @return
     * @throws Throwable
     */
    public static String logout(IHttpClient httpClient, String k) throws Throwable {
        return httpClient.performGet("http://4pda.ru/forum/index.php?act=Login&CODE=03&k=" + k);
    }
}
