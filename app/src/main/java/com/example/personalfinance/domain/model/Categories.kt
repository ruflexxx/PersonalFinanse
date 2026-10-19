package com.example.personalfinance.domain.model

import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.Color
import com.example.personalfinance.R

enum class Categories(
    val displayName: String,
    @DrawableRes val iconRes: Int,
    val iconColor: Color
) {
    Food(
        displayName = "Food",
        iconRes = R.drawable.food,
        iconColor = Color(0xFF1D9E75)
    ),
    Transport(
        displayName = "Transport",
        iconRes = R.drawable.transport,
        iconColor = Color(0xFF378ADD)
    ),
    Shopping(
        displayName = "Shopping",
        iconRes = R.drawable.shopping,
        iconColor = Color(0xFFD4537E)
    ),
    Entertainment(
        displayName = "Entertainment",
        iconRes = R.drawable.entertainment,
        iconColor = Color(0xFF7F77DD)
    ),
    Health(
        displayName = "Health",
        iconRes = R.drawable.health,
        iconColor = Color(0xFFD85A30)
    ),
    Other(
        displayName = "Other",
        iconRes = R.drawable.other,
        iconColor = Color(0xFF888780)
    );

    companion object {

        fun fromString(categoryName: String): Categories {
            return entries.find {
                it.name.equals(categoryName, ignoreCase = true) ||
                it.displayName.equals(categoryName, ignoreCase = true)
            } ?: Other
        }

    }
}
val categories = Categories.entries.toList()
