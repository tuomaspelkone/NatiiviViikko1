package com.example.viikko1.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddDialog(onClose: () -> Unit, onAddTask: (String, String, String) -> Unit) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var dueDate by remember { mutableStateOf("") }
    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        val selectedDate = datePickerState.selectedDateMillis
                        if (selectedDate != null) {
                            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                            dueDate = sdf.format(Date(selectedDate))
                        }
                        showDatePicker = false
                    }
                ) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showDatePicker = false
                    }
                ) {
                    Text("Cancel")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    AlertDialog(
        onDismissRequest = onClose,
        title = { Text(text = "Lisää uusi tehtävä") },
        text = {
            Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(8.dp)) {
                // Otsikkokenttä
                TextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text(text = "Nimi") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                // Kuvauskenttä (monirivinen)
                TextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text(text = "Kuvaus (valinnainen)") },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 3
                )
                // Eräpäiväkenttä (readonly, avaa kalenterin)
                TextField(
                    value = dueDate,
                    onValueChange = { },
                    label = { Text(text = "Eräpäivä") },
                    readOnly = true,
                    modifier = Modifier.fillMaxWidth(),
                    trailingIcon = {
                        Icon(
                            Icons.Default.DateRange,
                            contentDescription = "Valitse päivämäärä",
                            modifier = Modifier.clickable { showDatePicker = true }
                        )
                    }
                )
            }
        },
        confirmButton = {
            Button(onClick = {
                onAddTask(title.trim(), description.trim(), dueDate)
                onClose()
            }, enabled = title.isNotBlank()) {
                Text(text = "Tallenna")
            }
        },
        dismissButton = {
            Button(onClick = onClose) {
                Text(text = "Peruuta")
            }
        }
    )
}
