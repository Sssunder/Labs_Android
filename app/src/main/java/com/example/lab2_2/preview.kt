package com.example.lab2_2

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview




@Preview
@Composable
fun MainActivityPreview() {
    val viewModel = ItemViewModel()
    MainActivityUI(viewModel)
}
