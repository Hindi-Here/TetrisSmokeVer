package com.example.cubres

import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class AdaptiveApp(private val activity: AppCompatActivity) {

    init {
        setHeaderContentSize()
    }

    fun setHeaderContentSize() {
        val bestScore: TextView = activity.findViewById(R.id.bestScore)
        val score: TextView = activity.findViewById(R.id.score)

        val width = activity.resources.displayMetrics.widthPixels
        val textSize = width / 40f
        val buttonSize = width / 24

        bestScore.textSize = textSize
        score.textSize = textSize

        setButtonSize(R.id.clearButton, (buttonSize * activity.resources.displayMetrics.density).toInt())
        setButtonSize(R.id.themeButton, (buttonSize * activity.resources.displayMetrics.density).toInt())
    }

    private fun setButtonSize(buttonId: Int, size: Int) {
        val button: ImageButton = activity.findViewById(buttonId)
        button.layoutParams = button.layoutParams.apply {
            width = size
            height = size
        }
    }
}