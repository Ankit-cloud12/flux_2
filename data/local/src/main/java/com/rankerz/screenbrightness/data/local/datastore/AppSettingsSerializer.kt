package com.rankerz.screenbrightness.data.local.datastore

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.rankerz.screenbrightness.core.domain.model.AppSettings // Assuming AppSettings exists
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream

// TODO: Implement actual serialization/deserialization logic using kotlinx.serialization
object AppSettingsSerializer : Serializer<AppSettings> {

    override val defaultValue: AppSettings = AppSettings() // Provide a default instance

    override suspend fun readFrom(input: InputStream): AppSettings {
        try {
            val jsonString = input.bufferedReader().use { it.readText() }
            return Json.decodeFromString(AppSettings.serializer(), jsonString)
        } catch (exception: SerializationException) {
            throw CorruptionException("Cannot read proto.", exception)
        }
    }

    override suspend fun writeTo(t: AppSettings, output: OutputStream) {
        withContext(Dispatchers.IO) {
            output.write(
                Json.encodeToString(AppSettings.serializer(), t).encodeToByteArray()
            )
        }
    }
}