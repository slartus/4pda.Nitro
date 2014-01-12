package ru.pda.api;

import java.util.ArrayList;

import ru.pda.interfaces.IHttpClient;
import ru.pda.interfaces.ListInfo;
import ru.pda.interfaces.forum.News;

/**
 * Created by slartus on 12.01.14.
 */
public class NewsApi {
    public static ArrayList<News> getNews(IHttpClient client,ListInfo listInfo){

        // тут магия парсинга
        return new ArrayList<News>();
    }
}
