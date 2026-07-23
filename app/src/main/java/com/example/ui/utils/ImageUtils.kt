package com.example.ui.utils

import android.content.Context
import com.example.R

object ImageUtils {
    fun getDrawableResId(context: Context, name: String?): Int {
        if (name.isNull_orEmpty()) return R.drawable.img_garden_banner_1784810613294
        val resId = context.resources.getIdentifier(name, "drawable", context.packageName)
        return if (resId != 0) resId else R.drawable.img_garden_banner_1784810613294
    }
}
private fun String?.isNull_orEmpty(): Boolean = this == null || this.trim().isEmpty()
