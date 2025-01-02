package com.example.cubres

import android.graphics.Color
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TableLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class Theme(private val activity: AppCompatActivity) {

    private var isDarkTheme = false
    private lateinit var bestScoreTextView: TextView
    private lateinit var scoreTextView: TextView
    private lateinit var mainView: View
    private lateinit var fogLayer1: View
    private lateinit var fogLayer2: View
    private lateinit var fogLayer3: View
    private lateinit var fogLayer4: View
    private lateinit var clearButton: ImageButton
    private lateinit var themeButton: ImageButton
    private lateinit var tableLayout: TableLayout
    private lateinit var imageView: ImageView

    init {
        initializeItem()
    }

    fun initializeItem() {
        mainView = activity.findViewById(R.id.main)
        bestScoreTextView = activity.findViewById(R.id.bestScore)
        scoreTextView = activity.findViewById(R.id.score)
        fogLayer1 = activity.findViewById(R.id.fogLayer1)
        fogLayer2 = activity.findViewById(R.id.fogLayer2)
        fogLayer3 = activity.findViewById(R.id.fogLayer3)
        fogLayer4 = activity.findViewById(R.id.fogLayer4)
        clearButton = activity.findViewById(R.id.clearButton)
        themeButton = activity.findViewById(R.id.themeButton)
        tableLayout = activity.findViewById(R.id.tableLayout)
        imageView = activity.findViewById(R.id.image)
    }

    fun applyTheme() {
        val systemColor = if (isDarkTheme) Color.DKGRAY else Color.WHITE
        mainView.setBackgroundColor(systemColor)
        activity.window.navigationBarColor = systemColor
        activity.window.statusBarColor = systemColor

        val fogLayerColor = if (isDarkTheme) Color.parseColor("#60000000") else Color.parseColor("#60FFFFFF")
        fogLayer2.setBackgroundColor(fogLayerColor)
        fogLayer3.setBackgroundColor(fogLayerColor)
        fogLayer4.setBackgroundColor(fogLayerColor)

        val textColor = if (isDarkTheme) Color.WHITE else Color.BLACK
        bestScoreTextView.setTextColor(textColor)
        scoreTextView.setTextColor(textColor)

        clearButton.setImageResource(if (isDarkTheme) R.drawable.icon_clear_solid_white else R.drawable.icon_clear_solid)
        themeButton.setImageResource(if (isDarkTheme) R.drawable.icon_sun_solid_white else R.drawable.icon_moon_solid)

        imageView.visibility = if (isDarkTheme) View.VISIBLE else View.GONE
        tableLayout.alpha = if (isDarkTheme) 0.5f else 1.0f
    }

    fun toggleTheme() {
        isDarkTheme = !isDarkTheme
        applyTheme()
    }
}