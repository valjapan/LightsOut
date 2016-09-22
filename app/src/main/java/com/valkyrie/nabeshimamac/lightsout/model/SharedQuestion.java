package com.valkyrie.nabeshimamac.lightsout.model;

import java.io.Serializable;
import java.util.Date;

/**
 * ActiveAndroidに問題を保存するためのmodelClass
 */
public class SharedQuestion implements Serializable {

    public String title;
    public String board;
    public int width;
    public int height;

    public SharedQuestion() {
    }

    public SharedQuestion(String title, String board, int width, int height) {
        this.title = title;
        this.board = board;
        this.width = width;
        this.height = height;
    }

    public static SharedQuestion valueOf(Question question) {
        return new SharedQuestion(
                question.title,
                question.board,
                question.width,
                question.height
        );
    }

    public Question toQuestion() {
        return new Question(
                title,
                board,
                width,
                height,
                new Date()
        );
    }

}