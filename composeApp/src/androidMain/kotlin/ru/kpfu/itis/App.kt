package ru.kpfu.itis

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import ru.kpfu.itis.navigation.AppNavGraph

@Composable
@Preview
fun App() {
    MaterialTheme {
        AppNavGraph()
    }
}
