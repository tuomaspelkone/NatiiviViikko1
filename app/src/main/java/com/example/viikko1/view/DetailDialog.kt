package com.example.viikko1.view

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.example.viikko1.domain.Task
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

@Composable
fun DetailDialog(task: Task, onClose: () -> Unit, onUpdate: (Task) -> Unit) {
    var title by remember { mutableStateOf(value = task.title) }
    var description by remember { mutableStateOf(value = task.description) }
    var dueDate by remember { mutableStateOf(value = task.dueDate) }

    AlertDialog(
        onDismissRequest = onClose,
        title = { Text(text = "Edit task")},
        text = {
            Column {
                TextField(value = title, onValueChange = { title = it }, label = { Text(text = "Title")})
                TextField(value = description, onValueChange = { description = it }, label = { Text(text = "Description")})
                TextField( value = dueDate, onValueChange = { dueDate = it }, label = { Text(text = "Due date") })
            }
        },
        confirmButton = {
            Button(onClick = {
                onUpdate(task.copy(title = title, description = description, dueDate = dueDate))
            })
            { Text(text = "Save") }
        },
        dismissButton = {
            Button(onClick = onClose) {
                Text(text = "Cancel")
            }
        }

    )
}