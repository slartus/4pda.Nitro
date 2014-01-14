package ru.forpda.api;

import android.text.Html;

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


        Pattern pattern = Pattern.compile("<a id=\"tid-link-\\d+\" href=\"http://4pda.ru/forum/index.php\\?showtopic=(\\d+)\" title=\".*?\">(.*?)</a></span>");
        Pattern descPattern = Pattern.compile(" id='tid-desc-\\d+'>(.*?)</span>");
        Pattern postsCountPattern = Pattern.compile("<a href=\"javascript:who_posted\\(\\d+\\);\">(\\d+)</a>");

        Pattern lastMessageDatePattern = Pattern.compile("<span class=\"lastaction\">(.*?)<br /><a href=\"http://4pda.ru/forum/index.php\\?showtopic=\\d+&amp;view=getlastpost\">Послед.:</a> <b><a href='http://4pda.ru/forum/index.php\\?showuser=\\d+'>(.*?)</a>");

        Pattern pagesCountPattern = Pattern.compile("<a href=\"http://4pda.ru/forum/index.php\\?autocom=.*?st=(\\d+)\">&raquo;</a>");


        String[] strings = pageBody.split("\n");
        pageBody = null;
        int phase = 0;
        Matcher m;
        Topic topic = null;


        String today = DateTimeExternals.getTodayString();
        String yesterday = DateTimeExternals.getYesterdayString();
        ArrayList<Topic> res = new ArrayList<Topic>();
        for (String str : strings) {
            if (listInfo.getOutCount() == 0) {
                m = pagesCountPattern.matcher(str);
                if (m.find()) {
                    listInfo.setOutCount(Integer.parseInt(m.group(1)) + 1);
                }
            }
            switch (phase) {
                case 0:
                    m = pattern.matcher(str);
                    if (m.find()) {
                        topic = new Topic(m.group(1), Html.fromHtml(m.group(2)).toString());
                        topic.setHasUnreadPosts(str.contains("view=getnewpost"));
                        phase++;
                    }
                    break;
                case 1:
                    m = descPattern.matcher(str);
                    if (m.find()) {
                        topic.setDescription(m.group(1));
                        phase++;
                    }
                    break;
                case 2:

                    m = postsCountPattern.matcher(str);
                    if (m.find()) {
                       // topic.setPostsCount(m.group(1));
                        phase++;
                    }
                    break;
                case 3:
                    m = lastMessageDatePattern.matcher(str);
                    if (m.find()) {
                        topic.setLastPostDate(DateTimeExternals.getDateTimeString( DateTimeExternals.parseForumDateTime(m.group(1), today, yesterday)));

                        topic.setLastPostAuthor(m.group(2));
                        res.add(topic);
                        phase = 0;
                    }
                    break;
            }
        }

        return res;
    }

    public static ArrayList<Topic> getSubscribes(IHttpClient client,ListInfo listInfo) {

        // тут магия парсинга
        return new ArrayList<Topic>();
    }

    public static ArrayList<Topic> getTopics(IHttpClient client, String forumId,ListInfo listInfo) {

        // тут магия парсинга
        return new ArrayList<Topic>();
    }
}
