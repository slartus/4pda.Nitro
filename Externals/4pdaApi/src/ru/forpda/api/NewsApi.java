package ru.forpda.api;

import android.text.Html;
import android.text.TextUtils;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ru.forpda.common.DateTimeExternals;
import ru.forpda.interfaces.IHttpClient;
import ru.forpda.interfaces.ListInfo;
import ru.forpda.interfaces.forum.News;

/**
 * Created by slartus on 12.01.14.
 */
public class NewsApi {
    public static ArrayList<News> getNews(IHttpClient httpClient, String url, ListInfo listInfo) throws IOException, ParseException {
        //http://4pda.ru/2013/page/7/
        //http://4pda.ru/2013/2/page/7/
        //http://4pda.ru/2013/2/2/page/7/
        //http://4pda.ru/tag/programs-for-ios/page/3
        //http://4pda.ru/page/5/
        //http://4pda.ru/page/5/?s=ios - поиск
        //http://4pda.ru/?s=%EF%EB%E0%ED%F8%E5%F2
        //http://4pda.ru/page/6/?s=%EF%EB%E0%ED%F8%E5%F2
        final int NEWS_PER_PAGE = 30;// 30 новостей на страницу выводит форум
        int pageNum = 1;
        String justUrl = url;// урл без страницы и параметров
        String params = "";// параметры, например, s=%EF%EB%E0%ED%F8%E5%F2
        // сначала проверим на поисковой урл
        Matcher m = Pattern.compile("(.*?)(?:page/+(\\d+)/+)?\\?(.*?)$", Pattern.CASE_INSENSITIVE)
                .matcher(url);
        if (m.find()) {
            justUrl = m.group(1);
            if (!TextUtils.isEmpty(m.group(2)))
                pageNum = Integer.parseInt(m.group(2));
            if (!TextUtils.isEmpty(m.group(3)))
                params = m.group(3);
        } else {
            m = Pattern.compile("(.*?)(?:page/+(\\d+)/+)?$", Pattern.CASE_INSENSITIVE)
                    .matcher(url);
            if (m.find()) {
                justUrl = m.group(1);
                if (!TextUtils.isEmpty(m.group(2)))
                    pageNum = Integer.parseInt(m.group(2));
            }
        }
        pageNum = (int) Math.ceil(listInfo.getFrom() / NEWS_PER_PAGE) + pageNum;
        String requestUrl = justUrl + "/page/" + pageNum + "/" + params;

        String dailyNewsPage = httpClient.performGet(requestUrl);

        Matcher postsMatcher = Pattern.compile("<div class=\"post\" id=\"post-\\d+\">([\\s\\S]*?)<span id=\"ka_\\d+_0_n\"></span></div><br /></div></div>")
                .matcher(dailyNewsPage);

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yy");


        Pattern mPattern = Pattern.compile("<a href=\"(/\\d+/\\d+/\\d+/(\\d+))/\" rel=\"bookmark\" title=\"(.*?)\" alt=\"\">.*?</a></h2>");

        Pattern textPattern = Pattern.compile("<div class=\"entry\" id=\"[^\"]*\">" +
                "(?:<a href=\"([^\"]*)\" class=\"oprj ([^\"]*)\"><div>(.*?)</div>)?" +
                "([\\s\\S]*?)" +
                "(?:<noindex><span class=\"mb_source\">Источник:&nbsp;<a href=\"([^\"]*)\" target=\"[^\"]*\">(.*?)</a></span></noindex>)?" +
                "<br /><br /></div><div class=\"postmetadata\" id=\"ka_meta_\\d+_0\"><span id=\"ka_\\d+_0\"></span>&nbsp;\\|&nbsp;" +
                "<strong>(.*?)</strong>&nbsp;\\|\\s*(\\d+\\.\\d+\\.\\d+)\\s*\\|\\s*" +
                "<a href=\"/\\d+/\\d+/\\d+/\\d+/#comments\" title=\"[^\"]*\"><b class=\"spr pc\"></b>\\s*(\\d+)\\s*</a>");
        Pattern imagePattern = Pattern.compile("<center><img[^>]*?src=\"(.*?)\"");
        ArrayList<News> res = new ArrayList<News>();
        while (postsMatcher.find()) {
            String postData = postsMatcher.group(1);
            m = mPattern.matcher(postData);

            if (m.find()) {
                String id = m.group(1);

                News news = new News(id, Html.fromHtml(m.group(3)).toString());

                Matcher textMatcher = textPattern.matcher(postData);
                if (textMatcher.find()) {
                    if (textMatcher.group(1) != null) {
                        news.setTagLink(textMatcher.group(1));
                        news.setTagName(textMatcher.group(2));
                        news.setTagTitle(textMatcher.group(3));
                    }
                    if (textMatcher.group(5) != null) {
                        news.setSourceUrl(textMatcher.group(5));
                        news.setSourceTitle(textMatcher.group(6));
                    }
                    news.setDescription(getDescription(textMatcher.group(4)));
                    news.setAuthor(Html.fromHtml(textMatcher.group(7)));
                    Date _pubDate = dateFormat.parse(textMatcher.group(8));
                    news.setNewsDate(DateTimeExternals.getDateString(_pubDate));
                    news.setCommentsCount(Integer.parseInt(textMatcher.group(9)));
                }

                Matcher imageMatcher = imagePattern.matcher(postData);
                if (imageMatcher.find()) {
                    news.setImgUrl(imageMatcher.group(1));
                }


                res.add(news);
            }
        }
        int lastPageNum = lastPageNum(dailyNewsPage);
        listInfo.setOutCount(res.size() * lastPageNum);
        return res;
    }

    private static String getDescription(String descriptionHtml) {
        return Html.fromHtml(removeDescriptionTrash(descriptionHtml)).toString();
//        final int maxSize=250;
//        String desc = Html.fromHtml(removeDescriptionTrash(descriptionHtml)).toString();
//        if (desc.length() > maxSize)
//            return desc.substring(0, maxSize) + "..";
//        return desc;
    }

    /**
     * Удалить из краткого текста новости ссылки "читать дальше" и картинки
     *
     * @return
     */
    private static String removeDescriptionTrash(CharSequence description) {
        return Pattern
                .compile("<p style=\"[^\"]*\"><a href=\"/\\d+/\\d+/\\d+/\\d+/#more-\\d+\" class=\"more-link\">читать дальше</a></p>|<img[^>]*?/>")
                .matcher(description)
                .replaceAll("").trim();
    }

    private static int lastPageNum(String pagebody) {
        Matcher m = Pattern.compile("<div class=\"wp-pagenavi\">.*href=\"/+page/+(\\d+)/+\"\\s+class=\"page\".*?</div>").matcher(pagebody);

        if (m.find()) {
            return Integer.parseInt(m.group(1));
        }
        return 1;
    }
}
