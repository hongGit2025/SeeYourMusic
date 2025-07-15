package com.hosugator.seeyourmusic

import android.util.Log
import com.google.mlkit.nl.languageid.LanguageIdentification
import kotlinx.coroutines.tasks.await

private const val TAG_LANGUAGE_IDENTIFY = "LanguageIdentifyUtils"

// 코루틴 방식의 비동기 작업
suspend fun identifyLanguage(inputText: String): String {
    // 예외처리: 빈 입력값일 때 함수 종료
    if (inputText.isBlank()) {
        Log.w(TAG_LANGUAGE_IDENTIFY, "Input text is blank for identification.")
        // 또는 throw IllegalArgumentException("Input text cannot be blank")
        return "und (blank)"
    }

    // LanguageIdentification 인스턴스 생성
    val languageIdentifier = LanguageIdentification.getClient()

    try {
        // await()를 통해, identifyLanguage 작업이 완료될 때까지 기다리고 결과를 받음
        val identifiedLanguageCode = languageIdentifier.identifyLanguage(inputText).await()

        // 예외 처리: 언어 미감지
        if (identifiedLanguageCode == "und") {
            Log.i(TAG_LANGUAGE_IDENTIFY, "언어를 식별하기 어렵습니다.")
            return "und" // 또는 예외를 던지거나, null을 반환하는 등 정책에 맞게 처리
        }

        Log.i(TAG_LANGUAGE_IDENTIFY, "언어 식별 완료: $identifiedLanguageCode")
        return identifiedLanguageCode // 식별된 언어 코드 반환

    } catch (exception: Exception) {
        Log.e(TAG_LANGUAGE_IDENTIFY, "언어 식별 실패", exception)
        // 실패 시 적절한 값 반환 또는 예외 다시 던지기
        // 예: return "" 또는 throw exception
        throw exception // 호출한 쪽에서 try-catch로 잡을 수 있도록 예외를 다시 던지는 것이 좋음
    }
}