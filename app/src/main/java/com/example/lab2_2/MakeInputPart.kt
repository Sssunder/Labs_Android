package com.example.lab2_2

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)

@Composable
fun MakeInputPart(model: ItemViewModel, lazyListState: LazyListState) {
    var programName by remember { mutableStateOf("") }
    var showTime by remember { mutableStateOf("") }
    var channelName by remember { mutableStateOf("") }
    var hostName by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()

    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        TextField(
            value = programName,
            onValueChange = { programName = it },
            label = { Text("Название программы") },
            modifier = Modifier.fillMaxWidth()
        )
        TextField(
            value = showTime,
            onValueChange = { showTime = it },
            label = { Text("Время показа") },
            modifier = Modifier.fillMaxWidth()
        )
        TextField(
            value = channelName,
            onValueChange = { channelName = it },
            label = { Text("Название канала") },
            modifier = Modifier.fillMaxWidth()
        )
        TextField(
            value = hostName,
            onValueChange = { hostName = it },
            label = { Text("ФИО ведущего") },
            modifier = Modifier.fillMaxWidth()
        )
        Button(
            onClick = {
                model.addProgToHead(
                    TVProg(programName, showTime, channelName, hostName)
                )
                scope.launch {
                    lazyListState.scrollToItem(0)
                }
                programName = ""
                showTime = ""
                channelName = ""
                hostName = ""
            },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text("Добавить")
        }
    }
}






fun TextField(value: String, onValueChange: Any, textStyle: TextStyle, function: (ItemViewModel, LazyListState) -> Unit) {

}
@Composable
fun MakeList(viewModel: ItemViewModel, lazyListState: LazyListState) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        state = lazyListState //состояние списка соотносим с переданным объектом
    ) {
        items(
            items = viewModel.proglistFlow.value,
            key = { lang -> lang.name },
            itemContent = { item ->
                ListRow(item, onItemClick = { /* определите здесь поведение при нажатии */ })
            }
        )
    }
}

