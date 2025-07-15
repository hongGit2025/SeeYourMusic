//package com.hosugator.seeyourmusic.translate
//
//import android.util.Log
//import com.google.mlkit.common.model.DownloadConditions
//import com.google.mlkit.nl.languageid.LanguageIdentification
//import com.google.mlkit.nl.languageid.LanguageIdentificationOptions
//import com.google.mlkit.nl.translate.TranslateLanguage
//import com.google.mlkit.nl.translate.Translation
//import com.google.mlkit.nl.translate.Translator
//import com.google.mlkit.nl.translate.TranslatorOptions
//import java.util.Locale
//
//
//private const val TAG_LANGUAGE_IDENTIFY = "LanguageIdentifyUtils"
//private const val TAG_LANGUAGE_DISPLAY = "LanguageDisplayUtils"
//private const val TAG_LANGUAGE_TRANSLATE = "LanguageTranslateUtils"
//
//class TranslateManager {
//
//    // 클래스 변수
//    var targetLanguage: String = TranslateLanguage.KOREAN // 번역 목표 언어 설정
//    var isLoading: Boolean = false
//    var statusMessage: String = ""
//    var identifiedCodeWithScript = ""
//
//    // 입력된 텍스트의 언어를 감지하는 함수
//    fun identifyLanguage(textToIdentify: String) {
//
//        // 예외 처리: 입력값이 비었을 때, 상태 메시지를 띄우고 함수 종료
//        if (textToIdentify.isBlank()) {
//            statusMessage = "텍스트를 입력하세요."
//            return
//        }
//
//        // 상태 표시: 대기값 참 및 상태 메시지
//        isLoading = true
//        statusMessage = "언어 식별 중..."
//
//        // 감지 조건: 0.5 이상 신뢰도인 언어만 식별
//        val langaugeOptions = LanguageIdentificationOptions.Builder()
//            .setConfidenceThreshold(0.5f)
//            .build()
//
//        // LanguageIdentification 인스턴스 생성
//        val languageIdentifier = LanguageIdentification.getClient(langaugeOptions)
//
//        // 생성된 languageIdentifier 인스턴스로 텍스트 식별
//        languageIdentifier.identifyLanguage(textToIdentify)
//            // 성공 행동 정의
//            .addOnSuccessListener { identifiedLanguageCode ->
//                // 예외 처리: 언어 미감지
//                if (identifiedLanguageCode == "und") {
//                    Log.i(TAG_LANGUAGE_IDENTIFY, "언어를 식별하기 어렵습니다.")
//                    statusMessage = "언어를 식별하기 어렵습니다"
//                    return@addOnSuccessListener
//                }
//                identifiedCodeWithScript =
//                    getDisplayLanguageNameFromLocale(identifiedLanguageCode)
//                Log.i(TAG_LANGUAGE_IDENTIFY, "언어 식별 완료: $identifiedCodeWithScript")
//                statusMessage =
//                    "언어 식별 완료 ${getDisplayLanguageNameFromLocale(identifiedCodeWithScript)}"
//                return@addOnSuccessListener
//            }
//            // 실패 행동 정의
//            .addOnFailureListener { exception ->
//                isLoading = false
//                statusMessage = "언어 식별 오류: ${exception.message}"
//                Log.e(TAG_LANGUAGE_IDENTIFY, "언어 식별 실패", exception)
//            }
//    }
//
//    // BCP 47 태그로부터 언어의 디스플레이 네임을 추출하는 함수
//    fun getDisplayLanguageNameFromLocale(languageCode: String): String {
//
//        // 입력값이 비어있거나 null일 경우에 대한 방어 코드 추가 (선택 사항이지만 권장)
//        if (languageCode.isBlank()) {
//            Log.w("LanguageDisplayUtils", "Input languageCode is blank.")
//            return "Unknown Language" // 또는 적절한 기본값
//        }
//
//        return try {
//            // 예: "zh-Hant" -> ["zh", "Hant"]
//            val parts = languageCode.split("-")
//
//            // 언어 태그인 앞의 zh만 가져온다 ["zh"]
//            val baseLanguageCode = parts[0]
//
//            // 예외 처리: 언어 미감지
//            if (baseLanguageCode == "und") {
//                return "Unknown Language"
//            }
//
//            // 현재 기기의 기본 로케일 기준으로 언어 이름을 가져옴
//            val locale = Locale(baseLanguageCode)
//
//            // 변수에 언어의 풀 네임을 할당한다. 예: 한국어 기기에서 "en" -> "영어"
//            var displayLanguageName = locale.displayLanguage
//
//            // 풀 네임이 저장된 변수를 반환한다
//            return displayLanguageName // 예: "en" -> "English", "ko" -> "Korean"
//
//            // 참고: 특정 로케일 기준으로 언어 이름을 가져올 수도 있음 (예: 항상 영어로 언어 이름 표시)
//            // displayLanguageName = locale.getDisplayLanguage(Locale.ENGLISH)
//        } catch (e: Exception) {     // 오류 처리: Tag Logging 및 언어 코드 반환
//            Log.e(TAG_LANGUAGE_DISPLAY, "Error getting display name for code: $languageCode", e)
//            return languageCode
//        }
//    }
//
//    // 감지된 언어를 원하는 언어로 번역하는 함수
//    fun translateLangauge(textToTranslate: String) {
//        val options = TranslatorOptions.Builder()
//            .setSourceLanguage(identifiedCodeWithScript)
//            .setTargetLanguage(targetLanguage)
//            .build()
//        val translatorForThisTask = Translation.getClient(options)
//        statusMessage = "번역 모델 준비 중..."
//        val conditions = DownloadConditions.Builder().build()
//        translatorForThisTask.downloadModelIfNeeded(conditions)
//            .addOnSuccessListener {
//                Log.d(TAG_LANGUAGE_TRANSLATE, "번역 모델 준비 완료: $identifiedCodeWithScript")
//                statusMessage = "번역 모델 준비 완료. 번역 중..."
//                translatorForThisTask.translate(textToTranslate)
//                    .addOnSuccessListener { translatedText ->
//                        isLoading = false
//                        statusMessage = "번역 성공"
//                        translatorForThisTask.close()
//                        return translatedText
//                    }
//                    .addOnFailureListener { exception ->
//                        isLoading = false
//                        Log.e(TAG_LANGUAGE_TRANSLATE, "번역 실패", exception)
//                        statusMessage = "번역 오류: ${exception.message}"
//                        translatorForThisTask.close()
//                    }
//            }
//            .addOnFailureListener { exception ->
//                isLoading = false
//                statusMessage =
//                    "번역 모델 다운로드 실패 ($identifiedCodeWithScript): ${exception.message}"
//                Log.e(TAG_LANGUAGE_TRANSLATE, "모델 다운로드 실패", exception)
//                translatorForThisTask.close()
//            }
//    }
//}
//
//// 강사님: addOn은 비동기 방식이기에 함수 종료 여부와 관계 없이 값 전달한다.
//// 아래와 같이 성공/실패에 대해 값 전달을 함수 자체로 반환하는 방식 적용해야 한다.
////class TranslateManager {
////    fun translate(
////        myTranslator: Translator,
////        source: String,
////        onSuccess: (String) -> Unit,
////        onFail: (String) -> Unit,
////    ): String {
////        myTranslator.translate(source)
////            .addOnSuccessListener { translatedText ->
////                translatedLyrics = translatedText
////                isLoading = false
////                statusMessage = "번역 성공!"
////                Log.d(TAG_MAINSCEEN, "번역 성공: $translatedText")
////            }
////            .addOnFailureListener { exception ->
////                translatedLyrics = ""
////                isLoading = false
////                statusMessage = "번역 오류: ${exception.message}"
////                Log.e(TAG_MAINSCEEN, "번역 오류", exception)
////            }
////        return ""
////    }
////}