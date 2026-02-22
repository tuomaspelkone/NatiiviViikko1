package com.example.viikko1.view

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.example.viikko1.data.local.entity.Task
import com.example.viikko1.viewModel.TaskViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: TaskViewModel,
    onNavigateCalendar: () -> Unit,
    onNavigateToSettings: () -> Unit
) {
    // Use the shared ViewModel passed from Activity
    val tasks by viewModel.allTasks.collectAsState(initial = emptyList())
    val pendingCount by viewModel.pendingCount.collectAsState(initial = 0)

    var showAddDialog by remember { mutableStateOf(false) }
    var selectedTask by remember { mutableStateOf<Task?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Tehtävät")
                        Text(
                            "$pendingCount tekemättä",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.deleteCompletedTasks() }) {
                        Icon(imageVector = Icons.Filled.Delete, contentDescription = "Poista valmiit")
                    }
                    IconButton(onClick = onNavigateCalendar) {
                        Icon(imageVector = Icons.Filled.CalendarToday, contentDescription = "Avaa kalenteri")
                    }
                    IconButton(onClick = onNavigateToSettings) {
                        Icon(imageVector = Icons.Filled.Settings, contentDescription = "Avaa asetukset")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showAddDialog = true }) {
                Icon(imageVector = Icons.Filled.Add, contentDescription = "Lisää uusi tehtävä")
            }
        }
    ) { padding ->
        if (tasks.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.CheckCircle,
                        contentDescription = null,
                        modifier = Modifier.size(64.dp),
                        tint = MaterialTheme.colorScheme.secondary
                    )
                    Text("Kaikki tehtävät tehty!", color = MaterialTheme.colorScheme.secondary)
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {
                items(tasks, key = { it.id }) { task ->
                    TaskItem(
                        task = task,
                        onToggle = { viewModel.toggleTask(task) },
                        onDelete = { viewModel.deleteTask(task) },
                        onEdit = { selectedTask = it }
                    )
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

        selectedTask?.let { task ->
            DetailDialog(
                task = task,
                onClose = { selectedTask = null },
                onUpdate = { updated ->
                    viewModel.updateTask(updated)
                    selectedTask = null
                },
                onDelete = { t -> viewModel.deleteTask(t) }
            )
        }
    }
}

@Composable
fun TaskItem(
    task: Task,
    onToggle: () -> Unit,
    onDelete: () -> Unit,
    onEdit: (Task) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp)
            .clickable { expanded = !expanded },
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .animateContentSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(checked = task.isCompleted, onCheckedChange = { onToggle() })

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = task.title,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        textDecoration = if (task.isCompleted) TextDecoration.LineThrough else null,
                        color = if (task.isCompleted) Color.Gray else MaterialTheme.colorScheme.onSurface
                    ),
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(4.dp))

                if (task.dueDate.isNotBlank()) {
                    Text(
                        text = "Eräpäivä: ${task.dueDate}",
                        style = MaterialTheme.typography.bodySmall,
                        color = if (task.isCompleted) Color.Gray else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                if (expanded) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = task.description.ifEmpty { "Ei kuvausta" },
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontStyle = if (task.description.isEmpty()) FontStyle.Italic else FontStyle.Normal,
                            color = if (task.isCompleted) Color.Gray else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            IconButton(onClick = { onEdit(task) }) {
                Icon(imageVector = Icons.Filled.Edit, contentDescription = "Muokkaa")
            }

            IconButton(onClick = onDelete) {
                Icon(imageVector = Icons.Filled.Delete, contentDescription = "Poista tehtävä", tint = MaterialTheme.colorScheme.error)
            }
        }
    }
}
