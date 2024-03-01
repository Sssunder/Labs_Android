package com.example.lab2_2

import android.content.Context
import android.content.res.Resources
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow


data class TVProg(
    val name: String,
    val showTime: String,
    val channelName: String,
    val hostName: String,
    var picture: Int = R.drawable.no_picture
)




class MainActivity : ComponentActivity() {
    private val viewModel = ItemViewModel() // Модель данных нашего списка

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainActivityUI(viewModel)
        }
    }
}

@Composable
fun MainActivityUI(viewModel: ItemViewModel) {
    val lazyListState = rememberLazyListState()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            MakeInputPart(viewModel, lazyListState)

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxSize(),
                state = lazyListState
            ) {
                items(
                    items = viewModel._proglistFlow.value,
                    key = { prog -> prog.name },
                    itemContent = { prog ->
                        ListRow(prog, onItemClick = { /* Define your onItemClick behavior here */ })
                    }
                )
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
            .border(BorderStroke(2.dp, Color.Blue)),
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
            modifier = Modifier.size(50.dp).clickable { onItemClick(item) }
        )
    }
}





@Composable
fun EasyList(){
    LazyColumn { //объект для представления списка
        item { // добавляем 1 элемент
            Text(text = "First item", fontSize = 24.sp)
        }
        items(5) { index -> // добавляем 5 эл-ов
            Text(text = "Item: $index", fontSize = 24.sp)
        }
        item { // добавляем 1 элемент
            Text(text = "Last item", fontSize = 24.sp)
        }
    }
}
@Composable
fun MyApplicationComposeTheme(content: @Composable () -> Unit) {
}

class ItemViewModel : ViewModel() {
    private var proglist = mutableStateListOf(
        TVProg("Утреннее шоу", "08:00", "Первый канал", "Иван Иванов", R.drawable.morning_show),
        TVProg("Новости", "12:00", "Россия 1", "Анна Петрова", R.drawable.news),
        TVProg("Спортивный обзор", "15:00", "Спорт", "Алексей Смирнов", R.drawable.sports_review),
        TVProg("Кулинарное шоу", "18:00", "Домашний", "Мария Куликова", R.drawable.cooking_show),
        TVProg("Кино вечера", "20:00", "ТNT", "Петр Сидоров", R.drawable.movie_night)
    )

    //добавляем объект, который будет отвечать за изменения в созданном списке
    val _proglistFlow = MutableStateFlow(proglist)
    //и геттер для него, который его возвращает
    val proglistFlow: StateFlow<List<TVProg>> get() = _proglistFlow
    fun clearList(){ //метод для очистки списка, понадобится в лаб.раб.№5
        proglist.clear()
    }
    fun addProgToHead(lang: TVProg) { //метод для добавления нового языка в начало списка
        proglist.add(0, lang)
    }
    fun addProgToEnd(lang: TVProg) { //метод для добавления нового языка в конец списка
        21
        proglist.add( lang)
    }
    fun removeItem(item: TVProg) { //метод для удаления элемента из списка
        val index = proglist.indexOf(item)
        proglist.remove(proglist[index])
    }
}
@Composable
fun MakeAlertDialog(context: Context, dialogTitle: String, openDialog: MutableState<Boolean>) {
//создаем переменную, в ней будет сохраняться текст, полученный из строковых ресурсов для выбранного языка
    var strValue = remember{ mutableStateOf("") } //для получения значения строки из ресурсов
//получаем id нужной строки из ресурсов через имя в dialogTitle
    val strId = context.resources.getIdentifier(dialogTitle, "string", context.packageName)
//секция try..catch нужна для обработки ошибки Resources.NotFoundException – отсутствие искомого ресурса
    try{ //если такой ресурс есть (т.е. его id не равен 0), то берем само значение этого ресурса
        if (strId != 0) strValue.value = context.getString(strId)
    } catch (e: Resources.NotFoundException) {
        //если произошла ошибка Resources.NotFoundException, то ничего не делаем
    }
    AlertDialog( // создаем AlertDialog
        onDismissRequest = { openDialog.value = false },//действия при закрытии окна
        title = { Text(text = dialogTitle) }, //заголовок окна
        text = { Text(text = strValue.value, fontSize = 20.sp) },//содержимое окна
        confirmButton = { //кнопка Ok, которая будет закрывать окно
            Button(onClick = { openDialog.value = false })
            { Text(text = "OK") }
        }
    )
}