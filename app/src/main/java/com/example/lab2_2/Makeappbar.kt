package com.example.lab2_2

import android.app.Activity
import android.content.Intent
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.launch

class Makeappbar {
    companion object {
        @OptIn(ExperimentalMaterial3Api::class)
        @Composable
        fun MakeAppBar(model: ItemViewModel, lazyListState: LazyListState) {
            var mDisplayMenu by remember { mutableStateOf(false) }
            val mContext = LocalContext.current
            val openDialog = remember { mutableStateOf(false) }
            val scope = rememberCoroutineScope()//объект для прокручивания списка при вставке нового эл-та
            val startForResult = //переменная-объект класса ManagedActivityResultLauncher,
                //ей присваиваем результат вызова метода rememberLauncherForActivityResult
                rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                        result ->
                    //внутри метода смотрим результат работы запущенного активити – если закрытие с кодом RESULT_OK
                    if (result.resultCode == Activity.RESULT_OK) {//то берем объект из его данных
                        val newLang = result.data?.getSerializableExtra("newItem") as TVProg //как язык
                        println("new lang name = ${newLang.name}") //вывод для отладки
                        model.addProgToEnd(newLang)
                        scope.launch {//прокручиваем список, чтобы был виден добавленный элемент
                            lazyListState.scrollToItem(0)
                        }
                    }
                }
            if (openDialog.value)
                makeAlertDialog(context = mContext, dialogTitle = "About", openDialog = openDialog)

            TopAppBar(
                title = { Text("ТВ Программы") },
                actions = {
                    IconButton(onClick = { mDisplayMenu = !mDisplayMenu }) {
                        Icon(Icons.Default.MoreVert, null)
                    }
                    DropdownMenu(
                        expanded = mDisplayMenu,
                        onDismissRequest = {
                            mDisplayMenu = false
                        }
                    ) {
                        DropdownMenuItem(
                            text = { Text(text = "About") },
                            onClick = {
                                Toast.makeText(mContext, "Kotik Pavel", Toast.LENGTH_SHORT).show()
                                mDisplayMenu = !mDisplayMenu
                                openDialog.value = true
                            }
                        )
                        DropdownMenuItem(
                            text = { Text(text = "Add prog") },
                            onClick = {
                                Toast.makeText(mContext, "Add prog", Toast.LENGTH_SHORT).show()
                                val newAct = Intent(mContext, InputActivity::class.java)
                                mContext.startActivity(newAct)
                                mDisplayMenu = !mDisplayMenu
                            }
                        )
                    }
                })
        }

        private fun makeAlertDialog(context: Any, dialogTitle: String, openDialog: Any) {

        }
    }
}