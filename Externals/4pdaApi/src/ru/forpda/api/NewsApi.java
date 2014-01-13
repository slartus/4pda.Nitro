package ru.forpda.api;

import java.util.ArrayList;

import ru.forpda.interfaces.IHttpClient;
import ru.forpda.interfaces.ListInfo;
import ru.forpda.interfaces.forum.News;

/**
 * Created by slartus on 12.01.14.
 */
public class NewsApi {
    public static ArrayList<News> getNews(IHttpClient client,ListInfo listInfo){

        // тут магия парсинга
        return new ArrayList<News>();
    }
}
