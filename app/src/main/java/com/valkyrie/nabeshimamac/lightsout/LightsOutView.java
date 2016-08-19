package com.valkyrie.nabeshimamac.lightsout;

import android.content.Context;
import android.graphics.Point;
import android.support.annotation.ColorInt;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class LightsOutView extends LinearLayout implements View.OnClickListener {

    private Button[][] btns;

    private boolean[][] flag;
    private Tap tapInstance;
    private int tapCount;

    private @ColorInt int blue;
    private @ColorInt int pink;

    private LightsOutListener listener;

    public LightsOutView(Context context) {
        this(context, null);
    }

    public LightsOutView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LightsOutView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inflate(context, R.layout.view_lights_out, this);

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
            if (listener != null) {
                listener.onButtonTapped(point.x, point.y, tapCount);
            }
            check(point.x, point.y);
        }
    }

    public void setOnLigitsOutListener(LightsOutListener listener) {
        this.listener = listener;
    }

    public void resetGame() {
        tapCount = 0;
        flag = new boolean[6][6];

        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                btns[i][j].setBackgroundColor(blue);
                flag[i][j] = false;
            }
        }
    }

    public void check(int line, int row) {
        checkFlag(line, row);
        // 左
        if (line > 0) {
            checkFlag(line - 1, row);
        }
        // 右
        if (line < 5) {
            checkFlag(line + 1, row);
        }
        // 上
        if (row > 0) {
            checkFlag(line, row - 1);
        }
        // 下
        if (row < 5) {
            checkFlag(line, row + 1);
        }
        updateFlags();
        setTapColor(line, row);
        if (judgeClear()) {
            // TODO クリアしたことをActivityに伝える
            if (listener != null) {
                listener.onClearListener();
            }
        }
    }

    public void checkFlag(int line, int row) {
        flag[line][row] = !flag[line][row];
        //TODO 全てが反転色になったらクリアを実装させること
    }

    public int getTapCount() {
        return tapCount;
    }

    private void loadButtons() {
        btns = new Button[6][6];
        btns[0][0] = (Button) findViewById(R.id.button);
        btns[0][1] = (Button) findViewById(R.id.button2);
        btns[0][2] = (Button) findViewById(R.id.button3);
        btns[0][3] = (Button) findViewById(R.id.button4);
        btns[0][4] = (Button) findViewById(R.id.button5);
        btns[0][5] = (Button) findViewById(R.id.button6);
        btns[1][0] = (Button) findViewById(R.id.button7);
        btns[1][1] = (Button) findViewById(R.id.button8);
        btns[1][2] = (Button) findViewById(R.id.button9);
        btns[1][3] = (Button) findViewById(R.id.button10);
        btns[1][4] = (Button) findViewById(R.id.button11);
        btns[1][5] = (Button) findViewById(R.id.button12);
        btns[2][0] = (Button) findViewById(R.id.button13);
        btns[2][1] = (Button) findViewById(R.id.button14);
        btns[2][2] = (Button) findViewById(R.id.button15);
        btns[2][3] = (Button) findViewById(R.id.button16);
        btns[2][4] = (Button) findViewById(R.id.button17);
        btns[2][5] = (Button) findViewById(R.id.button18);
        btns[3][0] = (Button) findViewById(R.id.button19);
        btns[3][1] = (Button) findViewById(R.id.button20);
        btns[3][2] = (Button) findViewById(R.id.button21);
        btns[3][3] = (Button) findViewById(R.id.button22);
        btns[3][4] = (Button) findViewById(R.id.button23);
        btns[3][5] = (Button) findViewById(R.id.button24);
        btns[4][0] = (Button) findViewById(R.id.button25);
        btns[4][1] = (Button) findViewById(R.id.button26);
        btns[4][2] = (Button) findViewById(R.id.button27);
        btns[4][3] = (Button) findViewById(R.id.button28);
        btns[4][4] = (Button) findViewById(R.id.button29);
        btns[4][5] = (Button) findViewById(R.id.button30);
        btns[5][0] = (Button) findViewById(R.id.button31);
        btns[5][1] = (Button) findViewById(R.id.button32);
        btns[5][2] = (Button) findViewById(R.id.button33);
        btns[5][3] = (Button) findViewById(R.id.button34);
        btns[5][4] = (Button) findViewById(R.id.button35);
        btns[5][5] = (Button) findViewById(R.id.button36);
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                final Point point = new Point(i, j);
                btns[i][j].setTag(point);
                btns[i][j].setOnClickListener(this);
            }
        }
    }

    public void updateFlags() {
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
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
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
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
