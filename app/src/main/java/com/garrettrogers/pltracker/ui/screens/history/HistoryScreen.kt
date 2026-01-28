package com.garrettrogers.pltracker.ui.screens.history

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.runtime.Composable
import androidx.compose.material3.Text
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.toMutableStateMap
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.material3.TabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults

import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.garrettrogers.pltracker.ui.components.TradeCard
import java.math.BigDecimal

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.TextButton
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.garrettrogers.pltracker.data.model.Trade
import com.garrettrogers.pltracker.data.model.AccountTransaction

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(
    viewModel: HistoryViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit,
    onNavigateToDetails: (Long) -> Unit
) {
    val historyGroups by viewModel.historyGroups.collectAsState()
    val transactions by viewModel.transactions.collectAsState()
    val snapshots by viewModel.snapshots.collectAsState()
    
    // State for collapsed/expanded groups, keyed by monthYear string.
    val expandedStates = remember { androidx.compose.runtime.mutableStateMapOf<String, Boolean>() }
    
    // State for Delete Confirmation
    var tradeToDelete by remember { mutableStateOf<Trade?>(null) }
    var snapshotToDelete by remember { mutableStateOf<com.garrettrogers.pltracker.data.model.PortfolioSnapshot?>(null) }
    var transactionToDelete by remember { mutableStateOf<com.garrettrogers.pltracker.data.model.AccountTransaction?>(null) }
    
    // State for Log Dialog
    var showLogDialog by remember { mutableStateOf(false) }
    
    // Tab State
    var selectedTab by remember { androidx.compose.runtime.mutableStateOf(0) }

    if (showLogDialog) {
        com.garrettrogers.pltracker.ui.components.LogDataDialog(
            onDismiss = { showLogDialog = false },
            onLogValue = { date, value -> viewModel.logPortfolioValue(date, value) },
            onLogTransaction = { date, amount, type, note -> viewModel.logTransaction(date, amount, type, note) }
        )
    }

    if (tradeToDelete != null) {
        AlertDialog(
            onDismissRequest = { tradeToDelete = null },
            title = { Text("Delete Trade?") },
            text = { Text("Are you sure you want to delete ${tradeToDelete?.ticker}?") },
            confirmButton = {
                TextButton(onClick = {
                    tradeToDelete?.let { viewModel.deleteTrade(it) }
                    tradeToDelete = null
                }, colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.error)) { Text("Delete") }
            },
            dismissButton = {
                TextButton(onClick = { tradeToDelete = null }) { Text("Cancel") }
            }
        )
    }
    
    if (snapshotToDelete != null) {
        AlertDialog(
            onDismissRequest = { snapshotToDelete = null },
            title = { Text("Delete Entry?") },
            text = { Text("Delete portfolio value log for ${formatDate(snapshotToDelete!!.date)}?") },
            confirmButton = {
                TextButton(onClick = {
                    snapshotToDelete?.let { viewModel.deleteSnapshot(it) }
                    snapshotToDelete = null
                }, colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.error)) { Text("Delete") }
            },
            dismissButton = {
                TextButton(onClick = { snapshotToDelete = null }) { Text("Cancel") }
            }
        )
    }

    if (transactionToDelete != null) {
        AlertDialog(
            onDismissRequest = { transactionToDelete = null },
            title = { Text("Delete Transaction?") },
            text = { Text("Delete ${transactionToDelete!!.type} of $${transactionToDelete!!.amount}?") },
            confirmButton = {
                TextButton(onClick = {
                    transactionToDelete?.let { viewModel.deleteTransaction(it) }
                    transactionToDelete = null
                }, colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.error)) { Text("Delete") }
            },
            dismissButton = {
                TextButton(onClick = { transactionToDelete = null }) { Text("Cancel") }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("History") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, "Back")
                    }
                }
            )
        },
        floatingActionButton = {
            androidx.compose.material3.FloatingActionButton(
                onClick = { showLogDialog = true },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(Icons.Default.Add, "Log Data")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            androidx.compose.material3.TabRow(selectedTabIndex = selectedTab) {
                androidx.compose.material3.Tab(selected = selectedTab == 0, onClick = { selectedTab = 0 }, text = { Text("Trades") })
                androidx.compose.material3.Tab(selected = selectedTab == 1, onClick = { selectedTab = 1 }, text = { Text("Transact") })
                androidx.compose.material3.Tab(selected = selectedTab == 2, onClick = { selectedTab = 2 }, text = { Text("Portfolio") })
            }
            
            when (selectedTab) {
                0 -> { // TRADES
                    if (historyGroups.isEmpty()) {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { Text("No closed trades.") }
                    } else {
                        LazyColumn(modifier = Modifier.fillMaxSize(), contentPadding = PaddingValues(16.dp)) {
                            historyGroups.forEach { group ->
                                val isExpanded = expandedStates[group.monthYear] ?: true
                                item {
                                    HistoryGroupHeader(group, isExpanded) { expandedStates[group.monthYear] = !isExpanded }
                                }
                                if (isExpanded) {
                                    items(group.trades) { trade ->
                                        TradeCard(trade, { onNavigateToDetails(trade.id) }, { tradeToDelete = trade }, true, true)
                                    }
                                }
                            }
                        }
                    }
                }
                1 -> { // TRANSACTIONS
                   if (transactions.isEmpty()) {
                       Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { Text("No transactions.") }
                   } else {
                       LazyColumn(modifier = Modifier.fillMaxSize(), contentPadding = PaddingValues(16.dp)) {
                           items(transactions) { trans ->
                               Card(
                                   modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                                   colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                               ) {
                                   Row(
                                       modifier = Modifier.fillMaxWidth().padding(16.dp),
                                       horizontalArrangement = Arrangement.SpaceBetween,
                                       verticalAlignment = Alignment.CenterVertically
                                   ) {
                                       Column(modifier = Modifier.weight(1f)) {
                                           Text(trans.type, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = if (trans.type == "DEPOSIT") Color.Green else Color.Red)
                                           Text(formatDate(trans.date), style = MaterialTheme.typography.bodySmall)
                                           if (trans.note.isNotBlank()) Text(trans.note, style = MaterialTheme.typography.bodyMedium)
                                       }
                                       Row(verticalAlignment = Alignment.CenterVertically) {
                                           Text("$${trans.amount}", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                                           IconButton(onClick = { transactionToDelete = trans }) {
                                               Icon(Icons.Default.Delete, "Delete", tint = Color.Gray)
                                           }
                                       }
                                   }
                               }
                           }
                       }
                   }
                }
                2 -> { // PORTFOLIO SNAPSHOTS
                    if (snapshots.isEmpty()) {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { Text("No portfolio logs.") }
                    } else {
                        LazyColumn(modifier = Modifier.fillMaxSize(), contentPadding = PaddingValues(16.dp)) {
                            items(snapshots) { snap ->
                                Card(
                                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth().padding(16.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Column {
                                            Text("Value Log", style = MaterialTheme.typography.labelSmall)
                                            Text(formatDate(snap.date), style = MaterialTheme.typography.bodyMedium)
                                        }
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Text("$${snap.totalValue}", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                                            IconButton(onClick = { snapshotToDelete = snap }) {
                                                Icon(Icons.Default.Delete, "Delete", tint = Color.Gray)
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun HistoryGroupHeader(
    group: HistoryGroup,
    isExpanded: Boolean,
    onToggle: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onToggle() }
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                contentDescription = if (isExpanded) "Collapse" else "Expand",
                tint = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = group.monthYear,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
        
        val pnlColor = if (group.totalPnl >= BigDecimal.ZERO) Color.Green else Color.Red
        val prefix = if (group.totalPnl >= BigDecimal.ZERO) "+" else ""
        
        Text(
            text = "$prefix$${group.totalPnl}",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = pnlColor
        )
    }
}

private fun formatDate(millis: Long): String {
    val sdf = java.text.SimpleDateFormat("MMM dd, yyyy", java.util.Locale.US)
    return sdf.format(java.util.Date(millis))
}
