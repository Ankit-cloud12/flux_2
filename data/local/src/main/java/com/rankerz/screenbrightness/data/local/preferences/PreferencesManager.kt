package com.rankerz.screenbrightness.data.local.preferences

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

// TODO: Define actual preference keys and implement specific get/set methods
@Singleton
class PreferencesManager @Inject constructor(@ApplicationContext context: Context) {

    // Define the DataStore instance
    private val Context.dataStore by preferencesDataStore(name = "rankerz_settings")
    private val dataStore = context.dataStore

    // Example generic preference flow
    fun <T> getPreferenceFlow(key: Preferences.Key<T>, defaultValue: T): Flow<T> {
        return dataStore.data
            .catch { exception ->
                // dataStore.data throws an IOException when an error is encountered when reading data
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }
            .map { preferences ->
                preferences[key] ?: defaultValue
            }
    }

    // Example generic preference update function
    suspend fun <T> updatePreference(key: Preferences.Key<T>, value: T) {
        dataStore.edit { preferences ->
            preferences[key] = value
        }
    }

    // Define specific preference keys here (example)
    // object PreferencesKeys {
    //     val OVERLAY_BRIGHTNESS = floatPreferencesKey("overlay_brightness")
    //     val COLOR_TEMPERATURE = intPreferencesKey("color_temperature")
    // }

    // Implement specific get/set methods using the keys
    // fun getOverlayBrightnessFlow(): Flow<Float> = getPreferenceFlow(PreferencesKeys.OVERLAY_BRIGHTNESS, 0.5f)
    // suspend fun setOverlayBrightness(brightness: Float) = updatePreference(PreferencesKeys.OVERLAY_BRIGHTNESS, brightness)
}