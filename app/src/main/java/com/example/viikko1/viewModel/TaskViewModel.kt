package com.example.viikko1.viewModel

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

import com.example.viikko1.domain.Task
import com.example.viikko1.domain.mockTasks
import androidx.lifecycle.ViewModel 

public class TaskViewModel : ViewModel() {

    private var allTasks = listOf<Task>()

    var tasks: List<Task> by mutableStateOf(value = listOf<Task>())
        private set

    init {
        allTasks = mockTasks
        tasks = allTasks
    }

    fun addTask(newTask: Task) {
        allTasks = allTasks + newTask
        tasks = allTasks
    }

    fun removeTask(id: Int) {
        allTasks = allTasks.filter { it.id != id }
        tasks = allTasks
    }
    
    fun toggleDone(id: Int) {
        allTasks = allTasks.map { task ->
            if (task.id == id) {
                task.copy(done = !task.done)
            } else {
                task
            }
        }
        tasks = allTasks
    }
    
    fun sortByDueDate() {
        tasks = tasks.sortedBy { it.dueDate }
    }
    
    fun filterByDone(isDone: Boolean) {
        tasks = allTasks.filter { it.done == isDone }
    }
    
    fun showAll() {
        tasks = allTasks
    }
}
