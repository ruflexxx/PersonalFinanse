package com.example.personalfinance.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ExpenseDao {

    @Query("SELECT * FROM expenses ORDER BY dateTime DESC")
    fun getAllExpenses(): Flow<List<ExpenseEntity>>

    @Insert
    suspend fun insertExpenses(expense: ExpenseEntity)

    @Delete
    suspend fun deleteExpenses(expense: ExpenseEntity)

    @Update
    suspend fun updateExpenses(expense: ExpenseEntity)

}