package com.hosugator.seeyourmusic

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import mySTT

private const val TAG_INPUTAREA = "InputAreaUtils"

// 텍스트 입력하는 기능을 포함한 입력창
@Composable
fun InputArea(
    inputText: String,
    onTextChange: (String) -> Unit,
) {
    // 결과를 저장할 변수 생성

    var identifiedLanguageResult by remember { mutableStateOf("언어 감지 전") }

    // 코루틴 관리할 변수 생성
    var coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier.padding(48.dp)
    ) {
        Text(text = "See Your Music")

        // Compose 추가: STT
        mySTT()

        // 텍스트 필드: 감지/번역할 텍스트 입력창 생성
        TextField(
            value = inputText,
            onValueChange = { onTextChange(it) },
            label = { Text("텍스트 입력") },
        )
        // 버튼: 감지 기능 실행
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

