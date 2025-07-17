package com.hosugator.seeyourmusic

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import java.util.*

private const val TAG_INPUTAREA = "InputAreaUtils"

@Composable
fun SpeakToText(
    inputText: String,
    onTextChange: (String) -> Unit,
) {
    var identifiedLanguageResult by remember { mutableStateOf("언어 감지 전") }
    val context = LocalContext.current
    // 코루틴 관리할 변수 생성
    var coroutineScope = rememberCoroutineScope()

    // Request microphone permission
    val requestPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted: Boolean ->
            if (isGranted) {
                // Permission granted
            } else {
                // Permission denied
            }
        }
    )

    LaunchedEffect(Unit) {
        requestPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
    }

    val recognizerIntent = remember {
        Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
            )
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
        }
    }

    val speechResultLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val matches = result.data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                matches?.let {
                    onTextChange(it[0])
                }
            }
        }

    Column(
        modifier = Modifier
            .padding(16.dp),
    ) {
        Button(
            onClick = {
                if (SpeechRecognizer.isRecognitionAvailable(context)) { // 추가
                    speechResultLauncher.launch(recognizerIntent)
                } else {
                    // 사용자에게 음성 인식을 사용할 수 없다고 알림
                    // (예: Toast, Snackbar, 또는 상태 변수 변경으로 UI에 메시지 표시)
                    Log.w(TAG_INPUTAREA, "Speech recognition not available.")
                }
            }
        ) {
            Text("음성 인식")
        }
        TextField(
            value = inputText,
            onValueChange = { newText ->
                onTextChange(newText)
            },
        )
        Row {// 버튼: 감지 기능 실행
            Button(
                onClick = {
                    // 코루틴 시작
                    coroutineScope.launch {
                        try {
                            var result = identifyLanguage(inputText)
                            result = getDisplayLanguageName(result)
                            identifiedLanguageResult = "감지된 언어: $result"
                            Log.i(TAG_INPUTAREA, "언어가 감지되었습니다.")
                        } catch (e: Exception) {
                            identifiedLanguageResult = "오류: ${e.message}"
                            Log.e(TAG_INPUTAREA, "에러: 언어 감지 실패")
                        }
                    }
                },
            ) {
                Text(text = "언어 감지")
            }

            Text(text = identifiedLanguageResult)
        }

    }
}
