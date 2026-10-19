package com.example.personalfinance.viewModel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.personalfinance.domain.model.Categories
import com.example.personalfinance.domain.model.Expense
import com.example.personalfinance.repository.ExpenseRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.toLocalDateTime
import kotlinx.datetime.TimeZone


class ExpenseViewModel(
    private val repository: ExpenseRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _expenses = MutableStateFlow<List<Expense>>(emptyList())
    val expenses: StateFlow<List<Expense>> = _expenses
    private val editingExpenseId =
        savedStateHandle.getStateFlow<Long?>(
            key = "editing_expense_id",
            initialValue = null
        )
    val expenseToEdit = combine(
        expenses,
            editingExpenseId
    ) { expenseList , id ->
        expenseList.firstOrNull { it.id == id }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = null
    )
    private val _selectedCategory = MutableStateFlow<Categories?>(null)
    val selectedCategory: StateFlow<Categories?> = _selectedCategory.asStateFlow()
    val groupedExpenses: StateFlow<Map<Instant, List<Expense>>> = expenses
        .map { expenseList ->
            expenseList
                .groupBy { expense ->
                    val localDate = expense.dateTime.toLocalDateTime(TimeZone.of("Asia/Almaty"))
                    localDate.date
                        .atStartOfDayIn(TimeZone.of("Asia/Almaty"))
                }
                .toSortedMap(compareByDescending { it })
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyMap()
        )

    fun setExpenseToEdit(expense: Expense?) {
        savedStateHandle["editing_expense_id"] = expense?.id
    }

    init {
        loadExpenses()
    }

    private fun loadExpenses() {
        viewModelScope.launch {
            repository.getAllExpenses().collect { expenses ->
                _expenses.value = expenses
            }
        }
    }
    fun saveTime(): Instant {
        return Clock.System.now()
    }

    fun addExpense(
        title: String,
        amount: Double,
        category: String,
    ) {
        viewModelScope.launch {
            repository.addExpense(
                Expense(
                    id = 0,
                    title = title,
                    amount = amount,
                    category = category,
                    dateTime = saveTime()
                )
            )
        }
    }

    fun deleteExpense(expense: Expense) {
        viewModelScope.launch {
            repository.deleteExpense(expense)
        }
    }
    fun updateExpense(
        id: Long,
        title: String,
        amount: Double,
        category: String,
        dateTime: Instant
    ) {
        viewModelScope.launch {
            repository.updateExpense(
                    Expense(
                        id = id,
                        title = title,
                        amount = amount,
                        category = category,
                        dateTime = dateTime
                )
            )
        }
    }
    fun setSelectedCategory(category: Categories?) {
        _selectedCategory.value = category
    }

}