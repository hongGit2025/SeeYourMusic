//// In com.hosugator.seeyourmusic.stt.SttManager.kt
//package com.hosugator.seeyourmusic.stt
//
//import android.content.Context
//import android.content.Intent
//import android.os.Bundle
//import android.speech.RecognitionListener
//import android.speech.RecognizerIntent
//import android.speech.SpeechRecognizer
//import android.util.Log
//import kotlinx.coroutines.flow.MutableStateFlow
//import kotlinx.coroutines.flow.StateFlow
//import java.util.Locale
//
//class SttManager(private val context: Context) {
//
//    private var speechRecognizer: SpeechRecognizer? = null
//
//    private val _sttResult = MutableStateFlow<String?>(null)
//    val sttResult: StateFlow<String?> = _sttResult
//
//    private val _sttError = MutableStateFlow<String?>(null)
//    val sttError: StateFlow<String?> = _sttError
//
//    private val _isListening = MutableStateFlow(false)
//    val isListening: StateFlow<Boolean> = _isListening
//
//    init {
//        if (SpeechRecognizer.isRecognitionAvailable(context)) {
//            speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context)
//            speechRecognizer?.setRecognitionListener(object : RecognitionListener {
//                override fun onReadyForSpeech(params: Bundle?) {
//                    _isListening.value = true
//                    Log.d("SttManager", "Ready for speech")
//                }
//
//                override fun onBeginningOfSpeech() {
//                    Log.d("SttManager", "Beginning of speech")
//                }
//
//                override fun onRmsChanged(rmsdB: Float) {}
//                override fun onBufferReceived(buffer: ByteArray?) {}
//                override fun onEndOfSpeech() {
//                    _isListening.value = false
//                    Log.d("SttManager", "End of speech")
//                }
//
//                override fun onError(error: Int) {
//                    _isListening.value = false
//                    val errorMessage = getErrorText(error)
//                    _sttError.value = errorMessage
//                    _sttResult.value = null // 에러 시 결과는 null
//                    Log.e("SttManager", "Error: $errorMessage")
//                }
//
//                override fun onResults(results: Bundle?) {
//                    _isListening.value = false
//                    val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
//                    if (!matches.isNullOrEmpty()) {
//                        _sttResult.value = matches[0] // 가장 가능성 높은 결과
//                        _sttError.value = null
//                    } else {
//                        _sttResult.value = null
//                    }
//                }
//
//                override fun onPartialResults(partialResults: Bundle?) {}
//                override fun onEvent(eventType: Int, params: Bundle?) {}
//            })
//        } else {
//            _sttError.value = "Speech recognition not available on this device."
//            Log.e("SttManager", "Speech recognition not available")
//        }
//    }
//
//    fun startListening(language: String = Locale.getDefault().language) {
//        if (speechRecognizer == null && SpeechRecognizer.isRecognitionAvailable(context)) {
//            // init에서 실패했거나 destroy 후 다시 시도하는 경우 대비 (선택적)
//            // init() // 또는 speechRecognizer 재설정 로직
//        }
//
//        if (speechRecognizer != null && !_isListening.value) {
//            val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
//                putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
//                putExtra(RecognizerIntent.EXTRA_LANGUAGE, language) // 예: "ko-KR", "en-US"
//                putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, false) // 중간 결과 받을지 여부
//                // putExtra(RecognizerIntent.EXTRA_PROMPT, "Say something...") // 음성 입력 안내 메시지 (시스템 UI 사용 시)
//            }
//            try {
//                speechRecognizer?.startListening(intent)
//            } catch (e: SecurityException) {
//                _sttError.value = "Microphone permission not granted."
//                Log.e("SttManager", "SecurityException on startListening: ${e.message}")
//            }
//        } else if (speechRecognizer == null) {
//            _sttError.value = "Speech recognizer not initialized or not available."
//        }
//    }
//
//    fun stopListening() {
//        speechRecognizer?.stopListening()
//        _isListening.value = false
//    }
//
//    fun destroy() {
//        speechRecognizer?.destroy()
//        speechRecognizer = null
//    }
//
//    private fun getErrorText(errorCode: Int): String {
//        return when (errorCode) {
//            SpeechRecognizer.ERROR_AUDIO -> "Audio recording error"
//            SpeechRecognizer.ERROR_CLIENT -> "Client side error"
//            SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS -> "Insufficient permissions (Microphone?)"
//            SpeechRecognizer.ERROR_NETWORK -> "Network error"
//            SpeechRecognizer.ERROR_NETWORK_TIMEOUT -> "Network timeout"
//            SpeechRecognizer.ERROR_NO_MATCH -> "No speech match"
//            SpeechRecognizer.ERROR_RECOGNIZER_BUSY -> "RecognitionService busy"
//            SpeechRecognizer.ERROR_SERVER -> "error from server"
//            SpeechRecognizer.ERROR_SPEECH_TIMEOUT -> "No speech input"
//            else -> "Didn't understand, please try again."
//        }
//    }
//}
