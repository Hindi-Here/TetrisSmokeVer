package com.example.cubres

import android.content.Context
import android.widget.TextView

class FigureScore(private val context: Context, private val bestScore: TextView, private val score: TextView) {

    private var _bestScore: Int = 0
    private var _score: Int = 0

    init {
        loadScore()
        updateBestScore()
        updateScore()
    }

    private fun loadScore() {
        val sharedPreferences = context.getSharedPreferences("game_prefs", Context.MODE_PRIVATE)
        _bestScore = sharedPreferences.getInt("best_score", 0)
    }

    fun saveBestScore(score: Int) {
        val sharedPreferences = context.getSharedPreferences("game_prefs", Context.MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            putInt("best_score", score)
            apply()
        }
        _bestScore = _score
        updateBestScore()
    }

    fun updateBestScore() {
        bestScore.text = context.getString(R.string.best_score, _bestScore)
    }

    fun updateScore() {
        score.text = context.getString(R.string.current_score, _score)
    }

    fun setScore(points: Int) {
        _score += points
        updateScore()
    }

    fun resetScore() {
        _score = 0
        updateScore()
    }

    fun getScore(): Int {
        return _score
    }

    fun getBestScore(): Int {
        return _bestScore
    }
}