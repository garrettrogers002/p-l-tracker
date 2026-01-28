package com.garrettrogers.pltracker.ui.screens.analysis

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.garrettrogers.pltracker.data.model.PortfolioSnapshot
import com.garrettrogers.pltracker.ui.components.InteractivePortfolioGraph
import java.util.Calendar

enum class TimeRange(val label: String) {
    WEEK("1W"),
    MONTH("1M"),
    YTD("YTD"),
    SIX_MONTHS("6M"),
    YEAR("1Y"),
    FIVE_YEARS("5Y"),
    ALL("All")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnalysisScreen(
    viewModel: AnalysisViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit
) {
    val snapshots by viewModel.snapshots.collectAsState()
    var selectedRange by remember { mutableStateOf(TimeRange.ALL) }

    val filteredSnapshots = remember(snapshots, selectedRange) {
        filterSnapshotsByRange(snapshots, selectedRange).sortedBy { it.date }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Portfolio Analysis") },
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
            Text("Growth Over Time", style = MaterialTheme.typography.titleMedium)
            
            Spacer(modifier = Modifier.height(8.dp))

            // Time Range Chips
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
            ) {
                TimeRange.entries.forEach { range ->
                    FilterChip(
                        selected = selectedRange == range,
                        onClick = { selectedRange = range },
                        label = { Text(range.label) },
                        modifier = Modifier.padding(end = 4.dp)
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Graph Area
            if (filteredSnapshots.size >= 2) {
                InteractivePortfolioGraph(
                    snapshots = filteredSnapshots,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(280.dp)
                )
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(280.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (snapshots.isEmpty()) "No data. Log your portfolio value below."
                               else "Not enough data for this range (need at least 2 points).",
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Text("Log Daily Value", style = MaterialTheme.typography.titleMedium)
            
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            ) {
                OutlinedTextField(
                    value = viewModel.snapshotValue,
                    onValueChange = { viewModel.snapshotValue = it },
                    label = { Text("Total Value ($)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.weight(1f)
                )
                
                Button(
                    onClick = { viewModel.logSnapshot() },
                    modifier = Modifier.padding(start = 8.dp, top = 8.dp)
                ) {
                    Text("Log")
                }
            }
        }
    }
}

fun filterSnapshotsByRange(
    snapshots: List<PortfolioSnapshot>,
    range: TimeRange
): List<PortfolioSnapshot> {
    if (snapshots.isEmpty() || range == TimeRange.ALL) return snapshots

    val calendar = Calendar.getInstance()

    val cutoff: Long = when (range) {
        TimeRange.WEEK -> { calendar.add(Calendar.DAY_OF_YEAR, -7); calendar.timeInMillis }
        TimeRange.MONTH -> { calendar.add(Calendar.MONTH, -1); calendar.timeInMillis }
        TimeRange.YTD -> {
            calendar.set(Calendar.DAY_OF_YEAR, 1)
            calendar.set(Calendar.HOUR_OF_DAY, 0)
            calendar.set(Calendar.MINUTE, 0)
            calendar.set(Calendar.SECOND, 0)
            calendar.set(Calendar.MILLISECOND, 0)
            calendar.timeInMillis
        }
        TimeRange.SIX_MONTHS -> { calendar.add(Calendar.MONTH, -6); calendar.timeInMillis }
        TimeRange.YEAR -> { calendar.add(Calendar.YEAR, -1); calendar.timeInMillis }
        TimeRange.FIVE_YEARS -> { calendar.add(Calendar.YEAR, -5); calendar.timeInMillis }
        TimeRange.ALL -> 0L
    }

    return snapshots.filter { it.date >= cutoff }
}

