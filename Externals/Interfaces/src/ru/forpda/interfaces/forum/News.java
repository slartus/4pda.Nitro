package ru.forpda.interfaces.forum;

import java.util.Date;

/**
 * Created by slartus on 12.01.14.
 */
public class News implements IListItem {
    private CharSequence id;
    private CharSequence title;
    private Date newsDate;
    private CharSequence author;
    private CharSequence description;
    private CharSequence imgUrl;
    private int page;

    public News(CharSequence id, CharSequence title) {
        this.id = id;
        this.title = title;
    }

    public CharSequence getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public CharSequence getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getNewsDate() {
        return newsDate;
    }

    public void setNewsDate(Date newsDate) {
        this.newsDate = newsDate;
    }

    public CharSequence getAuthor() {
        return author;
    }

    public void setAuthor(CharSequence author) {
        this.author = author;
    }

    public CharSequence getDescription() {
        return description;
    }

    public void setDescription(CharSequence description) {
        this.description = description;
    }

    public CharSequence getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(CharSequence imgUrl) {
        this.imgUrl = imgUrl;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }
}
