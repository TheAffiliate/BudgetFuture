package com.example.budgetfuture.database

import androidx.room.*
import com.example.budgetfuture.utils.Constants.EXPENSE_TABLE
import java.util.*

@Dao
interface ExpenseDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertExpense(expenseEntity: ExpenseEntity)

    @Update
    fun updateExpense(expenseEntity: ExpenseEntity)

    @Delete
    fun deleteExpense(expenseEntity: ExpenseEntity)

    @Query("SELECT * FROM $EXPENSE_TABLE ORDER BY ExpenseId DESC")
    fun getAllExpenses(): MutableList<ExpenseEntity>

    @Query("SELECT * FROM $EXPENSE_TABLE WHERE ExpenseId LIKE :id")
    fun getExpense(id: Int): ExpenseEntity

    @Query("SELECT * FROM $EXPENSE_TABLE WHERE DateOfExpense BETWEEN :startDate AND :endDate ORDER BY DateOfExpense DESC")
    fun getExpensesBetween(startDate: Date, endDate: Date): List<ExpenseEntity>
}
