package ru.forpda.interfaces.forum;

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
import android.util.*;


/**
 * Created by slartus on 12.01.14.
 */
public class NewsList extends ArrayList<News> {
    private IHttpClient mClient;
    private CharSequence mSearchTag;

    private CharSequence mLastNewsUrl;
    private int mLastNewsPage;
    private int newsCountInt;

    /**
     * Сколько всего новостей на сайте
     *
     * @return
     */
    public int getNewsCount() {
        return newsCountInt;

    }

    private static String getSearchTag(String url) {
        Matcher m = Pattern.compile("4pda.ru/tag/(.*?)(/|$)").matcher(url);
        if (m.find()) {
            return "tag/" + m.group(1) + "/";
        }
        return url;
    }

    /**
     * @param client
     * @param newsUrl          - урл страницы новостей
     */
    public NewsList(IHttpClient client, String newsUrl) {

        mClient = client;
        mSearchTag = getSearchTag(newsUrl);
    }


    public void loadNextNewsPage() throws IOException, ParseException {
        Log.e("news", "size: " + size() + " empty: " + TextUtils.isEmpty(mSearchTag));
		if (size() == 0) {
            getPage(1, "http://4pda.ru/" + mSearchTag);
            return;
        }
		mLastNewsUrl = size() > 0 ? get(size() - 1).getId() : "";
        mLastNewsPage = size() > 0 ? get(size() - 1).getPage() : 0;
        CharSequence url = mLastNewsUrl;

        if (TextUtils.isEmpty(mSearchTag)) {
            Matcher m = Pattern.compile("4pda.ru/(\\d+)/(\\d+)/(\\d+)/(\\d+)").matcher(url);
            m.find();

            int year = Integer.parseInt(m.group(1));
            int nextPage = mLastNewsPage + 1;
            loadPage(year, nextPage, 0);
        } else {
            int nextPage = mLastNewsPage + 1;
            getPage(nextPage, "http://4pda.ru/" + mSearchTag + "page/" + nextPage);
			Log.e("news", "url: " +"http://4pda.ru/" + mSearchTag + "page/" + nextPage );
        }

        
    }

    private void loadPage(int year, int nextPage, int iteration) throws IOException, ParseException {

        String dailyNewsUrl = "http://4pda.ru/" + year + "/page/" + nextPage;

        String dailyNewsPage = getPage(nextPage, dailyNewsUrl);

        if (size() == 0) {
            if (iteration > 0) return;
            if (dailyNewsPage.contains("По указанным параметрам не найдено ни одного поста"))
                loadPage(year - 1, 1, iteration + 1);
            else
                loadPage(year, nextPage + 1, iteration + 1);
        }
    }

    private int lastPageNum(String pagebody, int curPage) {
        Matcher m = Pattern.compile("<div class=\"wp-pagenavi\">.*<a href=\".*?/page/(\\d+)/\"\\s+class=\"page\".*?</div>").matcher(pagebody);

        if (m.find()) {
            int newsPerPage = size() / curPage;
            return Integer.parseInt(m.group(1)) * newsPerPage;
        }
        return getNewsCount();
    }

    private String getPage(int page, String newsUrl) throws IOException, ParseException {
        String dailyNewsPage = mClient.performGet(newsUrl);
        Matcher postsMatcher = Pattern.compile("<div class=\"post\" id=\"post-\\d+\">([\\s\\S]*?)<a href=\"/\\d+/\\d+/\\d+/\\d+/#comments\"")
                .matcher(dailyNewsPage);
        Boolean someUnloaded = false;// одна из новостей незагружена - значит и остальные
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yy");


        Pattern mPattern = Pattern.compile("<a href=\"(/\\d+/\\d+/\\d+/(\\d+))/\" rel=\"bookmark\" title=\"(.*?)\" alt=\"\">.*?</a></h2>");
        Pattern infoPattern = Pattern.compile("<strong>(.*?)</strong>&nbsp;\\|\\s*(\\d+\\.\\d+\\.\\d+)\\s*\\|");
        Pattern textPattern = Pattern.compile("<div class=\"entry\" id=\".*?\">([\\s\\S]*?)</div><div class=\"postmetadata\"");
        Pattern imagePattern = Pattern.compile("<center><img[^>]*?src=\"(.*?)\"");
        while (postsMatcher.find()) {
            String postData = postsMatcher.group(1);
            Matcher m = mPattern.matcher(postData);

            if (m.find()) {
                String id = "http://4pda.ru" + m.group(1);

                if (!someUnloaded && findByTitle(id)!=null) continue;
                someUnloaded = true;

                News topic = new News(id, Html.fromHtml(m.group(3)).toString());

                Matcher infoMatcher = infoPattern.matcher(postData);
                if (infoMatcher.find()) {
                    Date _pubDate = dateFormat.parse(infoMatcher.group(2));
                    topic.setNewsDate(DateTimeExternals.getDateString(_pubDate));
                    topic.setAuthor(Html.fromHtml(infoMatcher.group(1)));
                }

                Matcher textMatcher = textPattern.matcher(postData);
                if (textMatcher.find()) {
                    topic.setDescription(Html.fromHtml(textMatcher.group(1).replaceAll("<img.*?/>", "")));
                }

                Matcher imageMatcher = imagePattern.matcher(postData);
                if (imageMatcher.find()) {
                    topic.setImgUrl(imageMatcher.group(1));
                }

                topic.setPage(page);
                add(topic);
            }
        }

        newsCountInt = Math.max(getNewsCount(), lastPageNum(dailyNewsPage, page));
        return dailyNewsPage;
    }

    public News findByTitle(String title) {
        title = title.toLowerCase().replace(" ", "");
        for (int i = 0; i < size(); i++) {
            News topic = get(i);
            if (topic.getTitle().toString().replace(" ", "").equalsIgnoreCase(title))
                return topic;
        }
        return null;
    }
}
