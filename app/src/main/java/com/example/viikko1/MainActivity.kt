package com.example.viikko1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.viikko1.ui.theme.Viikko1Theme
import com.example.viikko1.domain.Task
import com.example.viikko1.domain.mockTasks
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.example.viikko1.domain.addTask
import com.example.viikko1.domain.filterByDate
import com.example.viikko1.domain.filterByDone
import com.example.viikko1.domain.toggleDone

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Viikko1Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Homescreen(modifier = Modifier.padding(paddingValues = innerPadding))
                }
            }
        }
    }
}


@Composable
fun Homescreen(modifier: Modifier) {
    var taskList by remember { mutableStateOf(value = mockTasks) }

    Column(modifier = Modifier.padding(all = 100.dp)) {
        Text(text = "Tasklist", style = androidx.compose.material3.MaterialTheme.typography.headlineMedium)
        androidx.compose.foundation.layout.Spacer(modifier = Modifier.padding(10.dp))
        taskList.forEach { task ->
            Text(text = "${task.title} - Due: ${task.dueDate} - ${task.done}")
        }

        Row(modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly) {

            Button(
                onClick = {
                    val newTask = Task(
                        id = taskList.size + 1,
                        title = "New Task",
                        description = "Added from button",
                        priority = 1,
                        dueDate = "2023-12-31",
                        done = false
                    )
                    taskList = addTask(taskList, newTask)
                },
            ) { Text(text = "Add Task") }

            Button(
                onClick = {
                    taskList = toggleDone(taskList, id = taskList.size)
                },

                ) { Text(text = "Toggle Done") }

        }

        Row(modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly) {

            Button(
                onClick = {
                    taskList = filterByDone(taskList, done = true)
                },

                ) { Text(text = "Filter True") }

            Button(
                onClick = {
                    taskList = filterByDate(taskList)
                },

                ) { Text(text = "Sort by date") }

        }

        Row(modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly) {

        Button(
            onClick = {
                taskList = mockTasks
            },

            ) { Text(text = "Reset Filter") }
    }
    }


}



