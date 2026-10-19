package com.example.personalfinance.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.personalfinance.domain.model.Categories

@Composable
fun CategoryIcon(
    categoryName: String,
    modifier: Modifier,
    hasBackground: Boolean = true,
    containerSize: Dp = 48.dp,
    iconSize: Dp = 24.dp
) {
    val categoryEnum = Categories.fromString(categoryName)
    val iconColor = categoryEnum.iconColor
    val containerColor = iconColor.copy(alpha = 0.15f)

    if (hasBackground) {
        Box(
            modifier = modifier
                .size(containerSize)
                .clip(RoundedCornerShape(16.dp))
                .background(containerColor),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = categoryEnum.iconRes),
                contentDescription = categoryName,
                tint = iconColor,
                modifier = Modifier.size(iconSize)
            )
        }
    } else {
        Icon(
            painter = painterResource(id = categoryEnum.iconRes),
            contentDescription = categoryName,
            tint = iconColor,
            modifier = Modifier.size(iconSize)
        )
    }
}
