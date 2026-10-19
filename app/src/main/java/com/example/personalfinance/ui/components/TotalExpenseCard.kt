package com.example.personalfinance.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.personalfinance.ui.theme.CardGreen
import com.example.personalfinance.ui.theme.TextPrimary
import com.example.personalfinance.ui.utils.formatMoney

@Composable
fun TotalExpenseCard(
        totalAmount: Double,
        size: Int
    ) {

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = CardGreen)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Text(
                text = "Total Spent",
                color = TextPrimary.copy(alpha = 0.7f),
                fontSize = 16.sp
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = "${formatMoney(totalAmount)} ₸",
                color = TextPrimary,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold

            )
            Spacer(Modifier.height(4.dp))

            Text(
                text = "$size entries",
                color = TextPrimary.copy(alpha = 0.7f),
                fontSize = 14.sp
            )
        }
    }
}