package com.example.viikko1.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.viikko1.viewModel.TaskViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: TaskViewModel,
    onNavigateCalendar: () -> Unit = {},
    onNavigateToSettings: () -> Unit = {},
) {
    val tasks by viewModel.task.collectAsState()
    val selectedTask by viewModel.selectedTask.collectAsState()
    var showAddDialog by remember { mutableStateOf(false) }

    Column(modifier = Modifier.padding(all = 16.dp)) {

        TopAppBar(
            title = { Text(text = "Tehtävälista") },
            actions = {
                IconButton(onClick = { showAddDialog = true }) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "Add new task")
                }
                IconButton(onClick = onNavigateCalendar) {
                    Icon(imageVector = Icons.Default.CalendarMonth, contentDescription = "Calendar")
                }
                IconButton(onClick = onNavigateToSettings) {
                    Icon(imageVector = Icons.Default.Settings, contentDescription = "Settings")
                }
            }
        )

        Spacer(modifier = Modifier.height(4.dp))
        HorizontalDivider(thickness = 1.dp)
        Spacer(modifier = Modifier.height(12.dp))

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
                        .clickable { viewModel.selectTask(task) },
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

    if (showAddDialog) {
        AddDialog(
            onClose = { showAddDialog = false },
            onAddTask = { title, description, dueDate ->
                viewModel.addTask(title, description, dueDate)
                showAddDialog = false
            }
        )
    }

    if (selectedTask != null) {
        DetailDialog(
            task = selectedTask!!,
            onClose = { viewModel.closeDialog() },
            onUpdate = { viewModel.updateTask(it) },
            onDelete = { viewModel.removeTask(it) }
        )
    }
}
