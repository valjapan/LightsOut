package com.valkyrie.nabeshimamac.lightsout;

import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

public class MakeActivity extends AppCompatActivity {
    private LightsOutView lightsOutEachView;
    private List<Point> prePoints;
    EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make);

        lightsOutEachView = (LightsOutView) findViewById(R.id.lightsOutView2);
        lightsOutEachView.setMode(LightsOutView.MODE_MAKE);

        prePoints = new ArrayList<>();

        editText = (EditText) findViewById(R.id.titleEditText);
    }


    private void loadPrePoints() {
        for (Point point : prePoints) {
            lightsOutEachView.checkFlag(point.x, point.y);
        }
        lightsOutEachView.updateFlags();
    }

    void save(View v) {
        Question question = new Question();
        question.title = editText.getText().toString();
        question.board = lightsOutEachView.getFlagsToString();
        question.size = lightsOutEachView.getBoardSize();
        question.save();
        finish();
    }
}
