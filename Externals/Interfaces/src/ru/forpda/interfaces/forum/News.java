package ru.forpda.interfaces.forum;

/**
 * Created by slartus on 12.01.14.
 */
public class News implements IListItem {
    private CharSequence id;
    private CharSequence title;
    private String newsDate;
    private CharSequence author;
    private CharSequence description;
    private CharSequence imgUrl;
    private int page;

    private CharSequence tagLink;
    private CharSequence tagName;
    private CharSequence tagTitle;
	
	private CharSequence sourceTitle;
	private CharSequence sourceUrl;
	
	private int commentsCount;

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

    public String getNewsDate() {
        return newsDate;
    }

    public void setNewsDate(String newsDate) {
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

    /**
     * Ссылка на раздел
     */
    public CharSequence getTagLink() {
        return tagLink;
    }

    public void setTagLink(CharSequence tagLink) {
        this.tagLink = tagLink;
    }

    /**
     * Имя раздела
     */
    public CharSequence getTagName() {
        return tagName;
    }

    public void setTagName(CharSequence tagName) {
        this.tagName = tagName;
    }

    /**
     * Название раздела
     */
    public CharSequence getTagTitle() {
        return tagTitle;
    }

    public void setTagTitle(CharSequence tagTitle) {
        this.tagTitle = tagTitle;
    }
	
	/*
	 *Количество комментариев
	 */
	
	public void setCommentsCount(int commentsCount)
	{
		this.commentsCount = commentsCount;
	}

	public int getCommentsCount()
	{
		return commentsCount;
	}
	
	/*
	 * Url источника
	 */

	public void setSourceUrl(CharSequence sourceUrl)
	{
		this.sourceUrl = sourceUrl;
	}

	public CharSequence getSourceUrl()
	{
		return sourceUrl;
	}
	
	/*
	 * Текст источника
	 */
	

	public void setSourceTitle(CharSequence sourceTitle)
	{
		this.sourceTitle = sourceTitle;
	}

	public CharSequence getSourceTitle()
	{
		return sourceTitle;
	}
	
}
