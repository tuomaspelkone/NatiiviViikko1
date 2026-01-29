package com.example.viikko1.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.viikko1.domain.Task
import com.example.viikko1.view.DetailDialog
import com.example.viikko1.viewModel.TaskViewModel

@Composable
fun HomeScreen(viewModel: TaskViewModel = viewModel(), darkTheme: Boolean, onThemeToggle: () -> Unit) {
    val tasks by viewModel.task.collectAsState()
    val selectedTask by viewModel.selectedTask.collectAsState()
    var newTaskTitle by remember { mutableStateOf(value = "") }


    Column(modifier = Modifier.padding(all = 16.dp)) {
        Spacer(modifier = Modifier.height(32.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Tehtävälista", style = MaterialTheme.typography.headlineMedium)
            IconButton(onClick = onThemeToggle) {
                Icon(
                    imageVector = Icons.Filled.Settings,
                    contentDescription = "Vaihda teemaa"
                )
            }
        }

        Spacer(modifier = Modifier.height(height = 16.dp))

        Row {

            TextField(
                value = newTaskTitle,
                onValueChange = { newTaskTitle = it },
                label = { Text(text = "Uusi tehtävä") },
                modifier = Modifier.weight(weight = 1f)
            )

            Spacer(modifier = Modifier.width(width = 8.dp))

            Button(onClick = {
                if (newTaskTitle.isNotBlank()) {
                    viewModel.addTask(newTaskTitle)
                    newTaskTitle = ""
                }
            }) { Text(text = "Lisää uusi ") }

        }

        Spacer(modifier = Modifier.height(height = 8.dp))

        Button(onClick = { viewModel.sortByDueDate() }) {
            Text(text = "Järjestä päivämäärän mukaan")
        }

        Spacer(modifier = Modifier.height(height = 8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(onClick = { viewModel.filterByDone(isDone = true) }) {
                Text(text = "Valmiit")
            }
            Button(onClick = { viewModel.filterByDone(isDone = false) }) {
                Text(text = "Kesken")
            }
            Button(onClick = { viewModel.showAll() }) {
                Text(text = "Kaikki")
            }
        }

        Spacer(modifier = Modifier.height(height = 8.dp))

        LazyColumn() {
            items(items = tasks) { task ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(all = 8.dp)
                        .clickable {viewModel.selectTask(task)},
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Checkbox(
                        checked = task.done,
                        onCheckedChange = {
                            viewModel.toggleDone(id = task.id)
                        }
                    )

                    Text(text = task.title)

                    Button(onClick = { viewModel.removeTask(id = task.id) }) {
                        Text(text = "Poista")
                    }
                }
            }
        }
    }

    if (selectedTask != null) {
        DetailDialog(task=selectedTask!!, onClose = {viewModel.closeDialog()}, onUpdate = {
            viewModel.updateTask(it)
        })
    }
}
