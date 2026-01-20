package com.example.todoapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import com.example.todoapp.ui.theme.TodoAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TodoAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    TodoListScreen(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun TodoListScreen(modifier: Modifier = Modifier) {
    var todoList by remember { mutableStateOf(listOf("groceries", "walk dog")) }
    var completedList by remember { mutableStateOf(listOf<String>()) }
    var newTask by remember { mutableStateOf("") }
    var isAdding by remember { mutableStateOf(false) }

    Column(modifier = modifier.padding(16.dp)) {
        Text(text = "todo list")

        // active tasks with checkbox + delete per row
        todoList.forEach { task ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(top = 4.dp)
            ) {
                Checkbox(
                    checked = false,
                    onCheckedChange = { checked ->
                        if (checked) {
                            // move from todo → completed
                            todoList = todoList.filterNot { it == task }
                            completedList = completedList + task
                        }
                    }
                )

                Text(
                    text = task,
                    modifier = Modifier.weight(1f)
                )

                // delete from todoList
                Button(
                    onClick = {
                        todoList = todoList.filterNot { it == task }
                    }
                ) {
                    Text("X")
                }
            }
        }

        // button to show input
        Button(
            onClick = { isAdding = true },
            modifier = Modifier.padding(top = 8.dp)
        ) {
            Text("add task")
        }

        // input only when adding, submit with Enter/Done
        if (isAdding) {
            OutlinedTextField(
                value = newTask,
                onValueChange = { newTask = it },
                label = { Text("Enter a task") },
                singleLine = true,
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        if (newTask.isNotBlank()) {
                            todoList = todoList + newTask.trim()
                            newTask = ""
                            isAdding = false
                        } else {
                            isAdding = false
                        }
                    }
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            )
        }

        // completed section with undo + "X"
        if (completedList.isNotEmpty()) {
            Text(
                text = "completed",
                modifier = Modifier.padding(top = 16.dp)
            )

            completedList.forEach { task ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(top = 4.dp)
                ) {
                    Text(
                        text = task,
                        modifier = Modifier.weight(1f)
                    )

                    // undo: move back to todo
                    Button(
                        onClick = {
                            completedList = completedList.filterNot { it == task }
                            todoList = todoList + task
                        },
                        modifier = Modifier.padding(end = 4.dp)
                    ) {
                        Text("↺")
                    }

                    // delete from completed, label "X"
                    Button(
                        onClick = {
                            completedList = completedList.filterNot { it == task }
                        }
                    ) {
                        Text("X")
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TodoListPreview() {
    TodoAppTheme {
        TodoListScreen()
    }
}

