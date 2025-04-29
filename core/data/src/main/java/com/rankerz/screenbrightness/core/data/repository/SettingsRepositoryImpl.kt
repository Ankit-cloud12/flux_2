package com.rankerz.screenbrightness.core.data.repository

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.rankerz.screenbrightness.core.common.util.Constants
import com.rankerz.screenbrightness.core.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SettingsRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : SettingsRepository {

    private val TAG = "SettingsRepositoryImpl"

    override fun getStartOnBootEnabled(): Flow<Boolean> {
        return dataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    Log.e(TAG, "Error reading startOnBoot preference.", exception)
                    emit(false) // Default value on error
                } else {
                    throw exception
                }
            }
            .map { preferences ->
                preferences[Constants.START_ON_BOOT_ENABLED] ?: false // Default to false
            }
    }

    override suspend fun setStartOnBootEnabled(enabled: Boolean): Result<Unit> {
        return try {
            dataStore.edit { preferences ->
                preferences[Constants.START_ON_BOOT_ENABLED] = enabled
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e(TAG, "Error writing startOnBoot preference.", e)
            Result.failure(e)
        }
    }

    override fun getAppTheme(): Flow<String> {
        return dataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    Log.e(TAG, "Error reading appTheme preference.", exception)
                    emit("System") // Default value on error
                } else {
                    throw exception
                }
            }
            .map { preferences ->
                preferences[Constants.APP_THEME] ?: "System" // Default to "System"
            }
    }

    override suspend fun setAppTheme(theme: String): Result<Unit> {
         // Basic validation
         if (theme !in listOf("System", "Light", "Dark")) {
             return Result.failure(IllegalArgumentException("Invalid theme value: $theme"))
         }
         return try {
            dataStore.edit { preferences ->
                preferences[Constants.APP_THEME] = theme
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e(TAG, "Error writing appTheme preference.", e)
            Result.failure(e)
        }
    }
}