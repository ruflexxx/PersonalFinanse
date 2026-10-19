package com.example.personalfinance.ui.navigation

import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.personalfinance.ui.screens.AddExpenseScreen
import com.example.personalfinance.ui.theme.DarkBackground
import com.example.personalfinance.viewModel.ExpenseViewModel

@Composable
fun AppNavigation(
    viewModel: ExpenseViewModel
) {
    val navController = rememberNavController()
    val expenseToEdit by viewModel.expenseToEdit.collectAsState()
    NavHost(
        navController = navController,
        startDestination = "main_tabs",
        modifier = Modifier.background(DarkBackground)
    ) {
        composable("main_tabs") {
            MainTabsScreen(
                viewModel = viewModel,
                onAddExpenseClick = {
                    viewModel.setExpenseToEdit(null)
                    navController.navigate("add_expense") {
                    }
                },
                onEditExpenseClick = { expense ->
                    viewModel.setExpenseToEdit(expense)
                    navController.navigate("add_expense") {
                    }
                }
            )
        }
        composable("add_expense") {
            AddExpenseScreen(
                viewModel = viewModel,
                expense = expenseToEdit,
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
    }
}

