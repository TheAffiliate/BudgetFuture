package com.example.budgetfuture

import android.app.DatePickerDialog
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import androidx.cardview.widget.CardView
import com.example.budgetfuture.database.ExpenseDatabase
import com.example.budgetfuture.database.ExpenseEntity
import com.example.budgetfuture.utils.Constants.EXPENSE_DATABASE
import com.google.android.material.tabs.TabLayout
import androidx.room.Room
import java.util.*

class ExpensesOverview : AppCompatActivity() {

    private lateinit var expensesContainer: LinearLayout
    private lateinit var tabLayout: TabLayout

    private val expenseDB: ExpenseDatabase by lazy {
        Room.databaseBuilder(this, ExpenseDatabase::class.java, EXPENSE_DATABASE)
            .allowMainThreadQueries()
            .fallbackToDestructiveMigration()
            .build()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_expenses_overview)

        expensesContainer = findViewById(R.id.expensesContainer)
        tabLayout = findViewById(R.id.tabLayout)

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                when (tab.position) {
                    0 -> loadExpensesForWeek()
                    1 -> loadExpensesForMonth()
                    2 -> showCustomDateRangePicker()
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })

        loadExpensesForWeek()
    }

    private fun displayExpenses(expenses: List<ExpenseEntity>) {
        expensesContainer.removeAllViews()

        if (expenses.isEmpty()) {
            val noDataText = TextView(this).apply {
                text = "No expenses recorded."
                textSize = 16f
                setTextColor(0xFFE3BA42.toInt())
            }
            expensesContainer.addView(noDataText)
            return
        }

        expenses.forEach { expense ->
            val cardView = CardView(this).apply {
                layoutParams = LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                ).apply { setMargins(0, 0, 0, 24) }
                radius = 12f
                cardElevation = 6f
                setCardBackgroundColor(0xFF1A1F3D.toInt())
            }

            val contentLayout = LinearLayout(this).apply {
                orientation = LinearLayout.VERTICAL
                setPadding(32, 32, 32, 32)
            }

            val imageView = AppCompatImageView(this).apply {
                layoutParams = LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    300
                ).apply { bottomMargin = 16 }
                scaleType = android.widget.ImageView.ScaleType.CENTER_CROP
                expense.imagePath?.let {
                    val bitmap = BitmapFactory.decodeFile(it)
                    if (bitmap != null) setImageBitmap(bitmap)
                }
            }

            val summaryText = TextView(this).apply {
                text = "${expense.DateOfExpense}    R ${expense.amount}    ${expense.CategoryName}"
                textSize = 14f
                setTextColor(0xEEE3BA42.toInt())
            }

            val descriptionText = TextView(this).apply {
                text = expense.Description
                textSize = 16f
                setTextColor(0xEEE3BA42.toInt())
                setPadding(0, 16, 0, 0)
            }

            contentLayout.addView(imageView)
            contentLayout.addView(summaryText)
            contentLayout.addView(descriptionText)
            cardView.addView(contentLayout)
            expensesContainer.addView(cardView)
        }
    }

    private fun loadExpensesForWeek() {
        val cal = Calendar.getInstance()
        cal.set(Calendar.DAY_OF_WEEK, cal.firstDayOfWeek)
        val start = cal.time
        cal.add(Calendar.DAY_OF_WEEK, 6)
        val end = cal.time

        val expenses = expenseDB.dao().getExpensesBetween(start, end)
        displayExpenses(expenses)
    }

    private fun loadExpensesForMonth() {
        val cal = Calendar.getInstance()
        cal.set(Calendar.DAY_OF_MONTH, 1)
        val start = cal.time
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH))
        val end = cal.time

        val expenses = expenseDB.dao().getExpensesBetween(start, end)
        displayExpenses(expenses)
    }

    private fun showCustomDateRangePicker() {
        val cal = Calendar.getInstance()
        val startPicker = DatePickerDialog(this, { _, year, month, day ->
            val startDate = Calendar.getInstance().apply {
                set(year, month, day, 0, 0, 0)
            }.time

            val endPicker = DatePickerDialog(this, { _, year2, month2, day2 ->
                val endDate = Calendar.getInstance().apply {
                    set(year2, month2, day2, 23, 59, 59)
                }.time

                val expenses = expenseDB.dao().getExpensesBetween(startDate, endDate)
                displayExpenses(expenses)
            }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH))

            endPicker.setTitle("Select End Date")
            endPicker.show()
        }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH))

        startPicker.setTitle("Select Start Date")
        startPicker.show()
    }
}
