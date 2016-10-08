package com.valkyrie.nabeshimamac.lightsout.model;

import android.support.annotation.DrawableRes;

import com.valkyrie.nabeshimamac.lightsout.R;

/**
 * 色の設定
 */
public enum ThemeColors {
    PinkBlue(
            R.drawable.red_off_view,
            R.drawable.red_on_view,
            R.drawable.sky_blue_off_view,
            R.drawable.sky_blue_on_view
    ),
    YellowBlue(
            R.drawable.deep_blue_off_view,
            R.drawable.deep_blue_on_view,
            R.drawable.yellow_off_view,
            R.drawable.yellow_on_view
    ),
    OrangeGreen(
            R.drawable.green_off_view,
            R.drawable.green_on_view,
            R.drawable.orange_off_view,
            R.drawable.orange_on_view
    );
    @DrawableRes
    public int primaryOff;
    @DrawableRes
    public int primaryOn;
    @DrawableRes
    public int secondaryOff;
    @DrawableRes
    public int secondaryOn;

    ThemeColors(int primaryOff, int primaryOn, int secondaryOff, int secondaryOn) {
        this.primaryOff = primaryOff;
        this.primaryOn = primaryOn;
        this.secondaryOff = secondaryOff;
        this.secondaryOn = secondaryOn;
    }
}
