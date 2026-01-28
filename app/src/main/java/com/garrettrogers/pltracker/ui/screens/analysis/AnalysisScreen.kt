package com.garrettrogers.pltracker.ui.screens.analysis

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.garrettrogers.pltracker.data.model.PortfolioSnapshot
import java.math.BigDecimal
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

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
                PortfolioGraph(
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

@Composable
fun PortfolioGraph(
    snapshots: List<PortfolioSnapshot>,
    modifier: Modifier = Modifier
) {
    val lineColor = MaterialTheme.colorScheme.primary
    val gridColor = MaterialTheme.colorScheme.outlineVariant

    // Convert to native Android color for text drawing
    val textPaint = remember {
        android.graphics.Paint().apply {
            color = android.graphics.Color.GRAY
            textSize = 28f
            isAntiAlias = true
        }
    }

    Canvas(modifier = modifier) {
        if (snapshots.size < 2) return@Canvas

        val values = snapshots.mapNotNull { 
            try { BigDecimal(it.totalValue).toFloat() } catch (e: Exception) { null }
        }
        if (values.size < 2) return@Canvas

        val maxVal = values.maxOrNull() ?: 0f
        val minVal = values.minOrNull() ?: 0f
        val range = if (maxVal - minVal == 0f) 1f else maxVal - minVal

        // Padding for axis labels
        val paddingLeft = 70f
        val paddingRight = 20f
        val paddingTop = 20f
        val paddingBottom = 50f

        val graphWidth = size.width - paddingLeft - paddingRight
        val graphHeight = size.height - paddingTop - paddingBottom
        val stepX = graphWidth / (snapshots.size - 1)

        // Draw grid lines and Y-axis labels (5 ticks)
        val yTicks = 5
        for (i in 0..yTicks) {
            val yValue = minVal + (range * i / yTicks)
            val y = paddingTop + graphHeight - (graphHeight * i / yTicks)

            // Grid line
            drawLine(
                color = gridColor,
                start = Offset(paddingLeft, y),
                end = Offset(size.width - paddingRight, y),
                strokeWidth = 1f
            )

            // Y label
            drawContext.canvas.nativeCanvas.drawText(
                "$${yValue.toInt()}",
                10f,
                y + 10f,
                textPaint
            )
        }

        // Draw line path
        val path = Path()
        snapshots.forEachIndexed { index, _ ->
            val value = values.getOrNull(index) ?: return@forEachIndexed
            val x = paddingLeft + index * stepX
            val y = paddingTop + graphHeight - ((value - minVal) / range) * graphHeight

            if (index == 0) {
                path.moveTo(x, y)
            } else {
                path.lineTo(x, y)
            }
        }

        drawPath(
            path = path,
            color = lineColor,
            style = Stroke(width = 3.dp.toPx())
        )

        // Draw X-axis labels (first, middle, last)
        val dateFormat = SimpleDateFormat("MM/dd", Locale.US)
        val xLabelIndices = when {
            snapshots.size <= 3 -> snapshots.indices.toList()
            snapshots.size <= 7 -> listOf(0, snapshots.size / 2, snapshots.size - 1)
            else -> listOf(0, snapshots.size / 4, snapshots.size / 2, 3 * snapshots.size / 4, snapshots.size - 1)
        }

        xLabelIndices.forEach { index ->
            if (index in snapshots.indices) {
                val x = paddingLeft + index * stepX
                val dateStr = dateFormat.format(Date(snapshots[index].date))
                drawContext.canvas.nativeCanvas.drawText(
                    dateStr,
                    x - 20f,
                    size.height - 10f,
                    textPaint
                )
            }
        }
    }
}
