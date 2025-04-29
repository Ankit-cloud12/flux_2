package com.rankerz.screenbrightness.core.data.repository

import android.util.Log
import com.rankerz.screenbrightness.core.domain.model.UserProfile
import com.rankerz.screenbrightness.core.domain.repository.ProfileRepository
import com.rankerz.screenbrightness.data.local.dao.ProfileDao // Import DAO
import com.rankerz.screenbrightness.data.local.entity.toDomainModel // Import mappers
import com.rankerz.screenbrightness.data.local.entity.toEntity // Import mappers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map // Import map
import kotlinx.coroutines.withContext // Import withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProfileRepositoryImpl @Inject constructor(
    private val profileDao: ProfileDao // Inject DAO
) : ProfileRepository {

    // Remove in-memory storage
    // private val profilesFlow = ...

    override fun getAllProfiles(): Flow<List<UserProfile>> {
        // Get flow from DAO and map entities to domain models
        return profileDao.getAllProfiles().map { entities ->
            entities.map { it.toDomainModel() }
        }
    }

    override suspend fun getProfileById(profileId: String): UserProfile? = withContext(Dispatchers.IO) {
        // Get from DAO and map entity to domain model
        profileDao.getProfileById(profileId)?.toDomainModel()
    }

    override suspend fun addProfile(profile: UserProfile): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            // TODO: Consider adding check for name uniqueness in DAO or here if needed
            profileDao.insertProfile(profile.toEntity())
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e("ProfileRepositoryImpl", "Error adding profile", e)
            Result.failure(e)
        }
    }

    override suspend fun updateProfile(profile: UserProfile): Result<Unit> = withContext(Dispatchers.IO) {
         try {
            // TODO: Consider adding check for name uniqueness in DAO or here if needed
            profileDao.updateProfile(profile.toEntity())
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e("ProfileRepositoryImpl", "Error updating profile", e)
            Result.failure(e)
        }
    }

    override suspend fun deleteProfile(profileId: String): Result<Unit> = withContext(Dispatchers.IO) {
         try {
            val deletedRows = profileDao.deleteProfileById(profileId)
            if (deletedRows > 0) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Profile with ID $profileId not found"))
            }
        } catch (e: Exception) {
            Log.e("ProfileRepositoryImpl", "Error deleting profile", e)
            Result.failure(e)
        }
    }
}