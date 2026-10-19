package com.example.personalfinance.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.personalfinance.domain.model.Expense
import com.example.personalfinance.domain.model.categories
import com.example.personalfinance.ui.components.ExpenseCard
import com.example.personalfinance.ui.components.TotalExpenseCard
import com.example.personalfinance.ui.theme.DarkBackground
import com.example.personalfinance.ui.theme.TextPrimary
import com.example.personalfinance.ui.utils.formatExpenseHeaderDate
import com.example.personalfinance.viewModel.ExpenseViewModel
import kotlinx.datetime.Instant

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: ExpenseViewModel,
    onAddExpenseClick: () -> Unit,
    onEditExpenseClick: (Expense) -> Unit,
) {
    val expenses by viewModel.expenses.collectAsState()
    val groupedExpenses by viewModel.groupedExpenses.collectAsState()
    val isSelectedCategory by viewModel.selectedCategory.collectAsState()
    val filteredExpenses = remember(expenses, isSelectedCategory) {
        isSelectedCategory?.let { category ->
            expenses.filter { it.category == category.name }
        } ?: expenses
    }
    val filteredTotalAmount = remember(filteredExpenses) {
        filteredExpenses.sumOf { it.amount }
    }
    var isExpanded by remember { mutableStateOf(false) }

    val filteredGroups = remember(groupedExpenses, isSelectedCategory) {
        val selectedCategory = isSelectedCategory
        groupedExpenses.mapNotNull { (date, dayExpenses) ->
            val filteredExpenses = dayExpenses.filter { expense ->
                selectedCategory == null ||
                        expense.category == selectedCategory.name
            }
            if (filteredExpenses.isEmpty()) {
                null
            } else {
                date to filteredExpenses
            }
        }
    }
    val displayedGroups = remember(filteredGroups, isExpanded) {
        if (isExpanded) {
            filteredGroups
        } else {
            val result = mutableListOf<Pair<Instant, List<Expense>>>()
            var count = 0

            for (group in filteredGroups) {
                result.add(group)
                count += group.second.size

                if (count >= 5) break
            }

            result
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Expenses",
                        modifier = Modifier.weight(1f),
                        color = TextPrimary,
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
            item {
                TotalExpenseCard(filteredTotalAmount, filteredExpenses.size)
                Spacer(modifier = Modifier.height(16.dp))
            }

            item {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(horizontal = 8.dp)
                ) {
                    item {
                        FilterChip(
                            selected = isSelectedCategory == null,
                            onClick = { viewModel.setSelectedCategory(null) },
                            label = { Text("All") },
                            shape = CircleShape,
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = Color.White,
                                selectedLabelColor = Color.Black,
                                containerColor = DarkBackground,
                                labelColor = TextPrimary.copy(alpha = 0.7f)
                            ),
                            border = FilterChipDefaults.filterChipBorder(
                                enabled = true,
                                selected = isSelectedCategory == null,
                                borderColor = Color.Gray,
                                selectedBorderColor = Color.Transparent
                            )
                        )
                    }

                    items(categories) { category ->
                        val isSelected = category == isSelectedCategory
                        FilterChip(
                            selected = isSelected,
                            onClick = { viewModel.setSelectedCategory(category) },
                            label = { Text(text = "$category") },
                            shape = CircleShape,
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = Color.White,
                                selectedLabelColor = Color.Black,
                                containerColor = DarkBackground,
                                labelColor = TextPrimary.copy(alpha = 0.7f)
                            ),
                            border = FilterChipDefaults.filterChipBorder(
                                enabled = true,
                                selected = isSelected,
                                borderColor = Color.Gray,
                                selectedBorderColor = Color.Transparent
                            )
                        )

                    }
                }
            }


            displayedGroups.forEach { (date, expenses) ->

                item(key = "header_${date}") {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = formatExpenseHeaderDate(date),
                            color = TextPrimary.copy(alpha = 0.7f),
                            fontSize = 14.sp
                        )
                    }
                }
                itemsIndexed(
                    items = expenses,
                    key = { _, expense -> expense.id.toString() }
                ) { index, expense ->
                    ExpenseCard(
                        expense = expense,
                        onEditExpenseClick =  { onEditExpenseClick(expense) } ,
                        showDivider = index < expenses.lastIndex
                    )

                }


            }
            if (!isExpanded && displayedGroups.size < filteredGroups.size) {
                item {
                    Spacer(modifier = Modifier.height(8.dp))
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        androidx.compose.material3.TextButton(
                            onClick = {
                                isExpanded = true
                            }
                        ) {
                            Text(
                                text = "More",
                                color = TextPrimary,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }

//            itemsIndexed(
//                items = filteredExpenses,
//                key = { _, expense -> expense.id }
//            ) { index, expense ->
//                if (!isExpanded || index < 5) {
//                    val onEdit = remember(expense) { { onEditExpenseClick(expense) } }
//                    ExpenseCard(
//                        expense = expense,
//                        onEditExpenseClick = onEdit,
//                        showDivider = index < filteredExpenses.lastIndex
//                    )
//                }
//                if(!isExpanded && index == 4 && filteredExpenses.size > 5) {
//                    Spacer(modifier = Modifier.height(8.dp))
//                    Box(
//                        modifier = Modifier.fillMaxWidth(),
//                        contentAlignment = Alignment.Center
//                    ) {
//                        androidx.compose.material3.TextButton(
//                            onClick = {
//                                isExpanded = true
//                            }
//                        ) {
//                            Text(
//                                text = "More",
//                                color = TextPrimary,
//                                fontSize = 16.sp,
//                                fontWeight = FontWeight.Medium
//                            )
//                        }
//                    }
//                    Spacer(modifier = Modifier.height(8.dp))
//
//                }
//            }
            item {
                Spacer(Modifier.padding(24.dp))
            }
        }
        FloatingActionButton (
            onClick = onAddExpenseClick,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            contentColor = Color.White,
            containerColor = Color(0xFF1D9E75)
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add expense",
                tint = Color.White,
                modifier = Modifier.size(32.dp)
            )
        }


    }
}




