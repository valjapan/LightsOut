package com.valkyrie.nabeshimamac.lightsout.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.valkyrie.nabeshimamac.lightsout.MyApplication;
import com.valkyrie.nabeshimamac.lightsout.R;

import java.util.Locale;

/**
 * tutorial画面のActivity
 */
public class TutorialInformationActivity extends AppCompatActivity {
    private ViewFlipper viewFlipper;
    private Button buttonTutorial;
    private TextView titleTextView1, contentsTextView1, titleTextView2, contentsTextView2,
            titleTextView3, contentsTextView3Of1, contentsTextView3Of2, contentsTextView3Of3;
    private Locale locale = Locale.getDefault();


    public static Intent createIntent(Context context) {
        return new Intent(context, TitleActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial_info);
        viewFlipper = (ViewFlipper) findViewById(R.id.viewFlipper);
        //ViewFlipperのIDの関連付け
        buttonTutorial = (Button) findViewById(R.id.buttonTutorial);
        //ButtonのIDの関連付け


        buttonTutorial.setText("NEXT");

        Bundle bundle = new Bundle();
        ((MyApplication) getApplication()).analytics.logEvent(FirebaseAnalytics.Event.TUTORIAL_BEGIN, bundle);

        titleTextView1 = (TextView) findViewById(R.id.tutorialInfoTitle1);
        titleTextView2 = (TextView) findViewById(R.id.tutorialInfoTitle2);
        titleTextView3 = (TextView) findViewById(R.id.tutorialInfoTitle3);
        contentsTextView1 = (TextView) findViewById(R.id.tutorialInfoContents1);
        contentsTextView2 = (TextView) findViewById(R.id.tutorialInfoContents2);
        contentsTextView3Of1 = (TextView) findViewById(R.id.tutorialInfoContents3Of1);
        contentsTextView3Of2 = (TextView) findViewById(R.id.tutorialInfoContents3Of2);
        contentsTextView3Of3 = (TextView) findViewById(R.id.tutorialInfoContents3Of3);
        //TextViewのIDの関連付け

        Typeface sign = Typeface.createFromAsset(getAssets(), "SignPainter.otf");
        Typeface gothicAdobe = Typeface.createFromAsset(getAssets(), "AdobeGothicStd-Bold.otf");
        //フォントの読み込み

        titleTextView1.setTypeface(sign);
        titleTextView2.setTypeface(sign);
        titleTextView3.setTypeface(sign);
        contentsTextView1.setTypeface(gothicAdobe);
        contentsTextView2.setTypeface(gothicAdobe);
        contentsTextView3Of1.setTypeface(gothicAdobe);
        contentsTextView3Of2.setTypeface(gothicAdobe);
        contentsTextView3Of3.setTypeface(gothicAdobe);
        buttonTutorial.setTypeface(gothicAdobe);
        //フォントの指定

        if (locale.equals(Locale.JAPAN)) {
            contentsTextView1.setText("パネルをタップします");
            contentsTextView2.setText("触ったパネルと \n  上下左右の色が \n       反転します");
            contentsTextView3Of1.setText("全て\n");
            contentsTextView3Of2.setText("反転\n");
            contentsTextView3Of3.setText("させます\n");
        } else {
            contentsTextView1.setText("Tap the Panel\n");
            contentsTextView2.setText("The panel in contact \n with the tap the panel \n will be inverted");
            contentsTextView3Of1.setText("It reverses \n");
            contentsTextView3Of2.setText("the\n");
            contentsTextView3Of3.setText("all panel\n");
        }
    }

    public void next(View v) {
        if (viewFlipper.getDisplayedChild() < 2) {
            viewFlipper.showNext();
        } else {
            buttonTutorial.setText("OK");
            viewFlipper.setFlipInterval(500);
            //更新間隔(ms単位)
            Intent intent = new Intent(this, TutorialPlayActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
