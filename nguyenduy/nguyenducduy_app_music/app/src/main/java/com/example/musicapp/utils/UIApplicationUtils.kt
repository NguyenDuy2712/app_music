package com.example.musicapp.utils

import android.app.Activity
import android.graphics.Color
import android.view.View
import android.view.WindowManager


object UIApplicationUtils {

    fun transparentStatusBar(activity: Activity, isTransparent: Boolean) {
        activity.window.apply {
            if (isTransparent) {
                decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
                statusBarColor = Color.TRANSPARENT

            } else {
                decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR

            }
        }
    }
}

