package org.alex.konon.sol.siteToOK.entity;

import javax.persistence.*;

@Entity
@Table(name = "article")
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    //private Date date;
    private String title;

    @Column(columnDefinition="TEXT")
    private String text;

    public Article(){}

    public Article(String text) {
        this.text = text;
    }

    public Article(String text, String title){
        this.text=text;
        this.title=title;
    }



    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    /*public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }*/

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
