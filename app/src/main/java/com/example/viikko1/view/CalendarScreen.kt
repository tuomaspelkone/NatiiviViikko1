package com.example.viikko1.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.viikko1.domain.Task
import com.example.viikko1.viewModel.TaskViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarScreen(
    viewModel: TaskViewModel,
    onTaskClick: (Task) -> Unit = {},
    onNavigateToHome: () -> Unit
) {
    val tasks by viewModel.task.collectAsState()
    val selectedTask by viewModel.selectedTask.collectAsState()
    val grouped = tasks.groupBy { it.dueDate ?: "No date" }

    Column(modifier = Modifier.padding(all = 16.dp)) {

        TopAppBar(
            title = { Text(text = "Calendar", style = MaterialTheme.typography.headlineMedium) },
            navigationIcon = {
                IconButton(onClick = onNavigateToHome) {
                    Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                }
            }
        )

        Spacer(modifier = Modifier.height(4.dp))
        HorizontalDivider(thickness = 1.dp)
        Spacer(modifier = Modifier.height(12.dp))

        LazyColumn {
            grouped.toSortedMap().forEach { (date, tasksOfDay) ->
                item {
                    Text(
                        text = date,
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
                    )
                }
                items(items=tasksOfDay) { task ->
                    CalendarTaskCard(
                        task = task,
                        onTaskClick = onTaskClick
                    )
                }
                }
        }
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

@Composable
fun CalendarTaskCard(
    task: Task,
    onTaskClick: (Task) -> Unit
) {
    Card(
        modifier = Modifier
            .padding(vertical = 4.dp)
            .fillMaxWidth()
            .clickable { onTaskClick(task) }
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(text = task.title, style = MaterialTheme.typography.titleMedium)
            if (task.description.isNotBlank()) {
                Text(text = task.description, style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}
