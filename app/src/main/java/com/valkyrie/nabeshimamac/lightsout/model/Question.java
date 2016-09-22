package com.valkyrie.nabeshimamac.lightsout.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import java.util.Date;

/**
 * ActiveAndroidに問題を保存するためのmodelClass
 */
@Table(name = "Questions")
public class Question extends Model {

    @Column(name = "Title")
    public String title;
    @Column(name = "Board")
    public String board;
    @Column(name = "Width")
    public int width;
    @Column(name = "Height")
    public int height;
    @Column(name = "CreatedAt")
    public Date createdAt;
    @Column(name = "SharedKey")
    public String sharedKey;

    public Question() {
        super();
    }

    public Question(String title, String board, int width, int height, Date createdAt) {
        super();
        this.title = title;
        this.board = board;
        this.width = width;
        this.height = height;
        this.createdAt = createdAt;
    }

    public Question(String title, String board, int width, int height, Date createdAt, String sharedKey) {
        super();
        this.title = title;
        this.board = board;
        this.width = width;
        this.height = height;
        this.createdAt = createdAt;
        this.sharedKey = sharedKey;
    }
}