package com.hosugator.seeyourmusic

import android.util.Log
import java.util.Locale

private const val TAG_LANGUAGE_DISPLAY = "LanguageDisplayUtils"

// BCP 47 태그로부터 언어의 디스플레이 네임을 추출하는 함수
suspend fun getDisplayLanguageName(languageCode: String): String {

    // 입력값이 비어있거나 null일 경우에 대한 방어 코드 추가 (선택 사항이지만 권장)
    if (languageCode.isBlank()) {
        Log.w("LanguageDisplayUtils", "Input languageCode is blank.")
        return "Unknown Language" // 또는 적절한 기본값
    }

    try {
        // 예: "zh-Hant" -> ["zh", "Hant"]
        val parts = languageCode.split("-")

        // 언어 태그인 앞의 zh만 가져온다 ["zh"]
        val baseLanguageCode = parts[0]

        // 예외 처리: 언어 미감지
        if (baseLanguageCode == "und") {
            return "Unknown Language"
        }

        // 현재 기기의 기본 로케일 기준으로 언어 이름을 가져옴
        val locale = Locale(baseLanguageCode)

        // 변수에 언어의 풀 네임을 할당한다. 예: 한국어 기기에서 "en" -> "영어"
        var displayLanguageName = locale.displayLanguage

        // 풀 네임이 저장된 변수를 반환한다
        return displayLanguageName // 예: "en" -> "English", "ko" -> "Korean"

        // 참고: 특정 로케일 기준으로 언어 이름을 가져올 수도 있음 (예: 항상 영어로 언어 이름 표시)
        // displayLanguageName = locale.getDisplayLanguage(Locale.ENGLISH)
    } catch (e: Exception) {     // 오류 처리: Tag Logging 및 언어 코드 반환
        Log.e(TAG_LANGUAGE_DISPLAY, "Error getting display name for code: $languageCode", e)
        return languageCode
    }
}