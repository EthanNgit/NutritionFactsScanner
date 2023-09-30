package com.example.nutritionlabeltest.custom;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;

public class NorthUI {

    /**
     *
     * @apiNote Use this onCreate to set the navbar to the background color and status bar to transparent mode (has to be done per activity)
     *
     */
    public static void setAndroidUI(Activity activity, int backgroundColor) {
        setStatusBarToTransparent(activity);
        setNavbarToBackground(activity, backgroundColor);
    }
    /**
     *
     * @apiNote Use this onCreate to set the status bar to transparent mode (has to be done per activity)
     */
    public static void setStatusBarToTransparent(Activity activity) {
        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        activity.getWindow().setStatusBarColor(Color.TRANSPARENT);
        // this lines ensure only the status-bar to become transparent without affecting the nav-bar
        activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }

    /**
     *
     * @apiNote Use this onCreate to set the navbar to the background color (has to be done per activity)
     */
    public static void setNavbarToBackground(Activity activity, int color) {
        activity.getWindow().setNavigationBarColor(ContextCompat.getColor(activity, color));
        activity.getWindow().setNavigationBarDividerColor(ContextCompat.getColor(activity, color));
    }

}
