package com.rizwansayyed.zene.ui.main.ent.utils

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import kotlin.math.abs

fun dynamicNameColor(name: String): Brush {
    val hash = name.hashCode()
    val hue1 = abs(hash % 360).toFloat()
    val hue2 = abs((hash / 7) % 360).toFloat()

    val color1 = Color.hsv(
        hue = hue1,
        saturation = 0.75f,
        value = 0.95f
    )

    val color2 = Color.hsv(
        hue = hue2,
        saturation = 0.85f,
        value = 0.95f
    )

    return Brush.linearGradient(listOf(color1, color2))
}