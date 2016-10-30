package com.valkyrie.nabeshimamac.lightsout.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.support.v4.content.res.ResourcesCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.valkyrie.nabeshimamac.lightsout.R;
import com.valkyrie.nabeshimamac.lightsout.activity.MakeActivity;
import com.valkyrie.nabeshimamac.lightsout.manager.TapManager;
import com.valkyrie.nabeshimamac.lightsout.model.ThemeColors;

/**
 * lightsOutView内のシステム
 */
public class LightsOutView extends LinearLayout implements View.OnClickListener {
    //MODE_GAME(プレイ専用)だったら0
    //MODE_MAKE(作ってからそれをプレイする場合)だったら1
    public static final int MODE_GAME = 0;
    public static final int MODE_MAKE = 1;

    //デフォルトはマス目が6×6
    private int boardHeight = 6;
    private int boardWidth = 6;

    private Button[][] btns;
    private boolean[][] flag;
    //設置するボタンを動的に設置する

    private TapManager tapInstance;
    private int tapCount;
    private boolean isSound = true;

    // タイルのカラー
    private Drawable drawablePrimaryOff;
    private Drawable drawablePrimaryOn;
    private Drawable drawableSecondaryOff;
    private Drawable drawableSecondaryOn;


    // 0 -> Game, 1 -> Make
    private int mode = MODE_GAME;

    public static Intent createIntent(Context context) {
        return new Intent(context, MakeActivity.class);
    }

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

        drawablePrimaryOff = ResourcesCompat.getDrawable(getResources(), R.drawable.red_off_view, null);
        drawablePrimaryOn = ResourcesCompat.getDrawable(getResources(), R.drawable.red_on_view, null);
        drawableSecondaryOff = ResourcesCompat.getDrawable(getResources(), R.drawable.sky_blue_off_view, null);
        drawableSecondaryOn = ResourcesCompat.getDrawable(getResources(), R.drawable.sky_blue_on_view, null);

        tapInstance = new TapManager(getContext());

        loadButtons();
        resetGame();
        //ボタンを動的に宣言するところ
    }

    @Override
    public void onClick(View v) {
        if (v instanceof Button) {
            Point point = (Point) v.getTag();
            tapCount++;
            if (isSound) {
                tapInstance.play();
            }
            check(point.x, point.y);
            if (listener != null) {
                listener.onButtonTapped(point.x, point.y, tapCount);
                //動的にボタンを設置するからonClickが書かれない
                //そこでjavaで一つずつ入れていく。
            }
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        loadButtons();
        updateFlags();
    }

    public void setOnLightsOutListener(LightsOutListener listener) {
        this.listener = listener;
    }

    public void resetGame() {
        tapCount = 0;
        flag = new boolean[boardHeight][boardWidth];

        for (int i = 0; i < boardHeight; i++) {
            for (int j = 0; j < boardWidth; j++) {
                btns[i][j].setBackground(drawableSecondaryOff);
                //全てを青色に設置
                flag[i][j] = false;
                //Boolean型で統一する
            }
        }
        //リセットの内容
    }

    public boolean[][] getAllFlags() {
        return flag;
    }

    public void check(int line, int row) {
        //プレイ時の色のチェック内容
        checkFlag(line, row);
        if (mode == MODE_GAME) {
            if (line > 0) {
                // 左
                checkFlag(line - 1, row);
            }
            if (line < boardHeight - 1) {
                // 右
                checkFlag(line + 1, row);
            }
            if (row > 0) {
                // 上
                checkFlag(line, row - 1);
            }
            if (row < boardWidth - 1) {
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
        btns = new Button[boardHeight][boardWidth];
        final int margin = 13;
        final int blockSize = Math.min(getWidth() / boardWidth, getHeight() / boardHeight);
        final LayoutParams columnLayoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        final LayoutParams rowLayoutParams = new LayoutParams(blockSize - margin * 2, blockSize - margin * 2);
        rowLayoutParams.setMargins(margin, margin, margin, margin);
        //ボタンの配置、プロパティの設定

        for (int i = 0; i < boardHeight; i++) {
            LinearLayout rowLayout = new LinearLayout(getContext());
            rowLayout.setOrientation(HORIZONTAL);
            for (int j = 0; j < boardWidth; j++) {
                final Point point = new Point(i, j);
                btns[i][j] = new Button(getContext());
                btns[i][j].setText("");
                btns[i][j].setTag(point);
                btns[i][j].setOnClickListener(this);
                rowLayout.addView(btns[i][j], rowLayoutParams);
            }
            this.addView(rowLayout, columnLayoutParams);
        }
        // Boardを中央によせる
        final int horizontalPadding = (getWidth() - blockSize * boardWidth) / 2;
        final int verticalPadding = (getHeight() - blockSize * boardHeight) / 2;
        setPadding(
                horizontalPadding,
                verticalPadding,
                horizontalPadding,
                verticalPadding
        );
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
        for (int i = 0; i < boardHeight; i++) {
            for (int j = 0; j < boardWidth; j++) {
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
        if (flagPositions.length() != boardHeight * boardWidth) {
            return;
        }
        for (int i = 0; i < boardHeight; i++) {
            for (int j = 0; j < boardWidth; j++) {
                flag[i][j] = flagPositions.charAt(i * boardWidth + j) == '1';
            }
        }
        //boardSizeの配置
    }

    public void setBoardSize(int width, int height) {
        if (width == 0 || height == 0) {
            return;
        }
        this.boardHeight = height;
        this.boardWidth = width;
        loadButtons();
        resetGame();
    }

    public void setBoardHeight(int boardHeight) {
        if (boardHeight == 0) {
            return;
        }
        this.boardHeight = boardHeight;
        loadButtons();
        resetGame();
        //boardSizeの配置
    }

    public void setBoardWidth(int boardWidth) {
        if (boardWidth == 0) {
            return;
        }
        this.boardWidth = boardWidth;
        loadButtons();
        resetGame();
        //boardWidth
    }

    public int getBoardHeight() {
        return boardHeight;
    }
    //boardSizeの配置

    public int getBoardWidth() {
        return boardWidth;
    }

    public void updateFlags() {
        for (int i = 0; i < boardHeight; i++) {
            for (int j = 0; j < boardWidth; j++) {
                if (flag[i][j]) {
                    btns[i][j].setBackground(drawablePrimaryOff);
                } else {
                    btns[i][j].setBackground(drawableSecondaryOff);
                }
            }
        }
        //押されているかどうかを更新する
    }

    private void setTapColor(int line, int row) {
        if (flag[line][row]) {
            btns[line][row].setBackground(drawablePrimaryOn);
            //押されたらピンク
        } else {
            btns[line][row].setBackground(drawableSecondaryOn);
            //押されていなかったら青
        }
    }

    private boolean judgeClear() {
        for (int i = 0; i < boardHeight; i++) {
            for (int j = 0; j < boardWidth; j++) {
                if (!flag[i][j]) {
                    return false;
                }
            }
        }
        return true;
        //押されているかどうかの判断すること
    }

    public boolean isSound() {
        return isSound;
    }

    public void setSound(boolean sound) {
        isSound = sound;
    }

    public void setColor(ThemeColors colors) {
        drawablePrimaryOff = ResourcesCompat.getDrawable(getResources(), colors.primaryOff, null);
        drawablePrimaryOn = ResourcesCompat.getDrawable(getResources(), colors.primaryOn, null);
        drawableSecondaryOff = ResourcesCompat.getDrawable(getResources(), colors.secondaryOff, null);
        drawableSecondaryOn = ResourcesCompat.getDrawable(getResources(), colors.secondaryOn, null);
        updateFlags();
    }
    // テーマカラーごとの設定

    public void setButtonEnabled(boolean bool) {
        for (int i = 0; i < boardHeight; i++) {
            for (int j = 0; j < boardWidth; j++) {
                btns[i][j].setEnabled(bool);
            }
        }
        //ボタンを固定（タップをできなく）する設定
    }

    public interface LightsOutListener {
        // ますをタップした時に呼ばれる
        void onButtonTapped(int line, int row, int tapCount);

        // Gameをクリアした時に呼ばれる
        void onClearListener();
    }

}
