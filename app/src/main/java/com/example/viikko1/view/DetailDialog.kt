package com.example.viikko1.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
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
import com.example.viikko1.data.local.entity.Task
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailDialog(
    task: Task,
    onClose: () -> Unit,
    onUpdate: (Task) -> Unit,
    onDelete: (Task) -> Unit
) {
    var title by remember { mutableStateOf(task.title) }
    var description by remember { mutableStateOf(task.description) }
    var dueDate by remember { mutableStateOf(task.dueDate) }
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
        title = { Text(text = "Muokkaa tehtävää") },
        text = {
            Column {
                TextField(value = title, onValueChange = { title = it }, label = { Text(text = "Nimi") })
                TextField(value = description, onValueChange = { description = it }, label = { Text(text = "Kuvaus") })
                TextField(
                    value = dueDate,
                    onValueChange = { },
                    label = { Text(text = "Eräpäivä") },
                    readOnly = true,
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
                onUpdate(task.copy(title = title, description = description, dueDate = dueDate))
                onClose()
            }) {
                Text(text = "Tallenna")
            }
        },
        dismissButton = {
            Button(onClick = {
                onDelete(task)
                onClose()
            }) {
                Text(text = "Poista")
            }
        }
    )
}