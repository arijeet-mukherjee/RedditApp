package com.arijeet.newsapp.models.entry;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import java.io.Serializable;

@Root(name = "Entry", strict = false)
public class Entry implements Serializable {
    @Element(name = "content")
    private String content;

    @Element(name = "author",required = false)
    private Author author;

    @Element(name = "title")
    private String title;

    @Element(name = "id")
    private String id;


    @Element(name = "updated")
    private String updated;

    public Entry() {

    }

    public Entry(String content, Author author, String title, String updated) {
        this.content = content;
        this.author = author;
        this.title = title;
        this.updated = updated;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUpdated() {
        return updated;
    }

    public void setUpdated(String updated) {
        this.updated = updated;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "\n\nEntry{" +
                "content='" + content + '\'' +
                ", author='" + author + '\'' +
                ", id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", updated='" + updated + '\'' +
                '}'
                +
                "\n----------------------------------------------------------------\n";
    }
}
