package ru.forpda.api;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by slinkin on 21.01.14.
 */
public class TopicResult {
    private CharSequence title;
    private CharSequence description;
    private CharSequence body;
    private int page = 1;
    private int pagesCount;
    private int lastPageStart;
    private CharSequence entry;
    private CharSequence topicId;

    /**
     * Разбор ссылки топика: идентификатор топика, entry
     *
     * @param topicUrl
     */
    public void parseUrl(CharSequence topicUrl) {
        Matcher m = Pattern.compile("showtopic=(\\d+)").matcher(topicUrl);
        if (m.find())
            topicId = m.group(1);
        m = Pattern.compile("#(entry\\d+)").matcher(topicUrl);
        if (m.find())
            entry = m.group(1);

    }

    /**
     * Разбор шапки страницы топика: вытаскиваем страницы, название темы, описание темы
     *
     * @param header
     */
    public void parseHeader(CharSequence header) {
        final Pattern titlePattern = Pattern.compile("<title>(.*?) - 4PDA</title>");
//        final Pattern descriptionPattern = Pattern.compile("<div class=\"topic_title_post\">(.*?)<");
//        final Pattern moderatorTitlePattern = Pattern.compile("onclick=\"return setpidchecks\\(this.checked\\);\".*?>&nbsp;(.*?)<");
//        final Pattern pagesCountPattern = Pattern.compile("var pages = parseInt\\((\\d+)\\);");
//        final Pattern lastPageStartPattern = Pattern.compile("/forum/index.php\\?showtopic=\\d+&amp;st=(\\d+)");
//        final Pattern currentPagePattern = Pattern.compile("<span class=\"pagecurrent\">(\\d+)</span>");

        Matcher m = titlePattern.matcher(header);
        if (m.find()) {
            title = m.group(1);
        }

//        m = descriptionPattern.matcher(header);
//        if (m.find()) {
//            description = m.group(1).replace(title + ", ", "");
//        } else {
//            m = moderatorTitlePattern.matcher(header);
//            if (m.find())
//                description = m.group(1).replace(title + ", ", "");
//        }
//
//        m = pagesCountPattern.matcher(header);
//        if (m.find()) {
//            pagesCount=Integer.parseInt(m.group(1))+1;
//        }
//
//        m = lastPageStartPattern.matcher(header);
//        while (m.find()) {
//            lastPageStart=Integer.parseInt(m.group(1));
//        }
//
//        m = currentPagePattern.matcher(header);
//        if (m.find()) {
//            page=Integer.parseInt(m.group(1));
//        }
    }

    /**
     * Id топика
     */
    public CharSequence getTopicId() {
        return topicId;
    }

    /**
     * Тело топика
     */
    public CharSequence getBody() {
        return body;
    }

    public void setBody(CharSequence body) {
        this.body = body;
    }

//    /**
//     * Текущая страница топика
//     */
//    public int getPage() {
//        return page;
//    }
//
//    /**
//     *  Общее кол-во страниц топика
//     */
//    public int getPagesCount() {
//        return pagesCount;
//    }
//
//    public int getLastPageStart() {
//        return lastPageStart;
//    }
}
