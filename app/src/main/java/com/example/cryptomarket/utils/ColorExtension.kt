package com.example.cryptomarket.utils

import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.graphics.ColorFilter
import android.graphics.PorterDuff
import android.graphics.drawable.*
import android.os.Build
import androidx.annotation.ColorInt
import androidx.annotation.RequiresApi


fun Drawable.overrideColor(@ColorInt colorInt: Int) {
    when (val mutate = this.mutate()) {
        is GradientDrawable -> mutate.setColor(colorInt)
        is ShapeDrawable -> mutate.paint.color = colorInt
        is ColorDrawable -> mutate.color = colorInt
        else -> {
            setTint(colorInt)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                setTintBlendMode(BlendMode.SRC_OVER)
            } else {
                setTintMode(PorterDuff.Mode.SRC_OVER)
            }
        }
    }
}