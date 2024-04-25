package com.example.lab2_2

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Resources
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.example.lab2_2.ui.theme.Lab2_2Theme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private val viewModel = ItemViewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState != null && savedInstanceState.containsKey("TVs")){
            val tempTVArray = savedInstanceState.getSerializable("TVs") as ArrayList<TV>
            viewModel.clearList()
            tempTVArray.forEach {
                viewModel.addTVToEnd(it)
            }
        }
        setContent {
            val lazyListState = rememberLazyListState()
            Lab2_2Theme  {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color.Gray,
                    contentColor = Color.Black
                ) {
                    Column(Modifier.fillMaxSize()) {
                        MakeAppBar(viewModel, lazyListState)
                        MakeList(viewModel, lazyListState)
                    }
                }
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun MakeAppBar(model: ItemViewModel, lazyListState: LazyListState) {

        var mDisplayMenu by remember { mutableStateOf(false) }
        val mContext = LocalContext.current
        val openDialog = remember { mutableStateOf(false)}
        val scope = rememberCoroutineScope()
        val startForResult =
            rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                    result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val newTV = result.data?.getSerializableExtra("newItem") as TV
                    if (!model.isContains(newTV)) {
                        model.addTVToHead(newTV)
                        scope.launch {
                            lazyListState.scrollToItem(0)
                        }
                    }else{
                        Toast.makeText(mContext, "В списке уже есть эта программа", Toast.LENGTH_LONG).show()
                    }
                }
            }
        if (openDialog.value)
            MakeAlertDialog(context = mContext, dialogTitle = "About", openDialog = openDialog)
        TopAppBar(
            title = { Text("TV Shows") },
            actions = {
                IconButton(onClick = { mDisplayMenu = !mDisplayMenu }) {
                    Icon(Icons.Default.MoreVert, null)
                }
                DropdownMenu(
                    expanded = mDisplayMenu,
                    onDismissRequest = { mDisplayMenu = false }
                ) {
                    DropdownMenuItem(
                        text = { Text(text = "About")},
                        onClick = {
                            mDisplayMenu = !mDisplayMenu
                            openDialog.value = true
                        }
                    )
                    DropdownMenuItem(
                        text = { Text(text = "Add TV") },
                        onClick = {
                            val newAct = Intent(mContext, InputActivity::class.java)
                            startForResult.launch(newAct)
                            mDisplayMenu = !mDisplayMenu
                        }
                    )

                }
            }
        )
    }
    @Composable
    fun MakeList(viewModel: ItemViewModel, lazyListState: LazyListState) {
        val TVListState = viewModel.TVListFlow.collectAsState()
        LazyColumn(
            //        verticalArrangement = Arrangement.spacedBy(5.dp),
            modifier = Modifier
                .fillMaxSize(),
//                .background(Color.White),
            state = lazyListState
        ) {
            items(
                items = viewModel.TVListFlow.value,
                key = { tv -> tv.name },
                itemContent = { item ->
                    ListColumn(item, TVListState, viewModel)
                }
            )
        }
    }
    @SuppressLint("DiscouragedApi")
    @Composable
    fun MakeAlertDialog(context: Context, dialogTitle: String, openDialog: MutableState<Boolean>) {
        var strValue = remember{ mutableStateOf("") }
        val strId = context.resources.getIdentifier(dialogTitle.replace(" ", ""), "string", context.packageName)

        try{
            if (strId != 0) strValue.value = context.getString(strId)
        } catch (e: Resources.NotFoundException) {
        }
        AlertDialog(
            onDismissRequest = { openDialog.value = false },
            title = { Text(text = dialogTitle) },
            text = { Text(text = strValue.value, fontSize = 14.sp) },
            confirmButton = {
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { openDialog.value = false })
                { Text(text = "OK") }
            },

            )
    }
    @OptIn(ExperimentalFoundationApi::class, ExperimentalCoilApi::class)
    @Composable
    fun ListColumn(
        item: TV,
        TVListState: State<List<TV>>,
        viewModel: ItemViewModel
    ) {
        val context = LocalContext.current
        val openDialog = remember { mutableStateOf(false) }
        val TVSelected = remember { mutableStateOf("") }

        if (openDialog.value)
            MakeAlertDialog(context, TVSelected.value, openDialog)

        var mDisplayMenu by remember { mutableStateOf(false) }
        val launcher =
            rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { res ->
                if (res.data?.data != null) {
                    val imgURI = res.data?.data
//                    Toast.makeText(this@MainActivity, imgURI.toString(), Toast.LENGTH_LONG).show()
                    val index = TVListState.value.indexOf(item)
                    viewModel.changeImage(index, imgURI.toString())
                }
            }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .wrapContentHeight()
                .fillMaxWidth()
                .border(BorderStroke(6.dp, Color.White))
                .background(Color(255, 255, 255, 255))
                .combinedClickable(
                    onClick = {
                        println("item = ${item.name}")
                        TVSelected.value = item.name
                        openDialog.value = true
                    },
                    onLongClick = { mDisplayMenu = true }
                )
        ) {
            Text(
                text = item.name,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(10.dp),
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
            ) {
                Image(
                    painter = if (pictureIsInt(item.picture))
                        painterResource(item.picture.toInt())
                    else rememberImagePainter(item.picture),
                    contentDescription = "",
                    contentScale = ContentScale.FillHeight,
                    modifier = Modifier
                        .clip(RoundedCornerShape(25.dp))
                        .width(200.dp)
                        .height(210.dp)
                )

                Column(
                    horizontalAlignment = Alignment.Start,
                    modifier = Modifier.width(80.dp)
                ) {
                    DropdownMenu(
                        expanded = mDisplayMenu,
                        onDismissRequest = { mDisplayMenu = false },
                    ) {
                        DropdownMenuItem(
                            text = { Text(text = "Change image", fontSize = 12.sp) },
                            onClick = {
                                mDisplayMenu = !mDisplayMenu
                                val permission: String = Manifest.permission.READ_EXTERNAL_STORAGE
                                val grant = ContextCompat.checkSelfPermission(context, permission)
//                            Toast.makeText(context, grant.toString(), Toast.LENGTH_LONG).show()
                                if (grant != PackageManager.PERMISSION_GRANTED) {
                                    val permission_list = arrayOfNulls<String>(1)
                                    permission_list[0] = permission
                                    ActivityCompat.requestPermissions(
                                        context as Activity,
                                        permission_list,
                                        1
                                    )
                                }
                                val intent = Intent(Intent.ACTION_OPEN_DOCUMENT,
                                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI).apply {
                                    addCategory(Intent.CATEGORY_OPENABLE) }
                                launcher.launch(intent)
                            },
                        )
                        DropdownMenuItem(
                            text = { Text(text = "Remove", fontSize = 12.sp, color = MaterialTheme.colorScheme.error) },
                            onClick = {
                                mDisplayMenu = !mDisplayMenu
                                viewModel.removeItem(item)
                            },
                            modifier = Modifier.background(color = MaterialTheme.colorScheme.errorContainer)
                        )
                    }

                    Text(
                        text = "Show time:",
                        fontSize = 10.sp,
                        fontStyle = FontStyle.Italic
                    )
                    Text(
                        text = item.time.toString(),
                        fontSize = 14.sp,
                        modifier = Modifier.padding(bottom = 10.dp),
                        fontStyle = FontStyle.Italic
                    )
                    Text(
                        text = "Channel:",
                        fontSize = 10.sp,
                        fontStyle = FontStyle.Italic
                    )
                    Text(
                        text = item.channel.toString(),
                        fontSize = 14.sp,
                        modifier = Modifier.padding(bottom = 10.dp),
                        fontStyle = FontStyle.Italic
                    )
                    Text(
                        text = "Author:",
                        fontSize = 10.sp,
                        fontStyle = FontStyle.Italic
                    )
                    Text(
                        text = item.fio.toString(),
                        fontSize = 14.sp,
                        modifier = Modifier.padding(bottom = 10.dp),
                        fontStyle = FontStyle.Italic
                    )
                }
            }
        }
    }
    fun pictureIsInt(picture: String): Boolean{
        var data = try {
            picture.toInt()
        } catch (e:NumberFormatException){
            null
        }
        return data!=null
    }

    override fun onSaveInstanceState(outState: Bundle) {

//        Toast.makeText(this, "saved", Toast.LENGTH_SHORT).show()
        var tempTVArray = ArrayList<TV>()
        viewModel.TVListFlow.value.forEach {
            tempTVArray.add(it)
        }
        outState.putSerializable("TVs", tempTVArray)
        super.onSaveInstanceState(outState)
    }
}