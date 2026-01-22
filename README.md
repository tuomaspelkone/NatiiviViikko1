# NatiiviReadme

Check each weekly task by using the button above that says "main", then click "tags" and then the week that you are interested in.

## Viikko1

In this weekly assignment I created a tasklist app. There are five mock tasks to begin with and then you can create new ones with a click of a button. You can also mark the latest task as done, toggle the list to only show tasks that are complete and sort them by date.

Add task-button: Adds a task with a timestamp, defaults to not yet done.  
Toggle done-button: Toggles the latest task's boolean to mark it either done or not done.  
Filter true-button: Shows only the tasks that are marked done.  
Sort by date-button: Sorts the task list by date.  
Reset filter-button: Resets the task list.  

## Viikko2

How Compose state management works?
- When using Compose, you don't need to manually update widgets. Instead you tell Compose what is supposed to be shown at a given moment and it does the rest. For example, when using mutableStateOf, Compose automatically catches that and recomposes the UI to match the new data.

Why ViewModel is better than remember?
- ViewModel remembers data trough configuration changes (like screen rotation) while remember would reset. ViewModel is used for coding the logic of your app, remember is used mostly for simple or visible things.
