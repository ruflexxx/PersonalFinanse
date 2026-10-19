package com.example.personalfinance.domain.model

import androidx.compose.runtime.Immutable
import kotlinx.datetime.Instant

@Immutable
data class Expense(
    val id: Long,
    val title: String,
    val amount: Double,
    val category: String,
    val dateTime: Instant
)