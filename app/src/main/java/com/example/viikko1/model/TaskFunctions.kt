package com.example.viikko1.model

fun addTask(list: List<Task>, task: Task): List<Task>{
    return list + task
}

fun toggleDone(list: List<Task>, id: Int): List<Task> {
    return list.map {task ->
        if(task.id == id) {
            task.copy(isCompleted = !task.isCompleted)
        } else {
            task
        }
    }
}

fun filterByDone(list: List<Task>, done: Boolean): List<Task> {
    return list.filter { task -> task.isCompleted == done }
}

fun filterByDate(list: List<Task>): List<Task> {
    return list.sortedBy { task -> task.dueDate}
}