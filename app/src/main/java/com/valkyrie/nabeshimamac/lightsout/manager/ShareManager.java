package com.valkyrie.nabeshimamac.lightsout.manager;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.text.TextUtils;

/**
 * Created by NabeshimaMAC on 2016/09/22.
 */

public class ShareManager {
    private static final String TWITTER_ID = "com.twitter.android";

    public static void shareTwitter(Activity activity, String shareText) {
        if (isShareAppInstall(activity, TWITTER_ID)) {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_SEND);
            intent.setPackage(TWITTER_ID);
            intent.setType("image/png");
            intent.putExtra(Intent.EXTRA_TEXT, shareText);
            activity.startActivity(intent);
        } else {
            shareAppDl(activity, TWITTER_ID);
        }
    }

    // アプリがインストールされているかチェック
    private static Boolean isShareAppInstall(Activity activity, String shareId) {
        try {
            PackageManager pm = activity.getPackageManager();
            pm.getApplicationInfo(shareId, PackageManager.GET_META_DATA);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    // アプリが無かったらGooglePlayに飛ばす
    private static void shareAppDl(Activity activity, String shareId) {
        Uri uri = Uri.parse("market://details?id=" + shareId);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        activity.startActivity(intent);
    }

    public static String createShareQuestionUrl(String shareKey) {
        if (TextUtils.isEmpty(shareKey)) {
            return "";
        }
        return "https://lightsout.game/questions/" + shareKey;
    }

}
