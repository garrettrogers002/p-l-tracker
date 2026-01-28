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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.garrettrogers.pltracker.ui.components.TradeCard
import java.math.BigDecimal

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(
    viewModel: HistoryViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit
) {
    val historyGroups by viewModel.historyGroups.collectAsState()
    
    // State for collapsed/expanded groups, keyed by monthYear string.
    val expandedStates = remember { androidx.compose.runtime.mutableStateMapOf<String, Boolean>() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Trade History") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, "Back")
                    }
                }
            )
        }
    ) { padding ->
        if (historyGroups.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text("No closed trades yet.")
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {
                historyGroups.forEach { group ->
                    val isExpanded = expandedStates[group.monthYear] ?: true // Default to expanded
                    
                    item {
                        HistoryGroupHeader(
                            group = group,
                            isExpanded = isExpanded,
                            onToggle = { expandedStates[group.monthYear] = !isExpanded }
                        )
                    }
                    
                    if (isExpanded) {
                        items(group.trades) { trade ->
                            TradeCard(
                                trade = trade,
                                onClick = { /* Detail view if needed */ },
                                onDeleteClick = { viewModel.deleteTrade(trade) },
                                showEntryCost = true,
                                showPnl = true
                            )
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
