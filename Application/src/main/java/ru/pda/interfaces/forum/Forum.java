package ru.pda.interfaces.forum;

/**
 * Created by slartus on 12.01.14.
 */
public class Forum implements IListItem {
    private String id;
    private String title;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
