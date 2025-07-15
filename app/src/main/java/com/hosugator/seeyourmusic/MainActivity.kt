package com.hosugator.seeyourmusic

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.hosugator.seeyourmusic.ui.theme.SeeYourMusicTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SeeYourMusicTheme {
                MainScreen()
            }
        }
    }
}

@Composable
fun MainScreen() {
    var inputText by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.padding(48.dp)
    ) {
        InputArea(
            inputText,
            onTextChange = { newText -> inputText = newText }
        )
        OutputArea(
            inputText,
            onTextChange = { newText -> inputText = newText }
        )
    }

}

//@Preview(showBackground = true)
//@Composable
//fun MainPreview() {
//    SeeYourMusicTheme {
//        MainScreen()
//    }
//}
