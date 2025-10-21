package com.bassam.livesubs.translate

import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.TranslatorOptions
import kotlinx.coroutines.tasks.await

object Translator {
    suspend fun toArabic(text: String): String {
        val options = TranslatorOptions.Builder()
            .setSourceLanguage(TranslateLanguage.ENGLISH)
            .setTargetLanguage(TranslateLanguage.ARABIC)
            .build()
        val client = Translation.getClient(options)
        client.downloadModelIfNeeded(DownloadConditions.Builder().requireWifi().build()).await()
        val result = client.translate(text).await()
        client.close()
        return result
    }
}
