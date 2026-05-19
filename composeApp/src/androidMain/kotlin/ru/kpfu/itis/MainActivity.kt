package ru.kpfu.itis

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import ru.kpfu.itis.designSystem.Theme.CaseCareerTheme
import ru.kpfu.itis.feature.auth.data.datasource.UserDataSource
import ru.kpfu.itis.navigation.AppNavGraph

class MainActivity : ComponentActivity() {

    private val dbSource: UserDataSource by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        Log.i("TAG1", "мы в он креат")

        /*lifecycleScope.launch {
            Log.i("TAG2", "мы в лайфсайкл скоуп")
            dbSource.addUser()
            val user = dbSource.getUser()

            print(user.getOrNull(0))
        }*/


        setContent {
            setContent {
                CaseCareerTheme {
                    AppNavGraph()
                }
            }
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}