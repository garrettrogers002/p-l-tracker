package com.garrettrogers.pltracker.ui.screens.trade

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel

import android.app.DatePickerDialog
import android.content.Context
import android.widget.DatePicker
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import com.garrettrogers.pltracker.ui.components.DatePickerField

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTradeScreen(
    viewModel: AddTradeViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add Trade") },
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
            OutlinedTextField(
                value = viewModel.ticker,
                onValueChange = { viewModel.ticker = it.uppercase() },
                label = { Text("Ticker") },
                modifier = Modifier.fillMaxWidth()
            )
            
            DatePickerField(
                label = "Entry Date",
                date = viewModel.entryDate,
                onDateSelected = { viewModel.entryDate = it }
            )
            
            DatePickerField(
                label = "Expiration Date",
                date = viewModel.expirationDate,
                onDateSelected = { viewModel.expirationDate = it }
            )
            
            OutlinedTextField(
                value = viewModel.strikePrice,
                onValueChange = { viewModel.strikePrice = it },
                label = { Text("Strike Price") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )
            
            OutlinedTextField(
                value = viewModel.optionPrice,
                onValueChange = { viewModel.optionPrice = it },
                label = { Text("Option Px") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )
            
            OutlinedTextField(
                value = viewModel.stockPrice,
                onValueChange = { viewModel.stockPrice = it },
                label = { Text("Stock Px") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )
            
            OutlinedTextField(
                value = viewModel.quantity,
                onValueChange = { viewModel.quantity = it },
                label = { Text("Quantity") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )
            
            Button(
                onClick = { viewModel.saveTrade(onNavigateBack) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            ) {
                Text("Save Trade")
            }
        }
    }
}


