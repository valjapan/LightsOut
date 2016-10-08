package com.valkyrie.nabeshimamac.lightsout.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.valkyrie.nabeshimamac.lightsout.MyApplication;
import com.valkyrie.nabeshimamac.lightsout.R;
import com.valkyrie.nabeshimamac.lightsout.manager.PreferencesManager;
import com.valkyrie.nabeshimamac.lightsout.view.TutorialLightsOutView;

/**
 * Tutorialのユーザーがプレイする部分
 * Info→Play(今ここ)→通常問題
 */

public class TutorialPlayActivity extends AppCompatActivity implements TutorialLightsOutView.OnTutorialClearListener {

    public static Intent createIntent(Context context) {
        return new Intent(context, TitleActivity.class);
    }

    private TextView title, contents, contents2;
    private TutorialLightsOutView tutorialLightsOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial_play);
        Bundle bundle = new Bundle();
        ((MyApplication) getApplication()).analytics.logEvent(FirebaseAnalytics.Event.TUTORIAL_BEGIN, bundle);

        title = (TextView) findViewById(R.id.tutorialTitle);
        contents = (TextView) findViewById(R.id.tutorialContents);
        contents2 = (TextView) findViewById(R.id.tutorialContents2);
        tutorialLightsOut = (TutorialLightsOutView) findViewById(R.id.tutorialLightsOutView);
        // IDの関連付け

        Typeface sign = Typeface.createFromAsset(getAssets(), "SignPainter.otf");
        Typeface gothicAdobe = Typeface.createFromAsset(getAssets(), "AdobeGothicStd-Bold.otf");
        // フォントの読み込み

        title.setTypeface(sign);
        contents.setTypeface(gothicAdobe);
        contents2.setTypeface(gothicAdobe);
        // フォントの指定

        // SharedPreferencesからMuteかどうかの設定を読み込む
        tutorialLightsOut.setSound(PreferencesManager.getInstance(this).isSound());
        tutorialLightsOut.setColor(PreferencesManager.getInstance(this).getThemeColor());
        tutorialLightsOut.setOnTutorialClearListener(this);

    }

    @Override
    public void onTutorialClear() {
        Toast.makeText(this, "チュートリアルクリア！", Toast.LENGTH_SHORT).show();
        PreferencesManager.getInstance(this).checkTutorialEnd();
        finish();
    }
}
