package com.example.personalfinance.repository

import com.example.personalfinance.data.local.ExpenseDao
import com.example.personalfinance.data.local.ExpenseEntity
import com.example.personalfinance.domain.model.Expense
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ExpenseRepository(
    private val expenseDao: ExpenseDao
) {
    fun getAllExpenses(): Flow<List<Expense>> {
        return expenseDao.getAllExpenses()
            .map { entities ->
                entities.map { entity ->
                    Expense(
                        id = entity.id,
                        title = entity.title,
                        amount = entity.amount,
                        category = entity.category,
                        dateTime = entity.dateTime
                    )
                }
            }
    }
    suspend fun addExpense(expense: Expense) {
        expenseDao.insertExpenses(
            ExpenseEntity(
                id = expense.id,
                title = expense.title,
                amount = expense.amount,
                category = expense.category,
                dateTime = expense.dateTime
            )
        )
    }
    suspend fun deleteExpense(expense: Expense) {
        expenseDao.deleteExpenses(
            ExpenseEntity(
                id = expense.id,
                title = expense.title,
                amount = expense.amount,
                category = expense.category,
                dateTime = expense.dateTime
            )
        )
    }
    suspend fun updateExpense(expense: Expense) {
        expenseDao.updateExpenses(
            ExpenseEntity(
                id = expense.id,
                title = expense.title,
                amount = expense.amount,
                category = expense.category,
                dateTime = expense.dateTime
            )
        )
    }
}