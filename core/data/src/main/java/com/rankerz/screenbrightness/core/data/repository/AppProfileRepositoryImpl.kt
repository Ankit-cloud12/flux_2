package com.rankerz.screenbrightness.core.data.repository

import android.util.Log
import androidx.datastore.core.DataStore // Import DataStore
import androidx.datastore.preferences.core.Preferences // Import Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey // Import key type
import androidx.datastore.preferences.core.edit // Import edit
import com.rankerz.screenbrightness.core.domain.model.AppProfile
import com.rankerz.screenbrightness.core.domain.repository.AppProfileRepository
import com.rankerz.screenbrightness.data.local.dao.AppProfileDao // Import DAO
import com.rankerz.screenbrightness.data.local.entity.toDomainModel // Import mappers
import com.rankerz.screenbrightness.data.local.entity.toEntity // Import mappers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.io.IOException // For DataStore catch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppProfileRepositoryImpl @Inject constructor(
    private val appProfileDao: AppProfileDao,
    // TODO: Provide DataStore via Hilt module (e.g., in data:local or app module)
    private val dataStore: DataStore<Preferences>
) : AppProfileRepository {

    // Define DataStore key
    private object PreferencesKeys {
        val PER_APP_ENABLED = booleanPreferencesKey("per_app_brightness_enabled")
    }

    override fun getAllAppProfiles(): Flow<List<AppProfile>> {
        return appProfileDao.getAllAppProfiles().map { entities ->
            entities.map { it.toDomainModel() }
        }
    }

    override fun getAppProfile(packageName: String): Flow<AppProfile?> { // Changed to non-suspend returning Flow
        return appProfileDao.getAppProfile(packageName).map { it?.toDomainModel() }
    }

    override suspend fun addOrUpdateAppProfile(appProfile: AppProfile): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            appProfileDao.insertOrUpdateAppProfile(appProfile.toEntity())
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e("AppProfileRepository", "Error adding/updating app profile", e)
            Result.failure(e)
        }
    }

    override suspend fun deleteAppProfile(packageName: String): Result<Unit> = withContext(Dispatchers.IO) {
         try {
            val deletedRows = appProfileDao.deleteAppProfile(packageName)
            if (deletedRows > 0) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("App profile for $packageName not found"))
            }
        } catch (e: Exception) {
            Log.e("AppProfileRepository", "Error deleting app profile", e)
            Result.failure(e)
        }
    }

    override fun getPerAppBrightnessEnabled(): Flow<Boolean> {
         return dataStore.data
            .catch { exception ->
                // dataStore.data throws an IOException when an error is encountered when reading data
                if (exception is IOException) {
                    Log.e("AppProfileRepository", "Error reading per-app enabled pref.", exception)
                    emit(false) // Default value on error
                } else {
                    throw exception
                }
            }
            .map { preferences ->
                preferences[PreferencesKeys.PER_APP_ENABLED] ?: false // Default to false if not set
            }
    }

    override suspend fun setPerAppBrightnessEnabled(enabled: Boolean): Result<Unit> {
        return try {
            dataStore.edit { preferences ->
                preferences[PreferencesKeys.PER_APP_ENABLED] = enabled
            }
            Result.success(Unit)
        } catch (e: Exception) {
             Log.e("AppProfileRepository", "Error writing per-app enabled pref.", e)
             Result.failure(e)
        }
    }
}