
package com.example.lab4

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lab4.ui.theme.Lab4Theme
import java.util.Locale
import androidx.compose.material3.MaterialTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            Lab4Theme {
                Scaffold(
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    TipCalculator(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun TipCalculator(modifier: Modifier = Modifier) {
    var orderAmount by remember { mutableStateOf("") }
    var dishesCount by remember { mutableStateOf("") }
    var tipPercent by remember { mutableFloatStateOf(0f) }

    val dishes = dishesCount.toIntOrNull() ?: 0

    val discount = when {
        dishes <= 0 -> 0
        dishes <= 2 -> 3
        dishes <= 5 -> 5
        dishes <= 10 -> 7
        else -> 10
    }

    val amount = orderAmount.replace(',', '.').toDoubleOrNull() ?: 0.0

    val discountAmount = amount * discount / 100.0
    val tipAmount = amount * tipPercent / 100.0
    val total = amount - discountAmount + tipAmount

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Сумма заказа:")

            Spacer(modifier = Modifier.width(8.dp))

            TextField(
                value = orderAmount,
                onValueChange = { value ->
                    if (value.isEmpty() ||
                        value.matches(Regex("\\d*[.,]?\\d*"))
                    ) {
                        orderAmount = value
                    }
                },
                modifier = Modifier.weight(1f),
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Decimal
                ),
                placeholder = {
                    Text("0")
                }
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Количество блюд:")

            Spacer(modifier = Modifier.width(8.dp))

            TextField(
                value = dishesCount,
                onValueChange = { value ->
                    if (value.isEmpty() ||
                        value.all { it.isDigit() }
                    ) {
                        dishesCount = value
                    }
                },
                modifier = Modifier.weight(1f),
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                ),
                placeholder = {
                    Text("0")
                }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Чаевые: ${tipPercent.toInt()}%"
        )

        Slider(
            value = tipPercent,
            onValueChange = { tipPercent = it },
            valueRange = 0f..25f,
            steps = 24,
            modifier = Modifier.fillMaxWidth()
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("0", fontSize = 18.sp)
            Text("25", fontSize = 18.sp)
        }

        Spacer(modifier = Modifier.height(20.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Скидка:",
                fontSize = 20.sp
            )

            listOf(3, 5, 7, 10).forEach { percent ->
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    RadioButton(
                        selected = discount == percent,
                        onClick = null
                    )

                    Text(
                        text = "$percent%",
                        fontSize = 18.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(28.dp))

        Text(
            text = "Размер скидки: ${
                String.format(Locale.getDefault(), "%.2f", discountAmount)
            }",
            fontSize = 16.sp
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Чаевые: ${
                String.format(Locale.getDefault(), "%.2f", tipAmount)
            }",
            fontSize = 16.sp
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Итого к оплате: ${
                String.format(Locale.getDefault(), "%.2f", total)
            } ₽",
            fontSize = 20.sp,
            style = MaterialTheme.typography.titleMedium
        )
    }
}
