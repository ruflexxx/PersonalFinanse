package com.example.personalfinance.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.personalfinance.domain.model.Categories
import com.example.personalfinance.domain.model.Expense
import com.example.personalfinance.domain.model.categories
import com.example.personalfinance.ui.components.CategoryIcon
import com.example.personalfinance.ui.theme.CardGreen
import com.example.personalfinance.ui.theme.DarkBackground
import com.example.personalfinance.ui.theme.TextPrimary
import com.example.personalfinance.ui.utils.formatAmountInput
import com.example.personalfinance.ui.utils.formatMoney
import com.example.personalfinance.viewModel.ExpenseViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddExpenseScreen(
    viewModel: ExpenseViewModel,
    onBackClick: () -> Unit,
    expense: Expense?,
) {
    var title by rememberSaveable { mutableStateOf(expense?.title ?: "") }
    var amount by rememberSaveable(
        stateSaver = TextFieldValue.Saver
    ) {
        mutableStateOf(
            TextFieldValue(
                text = expense?.let { formatMoney(it.amount) } ?: ""
            )
        )
    }
    var category by rememberSaveable { mutableStateOf(expense?.category ?: "") }
    var errorMessage by remember {
        mutableStateOf<String?>(null)
    }
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()
    val categoryEnum = Categories.fromString(category)
    val iconColor = categoryEnum.iconColor.copy(alpha = 0.18f)
    val focusRequester = remember { FocusRequester() }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .imePadding()
            .verticalScroll(rememberScrollState())

    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(1f)
                .background(
                    color = DarkBackground,
                    shape = RoundedCornerShape(28.dp)
                )
                .padding(horizontal = 20.dp)
                .statusBarsPadding()


        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Spacer(Modifier.height(4.dp))
                Column(
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Button(
                            onClick = {
                                onBackClick()
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = DarkBackground,
                                contentColor = TextPrimary
                            ),
                        ) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Back",
                                modifier = Modifier
                                    .size(28.dp)

                            )
                        }
                        Text(
                            text = if (expense == null) {
                                "New Expense"
                            } else {
                                "Edit Expense"
                            },
                            color = TextPrimary,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(Modifier.height(4.dp))
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Amount",
                            color = TextPrimary.copy(alpha = 0.7f),
                            fontSize = 14.sp
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                contentAlignment = Alignment.Center,
                            ) {
                                BasicTextField(
                                    value = amount,
                                    onValueChange = { newValue ->
                                        val formatted = formatAmountInput(newValue)
                                        amount = TextFieldValue(
                                            text = formatted,
                                            selection = TextRange(formatted.length)
                                        )
                                    },
                                    modifier = Modifier.width(
                                        (amount.text.length.coerceAtLeast(1) * 22).dp
                                    ),
                                    textStyle = TextStyle(
                                        color = TextPrimary,
                                        fontSize = 36.sp,
                                        fontWeight = FontWeight.Bold,
                                        textAlign = TextAlign.Center
                                    ),
                                    singleLine = true,
                                    keyboardOptions = KeyboardOptions(
                                        keyboardType = KeyboardType.Number
                                    ),
                                    cursorBrush = SolidColor(Color.White),
                                    interactionSource = interactionSource,
                                    decorationBox = { innerTextField ->
                                        Box(
                                            contentAlignment = Alignment.Center,
                                        ) {
                                            if (amount.text.isBlank() && !isFocused) {
                                                Text(
                                                    text = "0",
                                                    color = TextPrimary,
                                                    fontSize = 36.sp,
                                                    fontWeight = FontWeight.Bold,
                                                )
                                            }
                                            innerTextField()
                                        }
                                    }
                                )
                            }
                            Spacer(Modifier.width(4.dp))
                            Text(
                                text = "₸",
                                color = TextPrimary.copy(alpha = 0.7f),
                                fontSize = 36.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                    Spacer(Modifier.height(16.dp))
                    //title field
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                color = Color(0xFF151515),
                                shape = RoundedCornerShape(24.dp)
                            )
                            .clickable {
                                focusRequester.requestFocus()
                            }
                            .padding(16.dp)
                    ) {
                        Column(modifier = Modifier.fillMaxWidth()) {
                            Text(
                                text = "Name",
                                color = TextPrimary.copy(alpha = 0.7f),
                                fontSize = 12.sp
                            )
                            Spacer(Modifier.height(4.dp))
                            BasicTextField(
                                value = title,
                                onValueChange = { title = it },
                                textStyle = TextStyle(
                                    color = TextPrimary,
                                    fontSize = 18.sp,
                                ),
                                cursorBrush = SolidColor(Color.White),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .focusRequester(focusRequester)

                            )
                        }
                    }
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text(
                            text = "Category",
                            color = TextPrimary.copy(alpha = 0.7f),
                            fontSize = 14.sp
                        )

                        categories.chunked(2).forEach { rowItems ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                rowItems.forEach { entry ->
                                    val isSelectedCategory = category == entry.name

                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                            .height(56.dp)
                                            .background(
                                                color = if (!isSelectedCategory) DarkBackground else iconColor,
                                                shape = RoundedCornerShape(36.dp)
                                            )
                                            .border(
                                                width = if (!isSelectedCategory) 0.5.dp else 1.5.dp,
                                                color = if (!isSelectedCategory) Color(0xFF2C2C2C) else iconColor,
                                                shape = RoundedCornerShape(36.dp)
                                            )
                                            .clickable { category = entry.name }
                                            .padding(horizontal = 14.dp),
                                        contentAlignment = Alignment.CenterStart
                                    ) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                                        ) {
                                            CategoryIcon(
                                                categoryName = entry.name,
                                                modifier = Modifier,
                                                hasBackground = false

                                            )
                                            Text(
                                                text = entry.name,
                                                color = TextPrimary,
                                                fontSize = 14.sp,
                                                fontWeight = FontWeight.Medium
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                    Spacer(Modifier.height(8.dp))
                    errorMessage?.let {
                        Text(
                            text = it,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                    Button(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = CardGreen,
                            contentColor = Color.White
                        ),
                        onClick = {
                            val parsedAmount = amount.text
                                .filterNot { it.isWhitespace() }
                                .replace(',', '.')
                                .toDoubleOrNull()
                            if (title.isBlank()) {
                                errorMessage = "Enter a name"
                            } else if (parsedAmount == null || parsedAmount <= 0) {
                                errorMessage = "Enter a valid amount"
                            } else if (category.isBlank()) {
                                errorMessage = "Select a category"
                            } else {
                                if (expense == null) {
                                    viewModel.addExpense(
                                        title = title,
                                        amount = parsedAmount,
                                        category = category
                                    )
                                } else {
                                    viewModel.updateExpense(
                                        id = expense.id,
                                        title = title,
                                        amount = parsedAmount,
                                        category = category,
                                        dateTime = expense.dateTime
                                    )
                                }
                                onBackClick()
                            }
                        }
                    ) {
                        Text(
                            text = "Save",
                            fontSize = 20.sp
                        )
                    }

                    if (expense != null) {
                        Button(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(52.dp),
                            onClick = {
                                viewModel.deleteExpense(expense)
                                onBackClick()
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.7f),
                                contentColor = MaterialTheme.colorScheme.onErrorContainer
                            )

                        ) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Delete",
                                tint = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                }
            }
        }
    }
}
