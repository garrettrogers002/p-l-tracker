package com.garrettrogers.pltracker.ui.screens.trade

import androidx.hilt.navigation.compose.hiltViewModel




import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.garrettrogers.pltracker.ui.components.DatePickerField
import com.garrettrogers.pltracker.ui.components.TradeCard

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.TextButton
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CloseTradeScreen(
    viewModel: CloseTradeViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit
) {
    val trade by viewModel.trade.collectAsState()
    var showDeleteDialog by remember { mutableStateOf(false) }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Delete Trade?") },
            text = { Text("Are you sure you want to delete this active trade? This cannot be undone.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.deleteTrade(onNavigateBack)
                        showDeleteDialog = false
                    },
                    colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("Delete")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Close Position") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            trade?.let { t ->
                Text(
                    text = "Closing Trade for ${t.ticker}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                // Reuse TradeCard to show current details, maybe disabled click
                TradeCard(trade = t, onClick = {}, showEntryCost = true)
                Spacer(modifier = Modifier.height(16.dp))
                
                DatePickerField(
                    label = "Exit Date",
                    date = viewModel.exitDate,
                    onDateSelected = { viewModel.exitDate = it }
                )
                
                OutlinedTextField(
                    value = viewModel.exitOptionPrice,
                    onValueChange = { viewModel.exitOptionPrice = it },
                    label = { Text("Exit Option Px") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )
                
                OutlinedTextField(
                    value = viewModel.exitStockPrice,
                    onValueChange = { viewModel.exitStockPrice = it },
                    label = { Text("Exit Stock Px") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )
                
                Spacer(modifier = Modifier.height(24.dp))
                
                Button(
                    onClick = { viewModel.closeTrade(onNavigateBack) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Text("Close Position")
                }

                Spacer(modifier = Modifier.height(16.dp))

                val deleteColor = MaterialTheme.colorScheme.error
                androidx.compose.material3.OutlinedButton(
                    onClick = { showDeleteDialog = true },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = deleteColor
                    ),
                    border = androidx.compose.foundation.BorderStroke(1.dp, deleteColor)
                ) {
                    Text("Delete Trade (Entry Error)")
                }
            } ?: Text("Loading trade...")
        }
    }
}
