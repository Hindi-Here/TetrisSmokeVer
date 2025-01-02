package com.example.cubres

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.LayerDrawable

class FigurePropetry {
    companion object {
        val figures: List<List<Pair<Int, Int>>> = listOf(
            listOf(Pair(0, 0)),
            listOf(Pair(0, 0), Pair(0, 1)),
            listOf(Pair(0, 0), Pair(0, 1), Pair(0, 2)),
            listOf(Pair(0, 0), Pair(0, 1), Pair(0, 2), Pair(0, 3)),

            listOf(Pair(0, 0), Pair(0, 1), Pair(1, 1)),
            listOf(Pair(0, 0), Pair(0, 1), Pair(0, 2), Pair(1, 2)),

            listOf(Pair(0, 0), Pair(0, 1), Pair(1, 0), Pair(1, 1)),
            listOf(Pair(0, 0), Pair(0, 1), Pair(1, 1), Pair(1, 2)),
            listOf(Pair(1, 0), Pair(0, 1), Pair(1, 1), Pair(2, 1))
        )

        val colors: List<Int> = listOf(
            Color.parseColor("#696055"),
            Color.parseColor("#f1ecf7"),
            Color.parseColor("#f5f1bd"),
            Color.parseColor("#bdf5d9"),
            Color.parseColor("#c7bdf5"),
            Color.parseColor("#f5bdca"),
            Color.parseColor("#a79bb4"),
            Color.parseColor("#9bb4b1")
        )

        fun shadows(figureColor: Int): LayerDrawable {
            val shadowDrawable = GradientDrawable().apply {
                setColor(Color.TRANSPARENT)
                cornerRadius = 20f
                setStroke(2, Color.BLACK)
            }

            val layers = arrayOf(shadowDrawable, ColorDrawable(figureColor))
            val layerDrawable = LayerDrawable(layers)
            layerDrawable.setLayerInset(1, 4, 4, 4, 4)
            return layerDrawable
        }

    }
}