package com.garrettrogers.pltracker.ui.screens.dashboard

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.garrettrogers.pltracker.ui.components.TradeCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    viewModel: DashboardViewModel = hiltViewModel(),
    onNavigateToAddTrade: () -> Unit,
    onNavigateToHistory: () -> Unit,
    onNavigateToAnalysis: () -> Unit,
    onNavigateToCloseTrade: (Long) -> Unit
) {
    val activeTrades by viewModel.activeTrades.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Active Trades") },
                actions = {
                    IconButton(onClick = onNavigateToAnalysis) {
                        Icon(imageVector = Icons.Default.Info, contentDescription = "Analysis")
                    }
                    IconButton(onClick = onNavigateToHistory) {
                        Icon(imageVector = Icons.Default.DateRange, contentDescription = "History")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onNavigateToAddTrade) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add Trade")
            }
        }
    ) { padding ->
        if (activeTrades.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text("No active trades. Add one!")
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentPadding = PaddingValues(bottom = 80.dp)
            ) {
                items(activeTrades) { trade ->
                    TradeCard(
                        trade = trade,
                        onClick = { onNavigateToCloseTrade(trade.id) }
                    )
                }
            }
        }
    }
}
