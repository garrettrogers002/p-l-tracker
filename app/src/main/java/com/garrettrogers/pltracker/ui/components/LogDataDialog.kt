package com.garrettrogers.pltracker.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import java.util.Date
import com.garrettrogers.pltracker.ui.components.DatePickerField

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LogDataDialog(
    onDismiss: () -> Unit,
    onLogValue: (Long, String) -> Unit, // Date, Value
    onLogTransaction: (Long, String, String, String) -> Unit // Date, Amount, Type, Note
) {
    var selectedTab by remember { mutableStateOf(0) } // 0 = Value, 1 = Transaction
    var date by remember { mutableStateOf(System.currentTimeMillis()) }
    var amount by remember { mutableStateOf("") }
    
    // Transaction specific
    var transactionType by remember { mutableStateOf("DEPOSIT") } // DEPOSIT or WITHDRAWAL
    var note by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Log Data") },
        text = {
            Column {
                TabRow(selectedTabIndex = selectedTab) {
                    Tab(selected = selectedTab == 0, onClick = { selectedTab = 0 }, text = { Text("Value") })
                    Tab(selected = selectedTab == 1, onClick = { selectedTab = 1 }, text = { Text("Transact") })
                }
                Spacer(modifier = Modifier.height(16.dp))

                DatePickerField(
                    label = "Date",
                    date = date,
                    onDateSelected = { date = it }
                )
                
                OutlinedTextField(
                    value = amount,
                    onValueChange = { amount = it },
                    label = { Text(if (selectedTab == 0) "Portfolio Value ($)" else "Amount ($)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )

                if (selectedTab == 1) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(
                            selected = transactionType == "DEPOSIT",
                            onClick = { transactionType = "DEPOSIT" }
                        )
                        Text("Deposit")
                        Spacer(modifier = Modifier.width(16.dp))
                        RadioButton(
                            selected = transactionType == "WITHDRAWAL",
                            onClick = { transactionType = "WITHDRAWAL" }
                        )
                        Text("Withdraw")
                    }
                    OutlinedTextField(
                        value = note,
                        onValueChange = { note = it },
                        label = { Text("Note (Optional)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (amount.isNotBlank()) {
                        if (selectedTab == 0) {
                            onLogValue(date, amount)
                        } else {
                            onLogTransaction(date, amount, transactionType, note)
                        }
                        onDismiss()
                    }
                }
            ) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
