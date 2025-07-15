package com.hosugator.seeyourmusic

import android.util.Log
import com.google.mlkit.common.MlKitException
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.Translator
import com.google.mlkit.nl.translate.TranslatorOptions
import kotlinx.coroutines.tasks.await

private const val TAG_LANGUAGE_TRANSLATE = "LanguageTranslateUtils"

// 감지된 언어를 원하는 언어로 번역하는 함수
suspend fun identifyAndTranslateLanguage(
    inputText: String,
    targetLanguage: String,
): String? {
    // 예외처리: 빈 입력값일 때 함수 종료
    if (inputText.isBlank()) {
        Log.w(TAG_LANGUAGE_TRANSLATE, "Input text is blank for identification and translation.")
        return "Input text is black."
    }

    // 언어 감지
    var identifiedLanguage: String
    try {
        identifiedLanguage = identifyLanguage(inputText) // 이전 대화에서 만든 suspend 버전 사용 가정

        if (identifiedLanguage == "und" || identifiedLanguage.startsWith("Unknown") || identifiedLanguage.startsWith(
                "Error"
            )
        ) {
            Log.w(
                TAG_LANGUAGE_TRANSLATE,
                "Source language not identified or error: $identifiedLanguage. Cannot translate."
            )
            return "Could not identify source language: $identifiedLanguage"
        }
        Log.i(TAG_LANGUAGE_TRANSLATE, "Language identified for translation: $identifiedLanguage")
    } catch (e: Exception) {
        Log.e(TAG_LANGUAGE_TRANSLATE, "Error during language identification step", e)
        return "Error in language identification: ${e.message}"
    }

    var translator: Translator? = null
    try {// 원본 언어와 목적 언어 설정
        val options = TranslatorOptions.Builder()
            .setSourceLanguage(identifiedLanguage)
            .setTargetLanguage(targetLanguage)
            .build()

        translator = Translation.getClient(options)

        // 언어 모델 다운로드 인스턴스 생성
        val conditions = DownloadConditions.Builder().build()

        Log.d(
            TAG_LANGUAGE_TRANSLATE,
            "Downloading translation model if needed for $identifiedLanguage -> $targetLanguage..."
        )
        translator.downloadModelIfNeeded(conditions).await() // await()로 모델 다운로드 완료까지 대기
        Log.d(
            TAG_LANGUAGE_TRANSLATE,
            "Translation model ready for $identifiedLanguage -> $targetLanguage."
        )

        // 텍스트 번역
        Log.d(TAG_LANGUAGE_TRANSLATE, "Translating text: \"$inputText\"")
        val translatedText = translator.translate(inputText).await() // await()로 번역 완료까지 대기
        Log.i(TAG_LANGUAGE_TRANSLATE, "Text translated to $targetLanguage: \"$translatedText\"")

        // 성공적으로 번역된 텍스트 반환
        return translatedText
    } catch (e: Exception) {
        Log.e(
            TAG_LANGUAGE_TRANSLATE,
            "Translation failed for \"$inputText\" ($identifiedLanguage -> $targetLanguage)",
            e
        )
        return "Translation error: ${e.message}"
    } finally {
        // Translator 리소스 해제
        translator?.close()
        Log.d(
            TAG_LANGUAGE_TRANSLATE,
            "Translator closed for $identifiedLanguage -> $targetLanguage task."
        )
    }
}
