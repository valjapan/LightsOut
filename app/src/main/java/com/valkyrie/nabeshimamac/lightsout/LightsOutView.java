package com.valkyrie.nabeshimamac.lightsout;

import android.content.Context;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

public class LightsOutView extends LinearLayout implements View.OnClickListener {
    public static final int MODE_GAME = 0;
    public static final int MODE_MAKE = 1;
    //MODE_GAME(プレイ専用)だったら0
    //MODE_MAKE(作ってからそれをプレイする場合)だったら1

    private int boardSize = 6;
    //デフォルトはマス目が6×6

    private Button[][] btns;
    private boolean[][] flag;
    //設置するボタンを動的に設置する

    private Tap tapInstance;
    private int tapCount;

    // 0 -> Game, 1 -> Make
    private int mode = MODE_GAME;

    private
    @ColorInt
    int blue;
    private
    @ColorInt
    int pink;
    //使用色の宣言

    private LightsOutListener listener;

    public LightsOutView(Context context) {
        this(context, null);
    }

    public LightsOutView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LightsOutView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.setOrientation(VERTICAL);

        blue = ContextCompat.getColor(getContext(), R.color.colorBlueOriginal);
        pink = ContextCompat.getColor(getContext(), R.color.colorPinkOriginal);

        tapInstance = new Tap(getContext());

        loadButtons();
        resetGame();
    //ボタンを動的に宣言するところ
    }

    @Override
    public void onClick(View v) {
        if (v instanceof Button) {
            Point point = (Point) v.getTag();
            tapCount++;
            tapInstance.play();
            check(point.x, point.y);
            if (listener != null) {
                listener.onButtonTapped(point.x, point.y, tapCount);
                //動的にボタンを設置するからonClickが書かれない
                //そこでjavaで一つずつ入れていく。
            }
        }
    }

    public void setOnLigitsOutListener(LightsOutListener listener) {
        this.listener = listener;
    }

    public void resetGame() {
        tapCount = 0;
        flag = new boolean[boardSize][boardSize];

        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                btns[i][j].setBackgroundColor(blue);
                //全てを青色に設置
                flag[i][j] = false;
                //Boolean型で統一する
            }
        }
        //リセットの内容
    }

    public void check(int line, int row) {
        //プレイ時の色のチェック内容
        checkFlag(line, row);
        if (mode == MODE_GAME) {
            if (line > 0) {
                // 左
                checkFlag(line - 1, row);
            }
            if (line < boardSize - 1) {
                // 右
                checkFlag(line + 1, row);
            }
            if (row > 0) {
                // 上
                checkFlag(line, row - 1);
            }
            if (row < boardSize - 1) {
                // 下
                checkFlag(line, row + 1);
            }
        }
        updateFlags();
        setTapColor(line, row);
        if (mode == MODE_GAME && judgeClear()) {
            //クリアしたことをActivityに伝える
            if (listener != null) {
                listener.onClearListener();
            }
        }
    }

    public void checkFlag(int line, int row) {
        flag[line][row] = !flag[line][row];
        //全てが反転色になったらクリアを実装させる
    }

    public int getTapCount() {
        return tapCount;
    }

    private void loadButtons() {
        removeAllViews();
        btns = new Button[boardSize][boardSize];
        int margin = 8;
        LayoutParams columnLayoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1);
        LayoutParams rowLayoutParams = new LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1);
        rowLayoutParams.setMargins(margin, margin, margin, margin);
        //ボタンの配置、プロパティの設定

        for (int i = 0; i < boardSize; i++) {
            LinearLayout rowLayout = new LinearLayout(getContext());
            rowLayout.setOrientation(HORIZONTAL);
            for (int j = 0; j < boardSize; j++) {
                final Point point = new Point(i, j);
                btns[i][j] = new Button(getContext());
                btns[i][j].setText("");
                btns[i][j].setTag(point);
                btns[i][j].setOnClickListener(this);
                rowLayout.addView(btns[i][j], rowLayoutParams);
            }
            this.addView(rowLayout, columnLayoutParams);
        }
    }

    public int getMode() {
        return mode;
    }
    //Modeの取得

    public void setMode(int mode) {
        this.mode = mode;
    }
    //取得したModeの設置

    public boolean[][] getFlags() {
        return flag;
    }
    //押されているか押されていないかの確認

    public String getFlagsToString() {
        //押されているかどうかの設置
        String data = "";
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                if (flag[i][j]) {
                    data = data + "1";
                    //押されていれば1で0と1の文字列にする
                } else {
                    data = data + "0";
                    //押されなかったら0
                }
            }
        }
        return data;
    }

    public void setFlagsFromString(String flagPositions) {
        if (flagPositions.length() != boardSize * boardSize) {
            return;
        }
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                flag[i][j] = flagPositions.charAt(i * boardSize + j) == '1';
            }
        }
        //boardSizeの配置
    }

    public void setBoardSize(int boardSize) {
        this.boardSize = boardSize;
        loadButtons();
        resetGame();
        //boardSizeの配置

    }

    public int getBoardSize() {
        return boardSize;
    }
    //boardSizeの配置


    public void updateFlags() {
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                if (flag[i][j]) {
                    btns[i][j].setBackgroundColor(pink);
                } else {
                    btns[i][j].setBackgroundColor(blue);
                }
            }
        }
        //押されているかどうかを更新する
    }

    private void setTapColor(int line, int row) {
        if (flag[line][row]) {
            Drawable drawablePink = ResourcesCompat.getDrawable(getResources(),R.drawable.red_on_view,null);
            btns[line][row].setBackground(drawablePink);
            //押されたらピンク
        } else {
            Drawable drawableBlue = ResourcesCompat.getDrawable(getResources(),R.drawable.blue_on_view,null);
            btns[line][row].setBackground(drawableBlue);
            //押されていなかったら青
        }
    }

    private boolean judgeClear() {
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                if (!flag[i][j]) {
                    return false;
                }
            }
        }
        return true;
        //押されているかどうかの判断すること
    }

    public interface LightsOutListener {
        // ますをタップした時に呼ばれる
        void onButtonTapped(int line, int row, int tapCount);

        // Gameをクリアした時に呼ばれる
        void onClearListener();
    }

}
