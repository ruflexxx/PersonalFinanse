package com.example.personalfinance

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.personalfinance.data.local.DatabaseProvider
import com.example.personalfinance.repository.ExpenseRepository
import com.example.personalfinance.ui.theme.PersonalFinanceTheme
import com.example.personalfinance.ui.navigation.AppNavigation
import com.example.personalfinance.viewModel.ExpenseViewModel
import com.example.personalfinance.viewModel.ExpenseViewModelFactory
import androidx.lifecycle.viewmodel.compose.viewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val database = DatabaseProvider.getDatabase(this)
        val repository = ExpenseRepository(database.expenseDao())
        setContent {
            PersonalFinanceTheme {
                val viewModel: ExpenseViewModel = viewModel(
                    factory = ExpenseViewModelFactory(repository)
                )

                AppNavigation(viewModel)
            }
        }
    }
}

