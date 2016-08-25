package com.valkyrie.nabeshimamac.lightsout;

import android.content.Context;
import android.graphics.Point;
import android.support.annotation.ColorInt;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

public class LightsOutView extends LinearLayout implements View.OnClickListener {
    public static final int MODE_GAME = 0;
    public static final int MODE_MAKE = 1;

    private int boardSize = 6;

    private Button[][] btns;
    private boolean[][] flag;

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
                flag[i][j] = false;
            }
        }
    }

    public void check(int line, int row) {
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

    public void setMode(int mode) {
        this.mode = mode;
    }

    public boolean[][] getFlags() {
        return flag;
    }

    public String getFlagsToString() {
        String data = "";
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                if (flag[i][j]) {
                    data = data + "1";
                } else {
                    data = data + "0";
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
    }

    public void setBoardSize(int boardSize) {
        this.boardSize = boardSize;
        loadButtons();
        resetGame();
    }

    public int getBoardSize() {
        return boardSize;
    }

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
    }

    private void setTapColor(int line, int row) {
        if (flag[line][row]) {
            btns[line][row].setBackground(getContext().getDrawable(R.drawable.red_on_view));
        } else {
            btns[line][row].setBackground(getContext().getDrawable(R.drawable.blue_on_view));
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
    }

    public interface LightsOutListener {
        // ますをタップした時に呼ばれる
        void onButtonTapped(int line, int row, int tapCount);

        // Gameをクリアした時に呼ばれる
        void onClearListener();
    }

}
