package com.rizwansayyed.zene.ui.main.ent.utils

import kotlin.random.Random

class LiveReadersCounter(
    private val min: Int = 5000,
    private val max: Int = 25000
) {
    companion object {
        var current = Random.nextInt(9000, 14001)
    }

    private var trendUp = Random.nextBoolean()
    private var trendStepsLeft = Random.nextInt(8, 16)
    private var volatility = Random.nextFloat() * 0.4f + 0.3f

    fun next(): Int {
        if (trendStepsLeft <= 0) {
            trendUp = !trendUp
            trendStepsLeft = Random.nextInt(8, 16)
            volatility = Random.nextFloat() * 0.4f + 0.3f
        }

        val delta = when {
            trendUp -> {
                if (Random.nextInt(100) < 15) {
                    -Random.nextInt(30, 101)
                } else {
                    Random.nextInt(40, 151)
                }
            }
            else -> {
                if (Random.nextInt(100) < 15) {
                    Random.nextInt(30, 101)
                } else {
                    -Random.nextInt(40, 151)
                }
            }
        }

        current += delta
        trendStepsLeft--

        if (current >= max) {
            current = max - Random.nextInt(0, 501)
            trendUp = false
            trendStepsLeft = Random.nextInt(6, 13)
        }

        if (current <= min) {
            current = min + Random.nextInt(0, 501)
            trendUp = true
            trendStepsLeft = Random.nextInt(6, 13)
        }

        return current
    }
}