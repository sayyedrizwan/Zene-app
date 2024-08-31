package com.rizwansayyed.zene.ui.settings.model

import com.rizwansayyed.zene.R

enum class SongQualityTypes(val value: String, val txt: Int) {
    PREMIUM_QUALITY("1440", R.string.premium_quality),
    HIGH_QUALITY("720", R.string.high_quality),
    STANDARD_QUALITY("480", R.string.standard_quality),
    LOW_QUALITY("240", R.string.low_quality)
}