# NatiiviReadme

Check each weekly task by using the button above that says "main", then click "tags" and then the week that you are interested in.

## Week1

In this weekly assignment I created a tasklist app. There are five mock tasks to begin with and then you can create new ones with a click of a button. You can also mark the latest task as done, toggle the list to only show tasks that are complete and sort them by date.

Add task-button: Adds a task with a timestamp, defaults to not yet done.  
Toggle done-button: Toggles the latest task's boolean to mark it either done or not done.  
Filter true-button: Shows only the tasks that are marked done.  
Sort by date-button: Sorts the task list by date.  
Reset filter-button: Resets the task list.  

## Week2

How Compose state management works?
- When using Compose, you don't need to manually update widgets. Instead you tell Compose what is supposed to be shown at a given moment and it does the rest. For example, when using mutableStateOf, Compose automatically catches that and recomposes the UI to match the new data.

Why ViewModel is better than remember?
- ViewModel remembers data trough configuration changes (like screen rotation) while remember would reset. ViewModel is used for coding the logic of your app, remember is used mostly for simple or visible things.

## Week3

Explain MVVM and why it's useful in Compose applications.
- MVVM has packets Model, View and ViewModel. It separates the logic and UI of the app thus making the code more clear.

How StateFlow works.
- StateFlow works like a live container for data. When the data inside changes, it automatically tells the screen to update and to show the new information.

## Week4

Navigation:
- Jetpack Compose Navigation is used to move between different screens (Composables) and manage the back stack.
- NavController triggers navigation events, while NavHost is the container that displays the current screen based on the route.
- The app's structure is defined in MainActivity's NavHost, which holds the routes to all screens. Navigation happens by calling navController.navigate().

Architecture
- The architecture uses a single, shared TaskViewModel created in MainActivity. This instance is passed to the composable screens.
- State is shared because both screens observe the same StateFlow from the shared ViewModel. Changes made on one screen are instantly reflected on the other.

Implementation
- CalendarScreen groups tasks from the ViewModel by their dueDate and displays them chronologically in a LazyColumn.
- AlertDialogs are used for both creating and editing tasks. AddDialog calls viewModel.addTask, while DetailDialog calls viewModel.updateTask or viewModel.removeTask.
