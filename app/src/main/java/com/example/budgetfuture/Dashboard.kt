package com.example.budgetfuture

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import com.example.budgetfuture.database.ExpenseDatabase
import com.example.budgetfuture.utils.Constants.EXPENSE_DATABASE
import com.example.budgetfuture.gamification.GamificationEngine
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.ColorTemplate
import java.text.SimpleDateFormat
import java.util.*

class Dashboard : AppCompatActivity() {

    private lateinit var expenseDB: ExpenseDatabase
    private lateinit var expensesContainer: LinearLayout
    private lateinit var balanceTextView: TextView
    private lateinit var userTextView: TextView
    private lateinit var buttonAddExpenses: Button
    private lateinit var buttonViewExpenses: Button
    private lateinit var buttonAddSalary: Button
    private lateinit var levelText: TextView
    private lateinit var xpProgress: ProgressBar
    private lateinit var badge: ImageView
    private lateinit var pieChart: PieChart

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var gamification: GamificationEngine

    private var currentBalance: Double = 0.0
    private var totalExpenses = 0f
    private val maxBudget = 1000f // Adjustable as needed

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE)

        userTextView = findViewById(R.id.textView5)
        balanceTextView = findViewById(R.id.balanceTextView)
        expensesContainer = findViewById(R.id.expensesContainer)
        buttonAddExpenses = findViewById(R.id.expensebtn)
        buttonViewExpenses = findViewById(R.id.viewExpensebtn)
        buttonAddSalary = findViewById(R.id.addSalaryBtn)
        levelText = findViewById(R.id.levelText)
        xpProgress = findViewById(R.id.xpProgress)
        badge = findViewById(R.id.congratsBadge)
        pieChart = findViewById(R.id.pieChart)

        gamification = GamificationEngine(this, levelText, xpProgress, badge)
        gamification.setMaxBudget(maxBudget)

        expenseDB = Room.databaseBuilder(this, ExpenseDatabase::class.java, EXPENSE_DATABASE)
            .allowMainThreadQueries()
            .fallbackToDestructiveMigration()
            .build()

        val userName = sharedPreferences.getString("name", "User")
        userTextView.text = "Hi, $userName"

        currentBalance = sharedPreferences.getFloat("currentBalance", 0.0f).toDouble()
        updateBalanceDisplay()

        buttonAddExpenses.setOnClickListener {
            startActivity(Intent(this, Categories::class.java))
        }

        buttonViewExpenses.setOnClickListener {
            startActivity(Intent(this, ExpensesOverview::class.java))
        }

        buttonAddSalary.setOnClickListener {
            showSalaryDialog()
        }

        loadExpenses()
    }

    override fun onResume() {
        super.onResume()
        loadExpenses()
        currentBalance = sharedPreferences.getFloat("currentBalance", 0.0f).toDouble()
        updateBalanceDisplay()
    }

    @SuppressLint("SetTextI18n")
    private fun loadExpenses() {
        val expensesList = expenseDB.dao().getAllExpenses()
        expensesContainer.removeAllViews()

        val inflater = layoutInflater
        val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        var total = 0f
        val pieEntries = mutableListOf<PieEntry>()

        for (expense in expensesList) {
            val view = inflater.inflate(R.layout.item_expense, expensesContainer, false)
            view.findViewById<TextView>(R.id.tvCategoryName).text = expense.CategoryName
            view.findViewById<TextView>(R.id.tvAmount).text = "R ${expense.amount}"
            view.findViewById<TextView>(R.id.tvDate).text = format.format(expense.DateOfExpense)
            view.findViewById<TextView>(R.id.tvDescription).text = expense.Description
            expensesContainer.addView(view)

            total += expense.amount.toFloat()
            pieEntries.add(PieEntry(expense.amount.toFloat(), expense.CategoryName))
            gamification.registerExpense(expense.amount.toFloat(), total)
        }

        totalExpenses = total
        val savedBalance = sharedPreferences.getFloat("currentBalance", 0.0f).toDouble()
        currentBalance = savedBalance - total
        updateBalanceDisplay()

        updateChart(pieEntries)
    }

    private fun updateChart(entries: List<PieEntry>) {
        val dataSet = PieDataSet(entries, "Expenses by Category")
        dataSet.colors = ColorTemplate.MATERIAL_COLORS.toList()
        val pieData = PieData(dataSet)
        pieChart.data = pieData
        pieChart.invalidate()
    }

    private fun showSalaryDialog() {
        val dialogView = layoutInflater.inflate(R.layout.activity_dialog_add_salary, null)
        val editTextSalary = dialogView.findViewById<EditText>(R.id.editTextSalary)

        AlertDialog.Builder(this)
            .setTitle("Add Salary")
            .setView(dialogView)
            .setPositiveButton("Add") { _, _ ->
                val input = editTextSalary.text.toString().trim()
                val salaryAmount = input.toDoubleOrNull()
                if (salaryAmount != null && salaryAmount > 0) {
                    val totalExpenses = expenseDB.dao().getAllExpenses().sumOf { it.amount }
                    currentBalance = salaryAmount - totalExpenses
                    with(sharedPreferences.edit()) {
                        putFloat("currentBalance", salaryAmount.toFloat())
                        apply()
                    }
                    updateBalanceDisplay()
                } else {
                    Toast.makeText(this, "Please enter a valid amount", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun updateBalanceDisplay() {
        balanceTextView.text = String.format(Locale.getDefault(), "R %,.2f", currentBalance)
    }
}
