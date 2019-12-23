package com.arijeet.newsapp.models;

import androidx.annotation.NonNull;

import com.arijeet.newsapp.models.entry.Entry;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.io.Serializable;
import java.util.List;

@Root(name = "Feed", strict = false)
public class Feed implements Serializable {
    @Element(name = "id")
    private String id;

    @Element(name = "title")
    private String title;

    @Element(name = "subtitle")
    private String subtitle;

    //private String link;
    @Element(name = "updated")
    private String updated;

    @Element(name="icon")
    private String icon;

    @Element(name = "logo")
    private String logo;

    @ElementList(inline = true,name = "entry",data = false,entry = "entry")
    private List<Entry> entry;

    public String getId() {
        return id;
    }

    public List<Entry> getEntry() {
        return entry;
    }

    public void setEntry(List<Entry> entry) {
        this.entry = entry;
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

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getUpdated() {
        return updated;
    }

    public void setUpdated(String updated) {
        this.updated = updated;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

   // @NonNull
    @Override
    public String toString() {
        return "Feed : \n [Entrys: \n "+ entry +"] \n";
    }
}
