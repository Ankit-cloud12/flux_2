package com.rankerz.screenbrightness.feature.profiles.domain.usecase

import com.rankerz.screenbrightness.feature.brightness.domain.usecase.SetBrightnessUseCase
import com.rankerz.screenbrightness.feature.temperature.domain.usecase.SetColorTemperatureUseCase
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import com.rankerz.screenbrightness.core.domain.model.UserProfile
import com.rankerz.screenbrightness.core.domain.repository.ProfileRepository
import javax.inject.Inject

/**
 * Use case to apply the settings from a specific user profile.
 * It sets both brightness and color temperature.
 */
class ApplyProfileUseCase @Inject constructor(
    private val profileRepository: ProfileRepository, // Assuming this interface exists
    private val setBrightnessUseCase: SetBrightnessUseCase,
    private val setColorTemperatureUseCase: SetColorTemperatureUseCase
) {
    suspend operator fun invoke(profileId: String): Result<Unit> {
        // Fetch the actual profile using the repository
        val profile = profileRepository.getProfileById(profileId)
            ?: return Result.failure(IllegalArgumentException("Profile with ID $profileId not found"))

        return try {
            // Apply settings concurrently
            coroutineScope {
                launch { setBrightnessUseCase(profile.brightnessLevel) }
                launch { setColorTemperatureUseCase(profile.colorTemperatureKelvin) }
            }
            // Consider how to handle partial failures (e.g., brightness sets, temp fails)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
        // Alternatively, use the repository method directly if it handles applying
        // return profileRepository.applyProfile(profileId)
    }

     // Overload to apply directly from a UserProfile object
     suspend operator fun invoke(profile: UserProfile): Result<Unit> {
         // println("Stub: Applying profile: ${profile.name}") // Placeholder removed
         return try {
             coroutineScope {
                 launch { setBrightnessUseCase(profile.brightnessLevel) }
                 launch { setColorTemperatureUseCase(profile.colorTemperatureKelvin) }
             }
             Result.success(Unit)
         } catch (e: Exception) {
             Result.failure(e)
         }
     }
}