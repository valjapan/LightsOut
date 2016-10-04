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

/**
 * tutorial画面のActivity
 */
public class TutorialInformationActivity extends AppCompatActivity {
    private ViewFlipper viewFlipper;
    private Button buttonTutorial;
    private TextView titleTextView1 ,contentsTextView1 ,titleTextView2 ,contentsTextView2,
                    titleTextView3 , contentsTextView3Of1 ,contentsTextView3Of2 ,contentsTextView3Of3;


    public static Intent createIntent(Context context) {
        return new Intent(context, TitleActivity.class);
    }

    //チュートリアル部分

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial_info);
        viewFlipper = (ViewFlipper) findViewById(R.id.viewFlipper);
        buttonTutorial = (Button) findViewById(R.id.buttonTutorial);

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

        Typeface sign = Typeface.createFromAsset(getAssets(), "SignPainter.otf");
        Typeface gothicAdobe = Typeface.createFromAsset(getAssets(), "AdobeGothicStd-Bold.otf");

        titleTextView1.setTypeface(sign);
        titleTextView2.setTypeface(sign);
        titleTextView3.setTypeface(sign);

        contentsTextView1.setTypeface(gothicAdobe);
        contentsTextView2.setTypeface(gothicAdobe);
        contentsTextView3Of1.setTypeface(gothicAdobe);
        contentsTextView3Of2.setTypeface(gothicAdobe);
        contentsTextView3Of3.setTypeface(gothicAdobe);

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
