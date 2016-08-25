package com.valkyrie.nabeshimamac.lightsout;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import java.util.Date;

@Table(name = "Questions")
public class Question extends Model {

    @Column(name = "Title")
    public String title;
    @Column(name = "Board")
    public String board;
    @Column(name = "Size")
    public int size;
    @Column(name = "CreatedAt")
    public Date createdAt;

    public Question() {
        super();
    }

    public Question(String title, String board, int size, Date createdAt) {
        super();
        this.title = title;
        this.board = board;
        this.size = size;
        this.createdAt = createdAt;
    }
}