package com.example.lab2_2

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.io.Serializable

data class TV(val name: String, val time:String, val channel: String, val fio:String, var picture: String = R.drawable.no_picture.toString() ):Serializable
class ItemViewModel : ViewModel() {
    private var TVList = mutableStateListOf(
        TV("Папины дочки", "14.30","СТС", "Вячеслав Муругов", R.drawable.papini_dochki.toString()),
        TV("Счатсливы Вместе", "8.30","ТНТ", "Рон Левит", R.drawable.schastlivy_vmeste.toString()),
        TV("Реальные пацаны", "9.20","ТНТ", "Антон Зайцев", R.drawable.real_pacany.toString()),
        TV("Универ", "10.00","ТНТ", "Кирилл Ткаченко", R.drawable.univer.toString()),
        TV("Кухня", "19.30","СТС", "Василий Куценко", R.drawable.kuhnya.toString())
    )
    private val _TVListFlow = MutableStateFlow(TVList)
    val TVListFlow: StateFlow<List<TV>> get() = _TVListFlow
    fun clearList(){
        TVList.clear()
    }
    fun addTVToHead(tv: TV) {
        TVList.add(0, tv)
    }
    fun addTVToEnd(tv: TV) {
        TVList.add(tv)
    }
    fun isContains(tv: TV): Boolean {
        return TVList.contains(tv)
    }
    fun removeItem(item: TV) {
        val index = TVList.indexOf(item)
        TVList.remove(TVList[index])
    }
    fun changeImage(index: Int, value: String){
        TVList[index] = TVList[index].copy(picture = value)
    }
}