package com.example.lab2_2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier


class InputActivity : ComponentActivity() {
    private val viewModel by viewModels<ItemViewModel>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            InputContent(
                MaterialTheme.colorScheme,
                onProgramAdded = { newProgram ->
                    viewModel.addProgToEnd(newProgram)
                    finish() // Закрытие активити после добавления программы
                }
            )
        }
    }
}


@Composable
fun InputContent(colorScheme: ColorScheme, onProgramAdded: (TVProg) -> Unit) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = colorScheme.background
    ) {
        var programName by remember { mutableStateOf("") }
        var showTime by remember { mutableStateOf("") }
        var channelName by remember { mutableStateOf("") }
        var hostName by remember { mutableStateOf("") }

        Column {
            MakeInputPart(
                programName = programName,
                onProgramNameChange = { programName = it },
                showTime = showTime,
                onShowTimeChange = { showTime = it },
                channelName = channelName,
                onChannelNameChange = { channelName = it },
                hostName = hostName,
                onHostNameChange = { hostName = it },
                onProgramAdded = {
                    // Создание нового объекта программы
                    val newProgram = TVProg(programName, showTime, channelName, hostName)
                    // Вызов колбэка для добавления программы
                    onProgramAdded(newProgram)
                }
            )
        }
    }
}

@Composable
fun MakeInputPart(
    programName: String,
    onProgramNameChange: (String) -> Unit,
    showTime: String,
    onShowTimeChange: (String) -> Unit,
    channelName: String,
    onChannelNameChange: (String) -> Unit,
    hostName: String,
    onHostNameChange: (String) -> Unit,
    onProgramAdded: () -> Unit
) {
    Column {
        TextField(
            value = programName,
            onValueChange = onProgramNameChange,
            label = { Text("Название программы") },
            modifier = Modifier.fillMaxWidth()
        )
        TextField(
            value = showTime,
            onValueChange = onShowTimeChange,
            label = { Text("Время показа") },
            modifier = Modifier.fillMaxWidth()
        )
        TextField(
            value = channelName,
            onValueChange = onChannelNameChange,
            label = { Text("Название канала") },
            modifier = Modifier.fillMaxWidth()
        )
        TextField(
            value = hostName,
            onValueChange = onHostNameChange,
            label = { Text("ФИО ведущего") },
            modifier = Modifier.fillMaxWidth()
        )
        Button(
            onClick = onProgramAdded,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Добавить")
        }
    }
}



// Вспомогательная функция для закрытия активити после добавления программы
fun ComponentActivity.finish() {
    finish()
}

// Вспомогательная функция для закрытия активити после добавления программы
fun InputActivity.finish() {
    finish()
}
