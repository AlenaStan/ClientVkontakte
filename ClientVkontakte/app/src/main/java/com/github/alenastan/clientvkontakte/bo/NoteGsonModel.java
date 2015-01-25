package com.github.alenastan.clientvkontakte.bo;

import java.io.Serializable;

/**
 * Created by lena on 25.01.2015.
 */
public class NoteGsonModel implements Serializable {

    private String title;

    private String content;

    private Long id;
    private String url;

    public NoteGsonModel(Long id, String title, String content, String url) {
        this.title = title;
        this.content = content;
        this.id = id;
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public Long getId() {
        return id;
    }

    public String getContent() {
        return content;
    }
    public String getUrl() {
        return url;
    }
}