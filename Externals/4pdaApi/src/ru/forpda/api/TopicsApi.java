package ru.forpda.api;

import android.text.Html;
import android.text.TextUtils;

import ru.forpda.common.DateTimeExternals;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ru.forpda.interfaces.IHttpClient;
import ru.forpda.interfaces.ListInfo;
import ru.forpda.interfaces.forum.Topic;

/**
 * Created by slartus on 12.01.14.
 */
public class TopicsApi {
    /**
     * Возвращает список тем из "Избранное"
     * @param client
     * @param listInfo
     * @return
     */
    public static ArrayList<Topic> getFavorites(IHttpClient client,ListInfo listInfo) throws ParseException, IOException {
        String pageBody = client.performGet("http://4pda.ru/forum/index.php?autocom=favtopics&st=" + listInfo.getFrom());

        Matcher m = Pattern.compile("<a href=\"http://4pda.ru/forum/index.php\\?autocom=[^\"]*?st=(\\d+)\">&raquo;</a>",Pattern.CASE_INSENSITIVE).matcher(pageBody);
        if (m.find()) {
            listInfo.setOutCount(Integer.parseInt(m.group(1)) + 1);
        }

        ArrayList<Topic> res = new ArrayList<Topic>();
        m = Pattern.compile("<!-- Begin Topic Entry \\d+ -->([\\s\\S]*?)<a id=\"tid-link-\\d+\" href=\"http://4pda.ru/forum/index.php\\?showtopic=(\\d+)\" title=\"[^\"'<>]*\">([^<>]*)</a></span>[\\s\\S]*?id='tid-desc-\\d+'>([^<>]*)</span>[\\s\\S]*?<span class=\"lastaction\">([^<>]*)<br /><a href=\"http://4pda.ru/forum/index.php\\?showtopic=\\d+&amp;view=getlastpost\">Послед.:</a> <b><a href='http://4pda.ru/forum/index.php\\?showuser=\\d+'>([^<>]*)</a>"
                ,Pattern.CASE_INSENSITIVE)
                .matcher(pageBody);

        String today = DateTimeExternals.getTodayString();
        String yesterday = DateTimeExternals.getYesterdayString();
        while (m.find()) {
            Topic topic = new Topic(m.group(2), m.group(3));
            if (m.group(1) != null)
                topic.setHasUnreadPosts(m.group(1).contains("view=getnewpost"));
            topic.setDescription(m.group(4));
            topic.setLastPostDate(DateTimeExternals.getDateTimeString(DateTimeExternals.parseForumDateTime(m.group(5), today, yesterday)));
            topic.setLastPostAuthor(m.group(6));
            res.add(topic);
        }

        return res;
    }

    public static ArrayList<Topic> getSubscribes(IHttpClient client, ListInfo listInfo) throws IOException, ParseException {
        String pageBody = client.performGet("http://4pda.ru/forum/index.php?act=UserCP&CODE=26");

        Pattern pattern = Pattern.compile("(?:<td colspan=\"6\" class=\"row1\"><b>(.*?)</b></td>)?\n" +
                "\\s*</tr><tr>\n" +
                "\\s*<td class=\"row2\" align=\"center\" width=\"5%\">(?:<font color='.*?'>)?(.*?)(?:</font>)?</td>\n" +
                "\\s*<td class=\"row2\">\n" +
                "\\s*<a href=\"http://4pda.ru/forum/index.php\\?showtopic=(\\d+).*?\">(.*?)</a>&nbsp;\n" +
                "\\s*\\( <a href=\"http://4pda.ru/forum/index.php\\?showtopic=\\d+.*?\" target=\"_blank\">В новом окне</a> \\)\n" +
                "\\s*<div class=\"desc\">(?:(.*?)<br />)?.*?\n" +
                "\\s*<br />\n" +
                "\\s*Тип: .*?\n" +
                "\\s*</div>\n" +
                "\\s*</td>\n" +
                "\\s*<td class=\"row2\" align=\"center\"><a href=\"javascript:who_posted\\(\\d+\\);\">\\d+</a></td>\n" +
                "\\s*<td class=\"row2\" align=\"center\">\\d+</td>\n" +
                "\\s*<td class=\"row2\">(.*?)<br />автор: <a href='http://4pda.ru/forum/index.php\\?showuser=\\d+'>(.*?)</a></td>",Pattern.CASE_INSENSITIVE);


        String today = DateTimeExternals.getTodayString();
        String yesterday = DateTimeExternals.getYesterdayString();


        Matcher m = pattern.matcher(pageBody);
        String forumTitle = null;
        ArrayList<Topic> res = new ArrayList<Topic>();
        while (m.find()) {

            if (!TextUtils.isEmpty(m.group(1)))
                forumTitle = m.group(1);
            Topic topic = new Topic(m.group(3), m.group(4));
            topic.setHasUnreadPosts(m.group(2).equals("+"));

            topic.setDescription(m.group(5));
            topic.setForumTitle(forumTitle);
            topic.setLastPostDate(DateTimeExternals.getDateTimeString(DateTimeExternals.parseForumDateTime(m.group(6), today, yesterday)));
            topic.setLastPostAuthor(m.group(7));

            res.add(topic);
        }
        listInfo.setFrom(res.size());
        return res;
    }

    public static ArrayList<Topic> getTopics(IHttpClient client, String forumId,ListInfo listInfo) {

        // тут магия парсинга
        return new ArrayList<Topic>();
    }
}
