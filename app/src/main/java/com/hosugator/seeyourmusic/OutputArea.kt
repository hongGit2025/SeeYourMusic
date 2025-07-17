package com.hosugator.seeyourmusic

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import com.google.mlkit.nl.translate.TranslateLanguage
import kotlinx.coroutines.launch

private const val TAG_OUTPUTAREA = "OutputAreaUtils"

@Composable
fun OutputArea(
    inputText: String,
    onTextChange: (String) -> Unit,
) {
    // 결과를 저장할 변수 생성
    var translatedLanguageStatus by remember { mutableStateOf("텍스트 번역 전") }
    var translatedText by remember { mutableStateOf("") }
    var targetLanguage by remember { mutableStateOf("KOREAN") }

    // 코루틴 관리할 변수 생성
    var coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier.padding(48.dp)
    ) {
        Row {
            // 버튼: 감지 기능 실행
            Button(
                onClick = {
                    // 코루틴 시작
                    val finalTargetLanguage = targetLanguage.substring(0..1).lowercase()
                    coroutineScope.launch {
                        try {
                            translatedLanguageStatus = "번역 중"
                            val result = identifyAndTranslateLanguage(
                                inputText,
                                targetLanguage = finalTargetLanguage
                            )
                            if (result != null) {
                                translatedText = result
                            }
                            Log.i(TAG_OUTPUTAREA, "텍스트가 번역되었습니다.")
                            translatedLanguageStatus = "텍스트 번역 완료"
                        } catch (e: Exception) {
                            translatedLanguageStatus = "오류: ${e.message}"
                            Log.e(TAG_OUTPUTAREA, "에러: 텍스트 번역 실패")
                        }
                    }
                },
            ) {
                Text(text = translatedLanguageStatus)
            }

            TextField(
                value = targetLanguage,
                onValueChange = { targetLanguage = it },
                label = { Text(text = "원하는 언어 입력") }
            )
        }
        Text(text = translatedText)
    }
}