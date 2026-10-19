package com.example.personalfinance.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.personalfinance.domain.model.Categories
import com.example.personalfinance.domain.model.Expense
import com.example.personalfinance.domain.model.categories
import com.example.personalfinance.ui.components.CategoryIcon
import com.example.personalfinance.ui.theme.DarkBackground
import com.example.personalfinance.ui.theme.TextPrimary
import com.example.personalfinance.ui.utils.formatMoney
import com.example.personalfinance.viewModel.ExpenseViewModel

@Composable
fun StatisticScreen(
    viewModel: ExpenseViewModel,
    onSelectedCategory: (Categories?) -> Unit
) {
    val expenses by viewModel.expenses.collectAsState()
    val categoryExpensesMap = categories.associateWith { category ->
        expenses
            .filter { Categories.fromString(it.category) == category }
            .sumOf { it.amount }
            .toFloat()
    }
    val maxAmount = expenses.sumOf { it.amount }.toFloat()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
            .verticalScroll(rememberScrollState())
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 12.dp,
                    vertical = 8.dp
                )
        ) {
            Row {
                Text(
                    text = "Statistic",
                    modifier = Modifier.weight(1f),
                    color = TextPrimary,
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            ExpenseDonutChartCard(categoryExpenses = categoryExpensesMap)

            Spacer(modifier = Modifier.height(16.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF151515)
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Expenses by category",
                        color = TextPrimary,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(Modifier.height(12.dp))

                    categories.forEach { entries ->
                        CategoryRowItem(
                            category = entries,
                            expenses = expenses,
                            maxCategoryAmount = maxAmount,
                            onSelectedCategory = { onSelectedCategory(entries) }
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun CategoryRowItem(
    category: Categories,
    expenses: List<Expense>,
    maxCategoryAmount: Float,
    onSelectedCategory: (Categories?) -> Unit
) {
    val amount = expenses
        .filter { Categories.fromString(it.category) == category }
        .sumOf { it.amount }
    val filteredAmount = formatMoney(amount)
    val filteredAmountForBar = expenses
        .filter { Categories.fromString(it.category) == category }
        .sumOf { it.amount }
        .toFloat()
    val progress = if (maxCategoryAmount > 0f) {
        (filteredAmountForBar / maxCategoryAmount).coerceIn(0f, 1f)
    } else {
        0f
    }
    var selectedCategory: Categories?
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                selectedCategory = category
                onSelectedCategory(selectedCategory)
            }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                CategoryIcon(
                    categoryName = category.name,
                    modifier = Modifier,
                    hasBackground = false,
                    iconSize = 20.dp
                )
                Spacer(Modifier.width(4.dp))
                Text(
                    text = category.displayName,
                    color = TextPrimary,
                    fontSize = 16.sp,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = "$filteredAmount ₸",
                    color = TextPrimary,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(Modifier.height(8.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(4.dp)
                    .background(
                        color = Color(0xFF2A2D2C),
                        shape = RoundedCornerShape(2.dp)
                    )
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(progress)
                        .height(4.dp)
                        .background(
                            color = category.iconColor,
                            shape = RoundedCornerShape(2.dp)
                        )
                )
            }
        }
    }
}

@Composable
fun ExpenseDonutChartCard(
    categoryExpenses: Map<Categories, Float>
) {
    val totalAmount = categoryExpenses.values.sum()

    val topCategoryEntry = categoryExpenses.maxByOrNull { it.value }
    val topCategory = topCategoryEntry?.key ?: Categories.Other
    val topCategoryAmount = topCategoryEntry?.value ?: 0f

    val topCategoryPercentage = if (totalAmount > 0f) {
        ((topCategoryAmount / totalAmount) * 100f).toInt()
    } else {
        0
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = Color(0xFF151515),
                shape = RoundedCornerShape(24.dp)
            )
            .padding(20.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier.size(150.dp),
                contentAlignment = Alignment.Center
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val strokeWidth = 26.dp.toPx()
                    val arcSize = size.minDimension - strokeWidth
                    val topLeft = Offset(
                        x = (size.width - arcSize) / 2f,
                        y = (size.height - arcSize) / 2f
                    )

                    var startAngle = -90f

                    if (totalAmount > 0f) {
                        Categories.entries.forEach { category ->
                            val amount = categoryExpenses[category] ?: 0f
                            if (amount > 0f) {
                                val sweepAngle = (amount / totalAmount) * 360f

                                drawArc(
                                    color = category.iconColor,
                                    startAngle = startAngle,
                                    sweepAngle = sweepAngle,
                                    useCenter = false,
                                    topLeft = topLeft,
                                    size = Size(arcSize, arcSize),
                                    style = Stroke(width = strokeWidth, cap = StrokeCap.Butt)
                                )
                                startAngle += sweepAngle
                            }
                        }
                    } else {
                        drawArc(
                            color = Color(0xFF2A2D2C),
                            startAngle = 0f,
                            sweepAngle = 360f,
                            useCenter = false,
                            topLeft = topLeft,
                            size = Size(arcSize, arcSize),
                            style = Stroke(width = strokeWidth, cap = StrokeCap.Butt)
                        )
                    }
                }

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Total",
                        color = Color.Gray,
                        fontSize = 12.sp
                    )
                    Text(
                        text = if (totalAmount >= 1_000_000f) {
                            String.format("%.2f m ₸", totalAmount / 1_000_000f)
                        } else {
                            "${totalAmount.toInt()} ₸"
                        },
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.width(24.dp))

            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = "Most",
                    color = Color.Gray,
                    fontSize = 14.sp
                )
                Text(
                    text = topCategory.displayName,
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "$topCategoryPercentage% expenses",
                    color = topCategory.iconColor,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}