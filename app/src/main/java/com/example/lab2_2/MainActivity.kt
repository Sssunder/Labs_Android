package com.example.lab2_2

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import com.example.lab2_2.Makeappbar.Companion.MakeAppBar
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.io.Serializable

data class TVProg(
    val name: String,
    val showTime: String,
    val channelName: String,
    val hostName: String,
    var picture: Int = R.drawable.no_picture,
) : Serializable

class MainActivity : ComponentActivity() {
    private val viewModel by viewModels<ItemViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainActivityContent(viewModel)
        }
    }
}

@Composable
fun MainActivityContent(viewModel: ItemViewModel) {
    val lazyListState = rememberLazyListState()
    var previousItemCount by remember { mutableIntStateOf(0) }
    val progList by viewModel.proglistFlow.collectAsState(initial = emptyList())

    // Добавим лог для проверки вызова Composable функции
    Log.d("MainActivityContent", "MainActivityContent вызван")

    // Добавим лог перед использованием collectAsState
    Log.d("MainActivityContent", "Прогрессивный список: $progList")

    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            MakeAppBar(viewModel, lazyListState)

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxSize(),
                state = lazyListState
            ) {
                itemsIndexed(
                    items = progList,
                    key = { _, prog -> prog.name },
                    itemContent = { _, prog ->
                        ListRow(prog, onItemClick = { item ->
                            viewModel.addProgToEnd(item)
                        })
                    }
                )

                val newItemCount = progList.size

                if (newItemCount > previousItemCount) {
                    Log.d("LazyColumn", "Новый элемент добавлен в LazyColumn")
                    previousItemCount = newItemCount
                }
            }

        }
    }
}




@Composable
fun ListRow(item: TVProg, onItemClick: (TVProg) -> Unit) {
    Row(
        modifier = Modifier
            .wrapContentHeight()
            .fillMaxWidth()
            .border(BorderStroke(2.dp, Color.Blue))
            .clickable { onItemClick(item) }, // Add clickable modifier here
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = "Название: ${item.name}",
                fontSize = 24.sp,
                fontWeight = FontWeight.SemiBold,
            )
            Text(
                text = "Время показа: ${item.showTime}",
                fontSize = 16.sp,
                fontStyle = FontStyle.Italic,
            )
            Text(
                text = "Канал: ${item.channelName}",
                fontSize = 16.sp,
                fontStyle = FontStyle.Italic,
            )
            Text(
                text = "Ведущий: ${item.hostName}",
                fontSize = 16.sp,
                fontStyle = FontStyle.Italic,
            )
        }
        Image(
            painter = painterResource(id = item.picture),
            contentDescription = null,
            modifier = Modifier
                .size(50.dp)
        )
    }
}

private var proglist = mutableStateListOf(
    TVProg("Утреннее шоу", "08:00", "Первый канал", "Иван Иванов", R.drawable.morning_show),
    TVProg("Новости", "12:00", "Россия 1", "Анна Петрова", R.drawable.news),
    TVProg("Спортивный обзор", "15:00", "Спорт", "Алексей Смирнов", R.drawable.sports_review),
    TVProg("Кулинарное шоу", "18:00", "Домашний", "Мария Куликова", R.drawable.cooking_show),
    TVProg("Кино вечера", "20:00", "ТNT", "Петр Сидоров", R.drawable.movie_night)
)

class ItemViewModel : ViewModel() {

    //добавляем объект, который будет отвечать за изменения в созданном списке
    private val _proglistFlow = MutableStateFlow(proglist)
    //и геттер для него, который его возвращает
    val proglistFlow: StateFlow<List<TVProg>> get() = _proglistFlow
    fun clearList(){ //метод для очистки списка, понадобится в лаб.раб.№5
        proglist.clear()
        _proglistFlow.value = proglist // Обновляем StateFlow после очистки списка
        Log.d("ItemViewModel", "Список программ очищен")
    }

    fun addProgToHead(item: TVProg) {
        proglist.add(0, item)
        _proglistFlow.value = proglist // Обновляем StateFlow после добавления программы в начало списка
        Log.d("ItemViewModel", "Новая программа добавлена в начало списка: $item")
    }

    fun addProgToEnd(item: TVProg) {
        Log.d("ItemViewModel", "Добавление новой программы: $item")
        proglist.add(item) // Добавляем переданный объект tvProg в список
        _proglistFlow.value = proglist // Обновляем StateFlow после добавления программы в конец списка
        Log.d("ItemViewModel", "Список программ после добавления: $proglist")
    }

    fun removeItem(item: TVProg) {
        proglist.remove(item)
        _proglistFlow.value = proglist // Обновляем StateFlow после удаления программы из списка
        Log.d("ItemViewModel", "Программа удалена: $item")
    }
}

