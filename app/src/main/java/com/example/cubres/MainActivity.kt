package com.example.cubres

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.TableLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var figureGameplay: FigureGameplay
    private lateinit var figureScore: FigureScore
    private lateinit var theme: Theme
    private lateinit var adaptiveApp: AdaptiveApp

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        window.navigationBarColor = Color.WHITE
        window.statusBarColor = Color.WHITE

        val table = findViewById<TableLayout>(R.id.tableLayout)
        val bestScore = findViewById<TextView>(R.id.bestScore)
        val score = findViewById<TextView>(R.id.score)

        figureScore = FigureScore(this, bestScore, score)
        figureGameplay = FigureGameplay(this, table, figureScore)

        theme = Theme(this)
        adaptiveApp = AdaptiveApp(this)
    }

    fun clearClick(view: View) {
        figureGameplay.clearFields()
    }

    fun changeThemeClick(view: View) {
        theme.toggleTheme()
    }

    override fun onPause() {
        super.onPause()
        figureGameplay.pauseGame()
    }

    override fun onResume() {
        super.onResume()
        figureGameplay.resumeGame()
    }

}