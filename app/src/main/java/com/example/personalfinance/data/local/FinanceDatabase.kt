package com.example.personalfinance.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [ExpenseEntity::class],
    version = 2
)
@TypeConverters(InstantConverter::class)
abstract class FinanceDatabase : RoomDatabase() {
    abstract fun expenseDao(): ExpenseDao
}