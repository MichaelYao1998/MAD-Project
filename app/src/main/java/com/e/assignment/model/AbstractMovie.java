package com.e.assignment.model;

public class AbstractMovie implements Movie {
    private String id;
    private String title;
    private String year;
    private String poster;
    public AbstractMovie(String id, String title, String year, String poster){
        this.id=id;
        this.title = title;
        this.year = year;
        this.poster=poster;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getYear() {
        return year;
    }

    @Override
    public String getPoster() {
        return poster;
    }
}
