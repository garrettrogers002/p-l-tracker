package com.garrettrogers.pltracker.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.garrettrogers.pltracker.data.model.PortfolioSnapshot
import java.math.BigDecimal
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.roundToInt

data class GraphDimensions(
    val paddingLeft: Float,
    val paddingRight: Float,
    val paddingTop: Float,
    val paddingBottom: Float,
    val graphWidth: Float,
    val graphHeight: Float,
    val stepX: Float
)

@Composable
fun InteractivePortfolioGraph(
    snapshots: List<PortfolioSnapshot>,
    modifier: Modifier = Modifier
) {
    val lineColor = MaterialTheme.colorScheme.primary
    val gridColor = MaterialTheme.colorScheme.outlineVariant
    val crosshairColor = MaterialTheme.colorScheme.tertiary
    val tooltipBackground = MaterialTheme.colorScheme.surfaceVariant
    val tooltipTextColor = MaterialTheme.colorScheme.onSurfaceVariant

    // Touch state
    var isTouching by remember { mutableStateOf(false) }
    var touchX by remember { mutableFloatStateOf(0f) }
    var selectedIndex by remember { mutableStateOf<Int?>(null) }
    var canvasWidth by remember { mutableFloatStateOf(0f) }
    var canvasHeight by remember { mutableFloatStateOf(0f) }

    // Graph calculations
    val values = remember(snapshots) {
        snapshots.mapNotNull { 
            try { BigDecimal(it.totalValue).toFloat() } catch (e: Exception) { null }
        }
    }

    val maxVal = values.maxOrNull() ?: 0f
    val minVal = values.minOrNull() ?: 0f
    val range = if (maxVal - minVal == 0f) 1f else maxVal - minVal

    // Text paint for canvas drawing
    val textPaint = remember {
        android.graphics.Paint().apply {
            color = android.graphics.Color.GRAY
            textSize = 28f
            isAntiAlias = true
        }
    }

    val dateFormat = remember { SimpleDateFormat("MM/dd/yy", Locale.US) }
    val currencyFormat = remember { NumberFormat.getCurrencyInstance(Locale.US) }

    // Calculate dimensions
    fun calculateDimensions(width: Float, height: Float): GraphDimensions {
        val paddingLeft = 70f
        val paddingRight = 20f
        val paddingTop = 20f
        val paddingBottom = 50f
        val graphWidth = width - paddingLeft - paddingRight
        val graphHeight = height - paddingTop - paddingBottom
        val stepX = if (snapshots.size > 1) graphWidth / (snapshots.size - 1) else 0f
        return GraphDimensions(paddingLeft, paddingRight, paddingTop, paddingBottom, graphWidth, graphHeight, stepX)
    }

    // Calculate selected index from touch position
    fun calculateSelectedIndex(touchXPos: Float, dimensions: GraphDimensions): Int? {
        if (snapshots.size < 2) return null
        val relativeX = touchXPos - dimensions.paddingLeft
        if (relativeX < 0 || relativeX > dimensions.graphWidth) return null
        val index = (relativeX / dimensions.stepX).roundToInt()
        return index.coerceIn(0, snapshots.size - 1)
    }

    Box(modifier = modifier) {
        Canvas(
            modifier = Modifier
                .matchParentSize()
                .pointerInput(snapshots) {
                    awaitPointerEventScope {
                        while (true) {
                            // Wait for the first touch down
                            val down = awaitFirstDown(requireUnconsumed = false)
                            
                            isTouching = true
                            touchX = down.position.x
                            val dims = calculateDimensions(canvasWidth, canvasHeight)
                            selectedIndex = calculateSelectedIndex(down.position.x, dims)
                            
                            // Track drag until release
                            do {
                                val event = awaitPointerEvent()
                                val currentPosition = event.changes.firstOrNull()?.position
                                
                                if (currentPosition != null) {
                                    touchX = currentPosition.x
                                    val currentDims = calculateDimensions(canvasWidth, canvasHeight)
                                    selectedIndex = calculateSelectedIndex(currentPosition.x, currentDims)
                                }
                                
                                // Consume all changes to prevent scrolling interference
                                event.changes.forEach { it.consume() }
                                
                            } while (event.changes.any { it.pressed })
                            
                            // Touch released
                            isTouching = false
                            selectedIndex = null
                        }
                    }
                }
        ) {
            canvasWidth = size.width
            canvasHeight = size.height

            if (snapshots.size < 2 || values.size < 2) return@Canvas

            val dims = calculateDimensions(size.width, size.height)

            // Draw grid lines and Y-axis labels (5 ticks)
            val yTicks = 5
            for (i in 0..yTicks) {
                val yValue = minVal + (range * i / yTicks)
                val y = dims.paddingTop + dims.graphHeight - (dims.graphHeight * i / yTicks)

                // Grid line
                drawLine(
                    color = gridColor,
                    start = Offset(dims.paddingLeft, y),
                    end = Offset(size.width - dims.paddingRight, y),
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
                val x = dims.paddingLeft + index * dims.stepX
                val y = dims.paddingTop + dims.graphHeight - ((value - minVal) / range) * dims.graphHeight

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
            val xLabelIndices = when {
                snapshots.size <= 3 -> snapshots.indices.toList()
                snapshots.size <= 7 -> listOf(0, snapshots.size / 2, snapshots.size - 1)
                else -> listOf(0, snapshots.size / 4, snapshots.size / 2, 3 * snapshots.size / 4, snapshots.size - 1)
            }

            xLabelIndices.forEach { index ->
                if (index in snapshots.indices) {
                    val x = dims.paddingLeft + index * dims.stepX
                    val dateStr = SimpleDateFormat("MM/dd", Locale.US).format(Date(snapshots[index].date))
                    drawContext.canvas.nativeCanvas.drawText(
                        dateStr,
                        x - 20f,
                        size.height - 10f,
                        textPaint
                    )
                }
            }

            // Draw crosshair line when touching
            if (isTouching && selectedIndex != null) {
                val idx = selectedIndex!!
                if (idx in snapshots.indices && idx in values.indices) {
                    val x = dims.paddingLeft + idx * dims.stepX
                    
                    // Vertical crosshair line
                    drawLine(
                        color = crosshairColor,
                        start = Offset(x, dims.paddingTop),
                        end = Offset(x, dims.paddingTop + dims.graphHeight),
                        strokeWidth = 2.dp.toPx()
                    )

                    // Dot on the line
                    val value = values[idx]
                    val y = dims.paddingTop + dims.graphHeight - ((value - minVal) / range) * dims.graphHeight
                    drawCircle(
                        color = crosshairColor,
                        radius = 6.dp.toPx(),
                        center = Offset(x, y)
                    )
                }
            }
        }

        // Tooltip overlay when touching
        if (isTouching && selectedIndex != null) {
            val idx = selectedIndex!!
            if (idx in snapshots.indices && idx in values.indices) {
                val snapshot = snapshots[idx]
                val value = values[idx]
                val dateStr = dateFormat.format(Date(snapshot.date))
                val priceStr = currencyFormat.format(value)

                val dims = calculateDimensions(canvasWidth, canvasHeight)
                val xPos = dims.paddingLeft + idx * dims.stepX
                
                // Position tooltip, avoiding edges
                val tooltipXOffset = if (xPos > canvasWidth / 2) {
                    (xPos - 140f).roundToInt()
                } else {
                    (xPos + 10f).roundToInt()
                }

                Surface(
                    modifier = Modifier
                        .offset { IntOffset(tooltipXOffset, 30) }
                        .padding(4.dp),
                    shape = RoundedCornerShape(8.dp),
                    color = tooltipBackground,
                    tonalElevation = 4.dp
                ) {
                    Column(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = dateStr,
                            style = MaterialTheme.typography.labelMedium,
                            color = tooltipTextColor
                        )
                        Text(
                            text = priceStr,
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }
    }
}
