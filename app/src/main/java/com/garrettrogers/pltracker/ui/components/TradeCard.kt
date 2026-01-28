package com.garrettrogers.pltracker.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.garrettrogers.pltracker.data.model.Trade
import java.math.BigDecimal
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton

@Composable
fun TradeCard(
    trade: Trade,
    onClick: () -> Unit,
    onDeleteClick: (() -> Unit)? = null,
    showEntryCost: Boolean = true,
    showPnl: Boolean = false
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = trade.ticker,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    if (onDeleteClick != null) {
                        IconButton(onClick = onDeleteClick) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete", tint = MaterialTheme.colorScheme.error)
                        }
                    }
                }
                Text(
                    text = "Q: ${trade.quantity}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            
            val sdf = SimpleDateFormat("MMM dd, yyyy", Locale.US)
            val expires = sdf.format(Date(trade.expirationDate))
            
            Text(
                text = "${trade.strikePrice} Strike • Exp: $expires",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            if (showEntryCost) {
                // Entry Cost = Option Price * Quantity * 100
                val entryPrice = BigDecimal(trade.entryOptionPrice)
                val quantity = BigDecimal(trade.quantity)
                val multiplier = BigDecimal(100)
                val cost = entryPrice.multiply(quantity).multiply(multiplier)
                
                Text(
                    text = "Entry Cost: $${cost}",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
            
            if (showPnl && trade.isClosed && trade.exitOptionPrice != null) {
                // P&L = (Exit Price - Entry Price) * Quantity * 100
                val entryPrice = BigDecimal(trade.entryOptionPrice)
                val exitPrice = BigDecimal(trade.exitOptionPrice)
                val quantity = BigDecimal(trade.quantity)
                val multiplier = BigDecimal(100)
                val pnl = exitPrice.subtract(entryPrice).multiply(quantity).multiply(multiplier)
                
                val pnlColor = if (pnl.compareTo(BigDecimal.ZERO) >= 0) Color.Green else Color.Red
                val prefix = if (pnl.compareTo(BigDecimal.ZERO) >= 0) "+" else ""
                
                Text(
                    text = "P&L: $prefix$${pnl}",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    color = pnlColor,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }
    }
}
