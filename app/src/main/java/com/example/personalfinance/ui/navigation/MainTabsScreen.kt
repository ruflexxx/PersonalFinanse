package com.example.personalfinance.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.personalfinance.domain.model.Expense
import com.example.personalfinance.ui.components.BottomBar
import com.example.personalfinance.ui.screens.HomeScreen
import com.example.personalfinance.ui.screens.StatisticScreen
import com.example.personalfinance.ui.theme.DarkBackground
import com.example.personalfinance.viewModel.ExpenseViewModel

@Composable
fun MainTabsScreen(
    viewModel: ExpenseViewModel,
    onAddExpenseClick: () -> Unit,
    onEditExpenseClick: (Expense) -> Unit
) {
    val tabNavController = rememberNavController()
    Scaffold(
        containerColor = DarkBackground,
        bottomBar = {
            BottomBar(tabNavController)
        },
    ) { paddingValues ->
        NavHost(
            navController = tabNavController,
            startDestination = "home",
            modifier = Modifier
                .padding(paddingValues)
        ) {
            composable("home") {
                HomeScreen(
                    viewModel = viewModel,
                    onAddExpenseClick = onAddExpenseClick,
                    onEditExpenseClick = onEditExpenseClick
                )
            }
            composable("statistic") {
                StatisticScreen(
                    viewModel = viewModel,
                    onSelectedCategory = { category ->
                        viewModel.setSelectedCategory(category)
                        tabNavController.popBackStack()
                    }
                )
            }
        }
    }
}