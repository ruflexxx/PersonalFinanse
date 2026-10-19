package com.example.personalfinance.ui.utils

import androidx.compose.ui.text.input.TextFieldValue
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.toLocalDateTime
import java.text.NumberFormat
import java.util.Locale

fun formatMoney(amount: Double): String {
    val formatter = NumberFormat.getNumberInstance(Locale("ru", "KZ"))
    return formatter.format(amount)
}
fun formatAmountInput(value: TextFieldValue): String {
    val digits = value.text.filter { it.isDigit() }

    return digits
        .reversed()
        .chunked(3)
        .joinToString(" ")
        .reversed()
}
fun formatExpenseDateTime(epochMillis: Instant): String {
    val dateTime = epochMillis.toLocalDateTime(
        TimeZone.of("Asia/Almaty")
    )
    return String.format(
        Locale("ru", "KZ"),
        "%02d:%02d",
        dateTime.hour,
        dateTime.minute
    )
}
fun formatExpenseHeaderDate(epochMillis: Instant): String {

    val timeZone = TimeZone.of("Asia/Almaty")
    val expenseDate = epochMillis.toLocalDateTime(timeZone)
    val today = Clock.System.now().toLocalDateTime(timeZone).date
    val yesterday = today.minus(1, DateTimeUnit.DAY)
    val week = yesterday.minus(7, DateTimeUnit.DAY)

    val months = listOf(
        "Jan", "Feb", "Mar", "Apr",
        "May", "Jun", "Jul", "Aug",
        "Sep", "Oct", "Nov", "Dec"
    )
    val day = expenseDate.date.dayOfMonth
    val month = months[expenseDate.monthNumber -1]
    val year = today.minus(1, DateTimeUnit.YEAR)
    val formattedDate = "$day $month"
    val formattedWeek = expenseDate.date.dayOfWeek.toString().lowercase().replaceFirstChar { it.uppercase() }

    return when {
        expenseDate.date == today -> "Today • $formattedDate"
        expenseDate.date == yesterday -> "Yesterday • $formattedDate"
        expenseDate.date in week..<yesterday -> "$formattedWeek • $formattedDate"
        expenseDate.date < year -> "$formattedDate ${year.year}"
        else -> formattedDate
    }
}