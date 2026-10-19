package com.example.personalfinance.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.personalfinance.domain.model.Expense
import com.example.personalfinance.ui.theme.DarkBackground
import com.example.personalfinance.ui.theme.TextPrimary
import com.example.personalfinance.ui.utils.formatExpenseDateTime
import com.example.personalfinance.ui.utils.formatExpenseHeaderDate
import com.example.personalfinance.ui.utils.formatMoney
import kotlinx.datetime.Instant

@Composable
fun ExpenseCard(
    expense: Expense,
    showDivider: Boolean = true,
    onEditExpenseClick: () -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = DarkBackground
        ),
        onClick = {
            onEditExpenseClick()
        }
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
//                    Row(
//                        modifier = Modifier.fillMaxWidth(),
//                        verticalAlignment = Alignment.CenterVertically
//                    ) {
//                        Text(
//                            text = formatExpenseHeaderDate(expense.dateTime),
//                            color = TextPrimary.copy(alpha = 0.7f),
//                            fontSize = 14.sp
//                        )
//                    }



                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        CategoryIcon(
                            categoryName = expense.category,
                            modifier = Modifier
                        )
                        Spacer(modifier = Modifier.width(8.dp))

                        Column(
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = expense.title,
                                color = TextPrimary,
                                fontSize = 18.sp
                            )
                            Spacer(Modifier.height(2.dp))
                            Text(
                                text = "${expense.category} • ${formatExpenseDateTime(expense.dateTime)}",
                                color = TextPrimary.copy(alpha = 0.7f),
                                fontSize = 14.sp
                            )
                        }
                        Row(
                            modifier = Modifier.padding(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "${formatMoney(expense.amount)} ₸",
                                color = TextPrimary,
                                fontSize = 18.sp
                            )
                        }
                    }
                }
            }

        if (showDivider) {
            HorizontalDivider(
                thickness = 1.2.dp,
                color = Color.White.copy(alpha = 0.12f)
            )
        }
    }
