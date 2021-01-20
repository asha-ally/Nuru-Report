package com.openinstitute.nuru.Database;

public class Initiatives {
    public int Id;
    public String title;
    public String user_id;
    public String narration;
    public String start_date;
    public String end_date;
    public String tags;
    public String summary;
    public String name;
    public String initiative_private;
    public String created_at;

    public Initiatives() {

    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Initiatives(int id, String title, String user_id, String narration, String start_date, String end_date, String tags, String initiative_private, String created_at, String name, String summary) {
        Id = id;
        this.title = title;
        this.user_id = user_id;
        this.narration = narration;
        this.start_date = start_date;
        this.end_date = end_date;
        this.tags = tags;
        this.initiative_private = initiative_private;
        this.created_at = created_at;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getNarration() {
        return narration;
    }

    public void setNarration(String narration) {
        this.narration = narration;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getInitiative_private() {
        return initiative_private;
    }

    public void setInitiative_private(String initiative_private) {
        this.initiative_private = initiative_private;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }
}

