package com.example.viikko1.viewModel

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

import com.example.viikko1.domain.Task
import com.example.viikko1.domain.mockTasks
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

public class TaskViewModel : ViewModel() {

    private val allTasks = MutableStateFlow<List<Task>>(value = emptyList())

    private val _task = MutableStateFlow<List<Task>>(value = emptyList())

    val task: StateFlow<List<Task>> = _task.asStateFlow()

    private val _selectedTask = MutableStateFlow<Task?>(value = null)

    val selectedTask: StateFlow<Task?> = _selectedTask.asStateFlow()

    init {
        allTasks.value = mockTasks
        _task.value = mockTasks
    }

    fun addTask(title: String) {
        // Etsi korkein olemassa oleva ID ja lisää siihen 1, jotta ID on aina yksilöllinen
        val newId = (allTasks.value.maxOfOrNull { it.id } ?: 0) + 1
        val newTask = Task(
            id = newId,
            title = title,
            description = "Added from button",
            priority = 1,
            dueDate = "2026-02-02",
            done = false
        )
        allTasks.value = allTasks.value + newTask
        _task.value = allTasks.value
    }

    fun removeTask(id: Int) {
        allTasks.value = allTasks.value.filter { it.id != id }
        _task.value = allTasks.value
    }
    
    fun toggleDone(id: Int) {
        allTasks.value = allTasks.value.map { task ->
            if (task.id == id) {
                task.copy(done = !task.done)
            } else {
                task
            }
        }
        _task.value = allTasks.value
    }
    
    fun sortByDueDate() {
        _task.value = allTasks.value.sortedBy { it.dueDate }
    }
    
    fun filterByDone(isDone: Boolean) {
        _task.value = allTasks.value.filter { it.done == isDone }
    }
    
    fun showAll() {
        _task.value = allTasks.value
    }

    fun selectTask(task: Task) {
        _selectedTask.value = task
    }

    fun updateTask(updatedTask: Task) {
        allTasks.value = allTasks.value.map {
            if (it.id == updatedTask.id) updatedTask else it
            }
        _task.value = allTasks.value
        _selectedTask.value = null
    }

    fun closeDialog() {
        _selectedTask.value = null
    }
}