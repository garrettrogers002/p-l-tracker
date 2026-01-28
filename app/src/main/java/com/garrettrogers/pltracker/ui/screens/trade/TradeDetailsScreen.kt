package com.garrettrogers.pltracker.ui.screens.trade

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.garrettrogers.pltracker.ui.components.TradeCard
import java.math.BigDecimal
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TradeDetailsScreen(
    tradeId: Long,
    viewModel: CloseTradeViewModel = hiltViewModel(), // Reusing CloseTradeViewModel for getById logic
    onNavigateBack: () -> Unit
) {
    val trade by viewModel.trade.collectAsState()
    var showDeleteDialog by remember { mutableStateOf(false) }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Delete Trade?") },
            text = { Text("Are you sure you want to delete this trade history? This cannot be undone.") },
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
                title = { Text("Trade Details") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { showDeleteDialog = true }) {
                        Icon(Icons.Default.Delete, "Delete", tint = MaterialTheme.colorScheme.error)
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
                .verticalScroll(rememberScrollState())
        ) {
            trade?.let { t ->
                TradeCard(trade = t, onClick = {}, showEntryCost = true, showPnl = true)
                
                Spacer(modifier = Modifier.height(24.dp))
                
                Text("Trade Info", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                
                DetailRow("Ticker", t.ticker)
                DetailRow("Strike", "${t.strikePrice}")
                DetailRow("Quantity", "${t.quantity}")
                
                Spacer(modifier = Modifier.height(16.dp))
                Text("Entry Details", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                DetailRow("Entry Date", formatDate(t.entryDate))
                DetailRow("Entry Option Price", "$${t.entryOptionPrice}")
                DetailRow("Entry Stock Price", "$${t.entryStockPrice}")
                
                Spacer(modifier = Modifier.height(16.dp))
                Text("Exit Details", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                DetailRow("Exit Date", t.exitDate?.let { formatDate(it) } ?: "-")
                DetailRow("Exit Option Price", t.exitOptionPrice?.let { "$$it" } ?: "-")
                DetailRow("Exit Stock Price", t.exitStockPrice?.let { "$$it" } ?: "-")
                
                if (t.isClosed && t.exitOptionPrice != null) {
                    Spacer(modifier = Modifier.height(24.dp))
                    val pnl = calculatePnl(t)
                    val color = if (pnl >= BigDecimal.ZERO) Color.Green else Color.Red
                    val prefix = if (pnl >= BigDecimal.ZERO) "+" else ""
                    
                    Text(
                        text = "Total P&L: $prefix$${pnl}",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = color
                    )
                }

            } ?: Text("Loading trade details...")
        }
    }
}

@Composable
fun DetailRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(text = value, fontWeight = FontWeight.SemiBold)
    }
}

fun formatDate(millis: Long): String {
    val sdf = SimpleDateFormat("MMM dd, yyyy", Locale.US)
    return sdf.format(Date(millis))
}

fun calculatePnl(trade: com.garrettrogers.pltracker.data.model.Trade): BigDecimal {
     return try {
        if (trade.entryOptionPrice.isBlank() || trade.exitOptionPrice.isNullOrBlank()) {
            BigDecimal.ZERO
        } else {
            val entryObj = BigDecimal(trade.entryOptionPrice)
            val exitObj = BigDecimal(trade.exitOptionPrice)
            val qty = BigDecimal(trade.quantity)
            val multiplier = BigDecimal(100)
            exitObj.subtract(entryObj).multiply(qty).multiply(multiplier)
        }
    } catch (e: Exception) {
        BigDecimal.ZERO
    }
}
