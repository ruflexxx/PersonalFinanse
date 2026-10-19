package com.example.personalfinance.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.personalfinance.repository.ExpenseRepository

class ExpenseViewModelFactory(
    private val repository: ExpenseRepository,
) : ViewModelProvider.Factory {

    override fun <T: ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        if (modelClass.isAssignableFrom(ExpenseViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ExpenseViewModel(
                repository = repository,
                savedStateHandle = extras.createSavedStateHandle()
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}