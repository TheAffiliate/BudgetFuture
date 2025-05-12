package com.example.budgetfuture.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.budgetfuture.utils.Constants.EXPENSE_TABLE
import java.util.Date

@Entity(tableName = EXPENSE_TABLE)
data class ExpenseEntity @JvmOverloads constructor(
    @PrimaryKey(autoGenerate = true)
    val ExpenseId : Int,
    val CategoryName : String,
    val DateOfExpense : Date,
    val amount :Double,
    val Description : String,
// Storing path for  the image/picture/reciept
    val imagePath: String? = null


)
