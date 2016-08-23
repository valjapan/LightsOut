package com.valkyrie.nabeshimamac.lightsout;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table(name = "Questions")
public class Question extends Model {

    @Column(name = "Title")
    public String title;
    @Column(name = "Board")
    public String board;
    @Column(name = "Size")
    public int size;

    public Question() {
        super();
    }

    public Question(String title, String board, int size) {
        super();
        this.title = title;
        this.board = board;
        this.size = size;
    }
}