package com.valkyrie.nabeshimamac.lightsout.view;

import android.content.Context;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

/**
 * Tutorial版LightsOutViewのシステム
 */
public class TutorialLightsOutView extends LightsOutView {
    public static final String TAG = TutorialLightsOutView.class.getSimpleName();
    //TODO Tutorialで押させるポイントを書く
    private Point[] STORIES = {
            new Point(0, 0),
            new Point(1, 1),
            new Point(2, 2),
            new Point(0, 2),
            new Point(2, 0),
    };
    // 現在どのチュートリアルまでやったかを記録する場所
    private int tutorialIndex = 0;
    // クリア時に呼ばれるリスナー
    private OnTutorialClearListener tutorialClearListener;

    public TutorialLightsOutView(Context context) {
        this(context, null);
    }

    public TutorialLightsOutView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TutorialLightsOutView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setBoardSize(3, 3);
    }

    @Override
    public void onClick(View v) {
        if (v instanceof Button) {
            final Point tapPoint = (Point) v.getTag();
            final Point tutorialPoint = STORIES[tutorialIndex];
            if (tutorialPoint.equals(tapPoint)) {
                // 通常のタップ処理を呼ぶ
                super.onClick(v);
                // チュートリアルを1つ進める
                tutorialIndex++;
                if (tutorialIndex == STORIES.length) {
                    // チュートリアルクリア
                    if (tutorialClearListener != null) {
                        tutorialClearListener.onTutorialClear();
                    }
                }
            } else {
                // 間違った場所をタップした際の処理
                String message = "";
                // 間違った場所をタップ
                final int dx = tapPoint.x - tutorialPoint.x;
                //縦の座標
                final int dy = tapPoint.y - tutorialPoint.y;
                //横の座標

                if (dx > 0) {
                    message += "上に" + dx + "マス ";
                } else if (dx == 0) {
                    message += "";
                } else if (dx < 0) {
                    message += "下に" + -dx + "マス ";
                }

                if (dy > 0) {
                    message += "左に" + dy + "マス";
                }else if (dy == 0){
                    message += "";
                }else if (dy < 0) {
                    message += "右に" + -dy + "マス ";
                }
                message += "をタップしてね！";
                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
            }
        }
    }

    public OnTutorialClearListener getOnTutorialClearListener() {
        return tutorialClearListener;
    }

    public void setOnTutorialClearListener(OnTutorialClearListener tutorialClearListener) {
        this.tutorialClearListener = tutorialClearListener;

    }

    public interface OnTutorialClearListener {
        void onTutorialClear();
    }

}
