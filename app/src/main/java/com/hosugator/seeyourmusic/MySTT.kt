//import android.Manifest
//import android.annotation.SuppressLint
//import android.app.Activity
//import android.content.Intent
//import android.os.Bundle
//import android.speech.RecognizerIntent
//import android.speech.SpeechRecognizer
//import android.util.Log
//import androidx.activity.ComponentActivity
//import androidx.activity.compose.rememberLauncherForActivityResult
//import androidx.activity.result.contract.ActivityResultContracts
//import androidx.compose.foundation.layout.*
//import androidx.compose.material.*
//import androidx.compose.material3.Button
//import androidx.compose.material3.Text
//import androidx.compose.material3.TextField
//import androidx.compose.runtime.*
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.unit.dp
//import java.util.*
//
//private const val TAG_STT = "SttUtils"
//
//@Composable
//fun mySTT(): String {
//    var message by remember { mutableStateOf("") }
//    val context = LocalContext.current
//
//    // Request microphone permission
//    val requestPermissionLauncher = rememberLauncherForActivityResult(
//        contract = ActivityResultContracts.RequestPermission(),
//        onResult = { isGranted: Boolean ->
//            if (isGranted) {
//                // Permission granted
//                Log.i(TAG_STT, "음성 권한 허가됨")
//            } else {
//                // Permission denied
//                Log.i(TAG_STT, "음성 권한 거부됨")
//            }
//        }
//    )
//
//    LaunchedEffect(Unit) {
//        requestPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
//    }
//
//    val recognizerIntent = remember {
//        Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
//            putExtra(
//                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
//                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
//            )
//            putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
//        }
//    }
//
//    val speechResultLauncher =
//        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
//            if (result.resultCode == Activity.RESULT_OK) {
//                val matches = result.data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
//                matches?.let {
//                    message = it[0]
//                }
//            }
//        }
//    speechResultLauncher.launch(recognizerIntent)
//    return message
//}