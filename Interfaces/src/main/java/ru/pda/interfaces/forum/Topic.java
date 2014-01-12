package ru.pda.interfaces.forum;

import java.util.Date;

/**
 * Created by slartus on 12.01.14.
 */
public class Topic implements IListItem {
    private CharSequence id;
    private CharSequence title;
    private Boolean hasUnreadPosts;
    private CharSequence description;
    private Date lastPostDate;
    private CharSequence lastPostAuthor;
    private CharSequence forumTitle;

    public Topic(CharSequence id, CharSequence title) {
        this.id = id;
        this.title = title;
    }

    /**
     * Возвращает идентификатор топика
     *
     * @return
     */
    public CharSequence getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    /**
     * Возвращает заголовок топика
     *
     * @return
     */
    public CharSequence getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Возвращает есть ли непрочитанные сообщения
     *
     * @return
     */
    public Boolean getHasUnreadPosts() {
        return hasUnreadPosts;
    }

    public void setHasUnreadPosts(Boolean hasUnreadPosts) {
        this.hasUnreadPosts = hasUnreadPosts;
    }

    /**
     * Возвращает описание топика
     *
     * @return
     */
    public CharSequence getDescription() {
        return description;
    }

    public void setDescription(CharSequence description) {
        this.description = description;
    }

    /**
     * Возвращает дату последнего поста в топике
     *
     * @return
     */
    public Date getLastPostDate() {
        return lastPostDate;
    }

    public void setLastPostDate(Date lastPostDate) {
        this.lastPostDate = lastPostDate;
    }

    /**
     * Возвращает ник автора последнего поста  в топике
     *
     * @return
     */
    public CharSequence getLastPostAuthor() {
        return lastPostAuthor;
    }

    public void setLastPostAuthor(CharSequence lastPostAuthor) {
        this.lastPostAuthor = lastPostAuthor;
    }

    /**
     * Название форума, в котором лежит топик
     * @return
     */
    public CharSequence getForumTitle() {
        return forumTitle;
    }

    public void setForumTitle(CharSequence forumTitle) {
        this.forumTitle = forumTitle;
    }
}
