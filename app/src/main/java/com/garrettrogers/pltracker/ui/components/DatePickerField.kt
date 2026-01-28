package com.garrettrogers.pltracker.ui.components

import android.app.DatePickerDialog
import android.widget.DatePicker
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@Composable
fun DatePickerField(
    label: String,
    date: Long,
    onDateSelected: (Long) -> Unit
) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance().apply { timeInMillis = date }
    val sdf = SimpleDateFormat("MMM dd, yyyy", Locale.US)
    
    val datePickerDialog = DatePickerDialog(
        context,
        { _: DatePicker, year: Int, month: Int, day: Int ->
            calendar.set(year, month, day)
            onDateSelected(calendar.timeInMillis)
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    OutlinedTextField(
        value = sdf.format(Date(date)),
        onValueChange = {},
        label = { Text(label) },
        readOnly = true,
        modifier = Modifier.fillMaxWidth(),
        trailingIcon = {
            Icon(
                Icons.Default.DateRange,
                contentDescription = "Select Date",
                modifier = Modifier.clickable { datePickerDialog.show() }
            )
        }
    )
}
