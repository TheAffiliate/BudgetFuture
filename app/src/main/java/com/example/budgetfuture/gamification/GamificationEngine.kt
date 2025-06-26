package com.example.budgetfuture.gamification

import android.content.Context
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast

class GamificationEngine(
    private val context: Context,
    private val levelText: TextView,
    private val xpProgress: ProgressBar,
    private val badge: ImageView
) {
    private var xp = 0
    private var level = 1
    private var maxBudget = 1000f

    fun setMaxBudget(max: Float) {
        maxBudget = max
    }

    fun registerExpense(expense: Float, totalExpenses: Float) {
        if (totalExpenses >= maxBudget) {
            badge.visibility = ImageView.VISIBLE
            Toast.makeText(context, "🎯 Max budget hit! +10XP", Toast.LENGTH_SHORT).show()
            xp += 10
            if (xp >= 100) {
                level++
                xp -= 100
                Toast.makeText(context, "🎉 You leveled up to Level $level!", Toast.LENGTH_SHORT).show()
            }
            levelText.text = "Level: $level (XP: $xp/100)"
            xpProgress.progress = xp
        }
    }
}
