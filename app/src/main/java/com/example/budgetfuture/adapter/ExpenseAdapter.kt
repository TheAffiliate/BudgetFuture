package com.example.budgetfuture.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.budgetfuture.R
import com.example.budgetfuture.database.ExpenseEntity
import java.text.SimpleDateFormat
import java.util.*

class ExpenseAdapter(private val expenses: MutableList<ExpenseEntity>) :
    RecyclerView.Adapter<ExpenseAdapter.ExpenseViewHolder>() {

    inner class ExpenseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvCategoryName: TextView = itemView.findViewById(R.id.tvCategoryName)
        val tvAmount: TextView = itemView.findViewById(R.id.tvAmount)
        val tvDate: TextView = itemView.findViewById(R.id.tvDate)
        val tvDescription: TextView = itemView.findViewById(R.id.tvDescription)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpenseViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_expense, parent, false)
        return ExpenseViewHolder(view)
    }

    override fun onBindViewHolder(holder: ExpenseViewHolder, position: Int) {
        val expense = expenses[position]
        holder.tvCategoryName.text = expense.CategoryName
        holder.tvAmount.text = "R ${expense.amount}"
        holder.tvDescription.text = expense.Description

        val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        holder.tvDate.text = format.format(expense.DateOfExpense)
    }

    override fun getItemCount(): Int = expenses.size

    fun getExpenseAt(position: Int): ExpenseEntity {
        return expenses[position]
    }

    fun removeExpenseAt(position: Int) {
        expenses.removeAt(position)
        notifyItemRemoved(position)
    }
}
