package com.example.lab2_2

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.lab2_2.ui.theme.Lab2_2Theme

class InputActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Lab2_2Theme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MakeInputPart()
                }
            }
        }
    }
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun MakeInputPart() {
        var tvShow by remember {
            mutableStateOf("")
        }
        var showTime by remember{
            mutableStateOf("")
        }
        var tvChannel by remember {
            mutableStateOf("")
        }
        var fio by remember {
            mutableStateOf("")
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            TextField(
                label = { Text("Программа") },
                value = tvShow.toString(),
                onValueChange = { newText ->
                    tvShow = newText
                },
                modifier = Modifier.fillMaxWidth(),
//            singleLine = true,
                maxLines = 2
            )
            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                TextField(
                    label = { Text("Время показа") },
                    value = showTime.toString(),
                    onValueChange = { newText ->
                        showTime = newText
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.weight(1f)
                )
                TextField(
                    label = { Text("Канал") },
                    value = tvChannel.toString(),
                    onValueChange = { newText ->
                        tvChannel = newText
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.weight(1f)
                )
            }
            TextField(
                label = { Text("ФИО") },
                value = fio.toString(),
                onValueChange = { newText ->
                    fio = newText
                },
                modifier = Modifier.fillMaxWidth(),
//            singleLine = true,
                maxLines = 2
            )
            Button(
                onClick = {
//                        println("added $tvShow $showTime $tvChannel $fio")
                    val newShow = TV(tvShow, showTime, tvChannel, fio)
                    val intent = Intent()
                    intent.putExtra("newItem", newShow)
                    setResult(RESULT_OK, intent)
                    tvShow = ""
                    showTime = ""
                    tvChannel = ""
                    fio = ""
                    finish()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Add")
            }
        }
    }
}