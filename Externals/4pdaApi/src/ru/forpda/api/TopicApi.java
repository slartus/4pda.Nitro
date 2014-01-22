package ru.forpda.api;

import android.text.Html;
import android.text.TextUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ru.forpda.interfaces.IHttpClient;
import ru.forpda.interfaces.exceptions.ShowInBrowserException;

/**
 * Created by slartus on 12.01.14.
 */
public class TopicApi {
    /**
     * Не уведомлять
     * При выборе этой опции вы не будете получать никаких уведомлений на e-mail,
     * но все новые и обновленные темы и форумы будут сохранены на вашей личной странице подписок
     */
    public static final String SUBSCRIBE_EMAIL_TYPE_NONE = "none";
    /**
     * Уведомление с задержкой
     * При выборе этой опции вы будете уведомлены о новых темах или ответах во время своего отсутствия на форуме.
     */
    public static final String SUBSCRIBE_EMAIL_TYPE_DELAYED = "delayed";
    /**
     * Немедленное уведомление
     * При выборе этой опции вы будете уведомлены о новых темах или ответах, независимо от того, присутствуете ли вы или отсутствуете на форуме.
     */
    public static final String SUBSCRIBE_EMAIL_TYPE_IMMEDIATE = "immediate";
    /**
     * Ежедневное уведомление
     * При выборе этой опции вы будете ежедневно получать краткое уведомление обо всех новых темах и сообщениях за текущий день
     */
    public static final String SUBSCRIBE_EMAIL_TYPE_DAILY = "daily";
    /**
     * Еженедельное уведомление
     * При выборе этой опции вы будете еженедельно получать краткое уведомление обо всех новых темах и сообщениях за текущую неделю
     */
    public static final String SUBSCRIBE_EMAIL_TYPE_WEEKLY = "weekly";

    /**
     * Подписка на ответы в топике
     *
     * @param forumId   - id форума (можно получить из TopicApi.getForumId)
     * @param authKey   - ключ, возвращаемый при логине в параметре K_KEY
     * @param topicId   - id топика
     * @param emailtype - способ уведомления (смотри константы TopicApi.SUBSCRIBE_EMAIL_TYPE_)
     */
    public static ResultInfo subscribe(IHttpClient client, CharSequence forumId, CharSequence authKey,
                                       CharSequence topicId, String emailtype) throws IOException {
        Map<String, String> additionalHeaders = new HashMap<String, String>();
        additionalHeaders.put("act", "usercp");
        additionalHeaders.put("CODE", "end_subs");
        additionalHeaders.put("method", "topic");
        additionalHeaders.put("auth_key", authKey.toString());
        additionalHeaders.put("tid", topicId.toString());
        additionalHeaders.put("fid", forumId.toString());
        additionalHeaders.put("st", "0");
        additionalHeaders.put("emailtype", emailtype);
        String res = client.performPost("http://4pda.ru/forum/index.php", additionalHeaders);

        Pattern p = Pattern.compile("<div class=\"errorwrap\">\n" +
                "\\s*<h4>Причина:</h4>\n" +
                "\\s*\n" +
                "\\s*<p>(.*)</p>", Pattern.MULTILINE);
        Matcher m = p.matcher(res);
        if (m.find()) {

            return new ResultInfo(false, "Ошибка подписки: " + m.group(1));
        }
        return new ResultInfo(true, "");
    }

    /**
     * Возвращает сервисный id для топика из подписок
     *
     * @param httpClient
     * @param topicId
     * @return null - нет в подписках топика
     */
    private static CharSequence getTopicSubscribedId(IHttpClient httpClient, CharSequence topicId) throws IOException {
        String body = httpClient.performGet("http://4pda.ru/forum/index.php?act=UserCP&CODE=26");

        Pattern pattern = Pattern.compile("(?:<td colspan=\"6\" class=\"row1\"><b>.*?</b></td>)?\n" +
                "\\s*</tr><tr>\n" +
                "\\s*<td class=\"row2\" align=\"center\" width=\"5%\">(?:<font color='.*?'>)?.*?(?:</font>)?</td>\n" +
                "\\s*<td class=\"row2\">\n" +
                "\\s*<a href=\"http://4pda.ru/forum/index.php\\?showtopic=" + topicId + "\">.*?</a>&nbsp;\n" +
                "\\s*\\(\\s*<a href=\"http://4pda.ru/forum/index.php\\?showtopic=" + topicId + "\" target=\"_blank\">В новом окне</a> \\)\n" +
                "\\s*<div class=\"desc\">(?:.*?<br />)?.*?\n" +
                "\\s*<br />\n" +
                "\\s*Тип: .*?\n" +
                "\\s*</div>\n" +
                "\\s*</td>\n" +
                "\\s*<td class=\"row2\" align=\"center\"><a href=\"javascript:who_posted\\(\\d+\\);\">\\d+</a></td>\n" +
                "\\s*<td class=\"row2\" align=\"center\">\\d+</td>\n" +
                "\\s*<td class=\"row2\">.*?<br />автор: <a href='http://4pda.ru/forum/index.php\\?showuser=\\d+'>.*?</a></td>\n" +
                "\\s*<td class=\"row1\" align=\"center\" style='padding: 1px;'><input class='checkbox' type=\"checkbox\" name=\"id-(\\d+)\" value=\"yes\" /></td>");

        Matcher m = pattern.matcher(body);

        if (m.find())
            return m.group(1);
        return null;
    }

    /**
     * Отписка от темы
     *
     * @throws IOException
     */
    public static ResultInfo unSubscribe(IHttpClient httpClient, CharSequence topicId) throws IOException {
        CharSequence subscribesId = getTopicSubscribedId(httpClient, topicId);
        if (subscribesId != null) {
            Map<String, String> additionalHeaders = new HashMap<String, String>();
            additionalHeaders.put("act", "UserCP");
            additionalHeaders.put("CODE", "27");
            additionalHeaders.put("id-" + subscribesId, "yes");
            additionalHeaders.put("trackchoice", "unsubscribe");
            httpClient.performPost("http://4pda.ru/forum/index.php", additionalHeaders);

            return new ResultInfo(true, null);
        }
        return new ResultInfo(false, "Тема в подписках не найдена");
    }

    /**
     * Добавить топик в Избранное
     *
     * @param forumId - id форума (можно получить из TopicApi.getForumId)
     * @param topicId - id топика
     * @return
     * @throws IOException
     */
    public static ResultInfo addToFavorites(IHttpClient httpClient, CharSequence forumId, CharSequence topicId) throws IOException {

        String res = httpClient.performGet("http://4pda.ru/forum/index.php?autocom=favtopics&CODE=03&f=" + forumId + "&t=" + topicId + "&st=0");

        Pattern pattern = Pattern.compile("\\s*<div class=\"tablepad\">\\s*(.*)\\s*<ul>", Pattern.MULTILINE);
        Matcher m = pattern.matcher(res);
        if (m.find()) {
            return new ResultInfo(true, m.group(1));
        } else {
            pattern = Pattern.compile("\\s*<h4>Причина:</h4>\\s*<p>(.*?)</p>", Pattern.MULTILINE);
            m = pattern.matcher(res);
            if (m.find()) {
                return new ResultInfo(false, m.group(1));
            }
        }
        return new ResultInfo(false, "Результат неизвестен. Сообщите разработчику");
    }

    /**
     * Удалить тему из избранного
     *
     * @param forumId - id форума (можно получить из TopicApi.getForumId)
     * @param topicId - id топика
     * @return
     * @throws IOException
     */
    public static ResultInfo removeFromFavorites(IHttpClient httpClient, CharSequence forumId,
                                                 CharSequence topicId) throws IOException {

        String query = "http://4pda.ru/forum/index.php?autocom=favtopics&CODE=02&selectedtids="
                + topicId + "&cb=1&t=" + topicId + "&st=0";
        if (!TextUtils.isEmpty(forumId))
            query += "&f=" + forumId;
        httpClient.performGet(query);

        return new ResultInfo(true, null);
    }

    /**
     * Возвращает ID форума для топика
     *
     * @return null - в случае провала
     * @throws IOException
     */
    public CharSequence getForumId(IHttpClient client, CharSequence topicId) throws IOException {

        String res = client.performGet("http://4pda.ru/forum/lofiversion/index.php?t" + topicId + ".html");

        Pattern pattern = Pattern.compile("<div class='ipbnav'>.*<a href='http://4pda.ru/forum/lofiversion/index.php\\?f(\\d+).html'>.*?</a></div>", Pattern.MULTILINE);
        Matcher m = pattern.matcher(res);
        if (m.find()) {
            return m.group(1);
        } else {
            return null;
        }

    }

    private static CharSequence normalizeTopicUrl(CharSequence topicUrl) {
        return topicUrl.toString()
                .replaceAll("#$", "$")// иногда встречаются ссылки вида ..#entry28650336#
                ;
    }

    public static TopicResult getTopic(IHttpClient client, CharSequence topicUrl) throws IOException {
        TopicResult topicResult = new TopicResult();


        String topicBody = client.performGet(normalizeTopicUrl(topicUrl).toString());
        topicResult.parseUrl(client.getRedirectUrl());

        Matcher mainMatcher = Pattern.compile("(<div class=\"pagination\">.[\\s\\S]*?<div class=\"pagination\">.*?</div>)")
                .matcher(topicBody);
        checkTopicResult(topicUrl, topicResult.getTopicId(), topicBody, mainMatcher);

        topicResult.parseHeader(mainMatcher.group(1));

        topicBody = mainMatcher.group(1);

        topicResult.setBody(topicBody);
        return topicResult;
    }

    /**
     * Проверяем, что вернулась корректная страница топика
     *
     * @throws IOException
     */
    private static void checkTopicResult(CharSequence topicUrl, CharSequence topicId, String topicBody,
                                         Matcher mainMatcher) throws IOException {
        if (!mainMatcher.find()) {
            final Pattern errorPattern = Pattern.compile("<div class=\"errorwrap\">([\\s\\S]*?)</div>");
            Matcher errorMatcher = errorPattern.matcher(topicBody);
            if (errorMatcher.find()) {
                final Pattern errorReasonPattern = Pattern.compile("<p>(.*?)</p>");
                Matcher errorReasonMatcher = errorReasonPattern.matcher(errorMatcher.group(1));
                if (errorReasonMatcher.find()) {
                    throw new ShowInBrowserException(topicUrl.toString(), errorReasonMatcher.group(1));
                }
            }


            if (TextUtils.isEmpty(topicBody))
                throw new ShowInBrowserException(topicUrl.toString(), "Сервер вернул пустую страницу");
            if (topicBody.startsWith("<h1>"))
                throw new ShowInBrowserException(topicUrl.toString(), "Ответ сайта 4pda: " + Html.fromHtml(topicBody).toString());
            throw new IOException("Ошибка разбора страницы id=" + topicId);
        }
    }
}
