package com.garrettrogers.pltracker.ui.screens.analysis

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import java.math.BigDecimal
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnalysisScreen(
    viewModel: AnalysisViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit
) {
    val snapshots by viewModel.snapshots.collectAsState()

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
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Graph Area
            if (snapshots.isNotEmpty()) {
                PortfolioGraph(
                    snapshots = snapshots,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp)
                )
            } else {
                Text(
                    text = "No data points yet. Log your portfolio value below.",
                    modifier = Modifier.height(250.dp)
                )
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

@Composable
fun PortfolioGraph(
    snapshots: List<com.garrettrogers.pltracker.data.model.PortfolioSnapshot>,
    modifier: Modifier = Modifier
) {
    val color = MaterialTheme.colorScheme.primary
    
    Canvas(modifier = modifier) {
        if (snapshots.size < 2) return@Canvas
        
        val values = snapshots.map { it.totalValue.toFloat() }
        val maxVal = values.maxOrNull() ?: 0f
        val minVal = values.minOrNull() ?: 0f
        val range = maxVal - minVal
        
        // Normalize
        val width = size.width
        val height = size.height
        val stepX = width / (snapshots.size - 1)
        
        val path = Path()
        
        snapshots.forEachIndexed { index, _ ->
            val value = values[index]
            val x = index * stepX
            val y = height - ((value - minVal) / (if (range == 0f) 1f else range)) * height
            
            if (index == 0) {
                path.moveTo(x, y)
            } else {
                path.lineTo(x, y)
            }
        }
        
        drawPath(
            path = path,
            color = color,
            style = Stroke(width = 4.dp.toPx())
        )
    }
}
