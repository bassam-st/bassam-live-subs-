package com.bassam.livesubs.stt

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import java.util.*

class SttManager(private val ctx: Context, private val onText: (String) -> Unit) {
    private var recognizer: SpeechRecognizer? = null

    fun start() {
        if (!SpeechRecognizer.isRecognitionAvailable(ctx)) return
        recognizer = SpeechRecognizer.createSpeechRecognizer(ctx)
        recognizer?.setRecognitionListener(listener)
        startListening()
    }

    private fun startListening() {
        val i = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
            putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true)
        }
        recognizer?.startListening(i)
    }

    fun stop() { recognizer?.destroy(); recognizer = null }

    private val listener = object : RecognitionListener {
        override fun onPartialResults(b: Bundle) {
            val res = b.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
            if (!res.isNullOrEmpty()) onText(res[0])
        }

        override fun onResults(b: Bundle) {
            val res = b.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
            if (!res.isNullOrEmpty()) onText(res[0])
            startListening()
        }

        override fun onError(error: Int) { startListening() }
        override fun onReadyForSpeech(p0: Bundle?) {}
        override fun onRmsChanged(p0: Float) {}
        override fun onBufferReceived(p0: ByteArray?) {}
        override fun onEndOfSpeech() {}
        override fun onEvent(p0: Int, p1: Bundle?) {}
        override fun onBeginningOfSpeech() {}
    }
}
