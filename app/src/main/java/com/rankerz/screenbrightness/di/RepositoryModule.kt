package com.rankerz.screenbrightness.di

import android.content.Context
import com.rankerz.screenbrightness.core.data.manager.SystemPermissionsManagerImpl
import com.rankerz.screenbrightness.core.data.repository.AppProfileRepositoryImpl
import com.rankerz.screenbrightness.core.data.repository.BrightnessRepositoryImpl
import com.rankerz.screenbrightness.core.data.repository.ColorTemperatureRepositoryImpl
import com.rankerz.screenbrightness.core.data.repository.ProfileRepositoryImpl
import com.rankerz.screenbrightness.core.data.repository.SchedulingRepositoryImpl
import com.rankerz.screenbrightness.core.data.repository.SettingsRepositoryImpl // Import Settings Impl
import com.rankerz.screenbrightness.core.data.service.OverlayServiceImpl
import com.rankerz.screenbrightness.core.domain.manager.SystemPermissionsManager
import com.rankerz.screenbrightness.core.domain.repository.AppProfileRepository
import com.rankerz.screenbrightness.core.domain.repository.BrightnessRepository
import com.rankerz.screenbrightness.core.domain.repository.ColorTemperatureRepository
import com.rankerz.screenbrightness.core.domain.repository.ProfileRepository
import com.rankerz.screenbrightness.core.domain.repository.SchedulingRepository
import com.rankerz.screenbrightness.core.domain.repository.SettingsRepository // Import Settings Interface
import com.rankerz.screenbrightness.core.domain.service.OverlayService
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindBrightnessRepository(
        brightnessRepositoryImpl: BrightnessRepositoryImpl
    ): BrightnessRepository

    @Binds
    @Singleton
    abstract fun bindColorTemperatureRepository(
        colorTemperatureRepositoryImpl: ColorTemperatureRepositoryImpl
    ): ColorTemperatureRepository

    @Binds
    @Singleton
    abstract fun bindSchedulingRepository(
        schedulingRepositoryImpl: SchedulingRepositoryImpl
    ): SchedulingRepository

    @Binds
    @Singleton
    abstract fun bindAppProfileRepository(
        appProfileRepositoryImpl: AppProfileRepositoryImpl
    ): AppProfileRepository

    @Binds
    @Singleton
    abstract fun bindSystemPermissionsManager(
        systemPermissionsManagerImpl: SystemPermissionsManagerImpl
    ): SystemPermissionsManager

    @Binds
    @Singleton
    abstract fun bindOverlayService(
        overlayServiceImpl: OverlayServiceImpl
    ): OverlayService

    @Binds
    @Singleton
    abstract fun bindProfileRepository(
        profileRepositoryImpl: ProfileRepositoryImpl
    ): ProfileRepository

    @Binds
    @Singleton
    abstract fun bindSettingsRepository(
        settingsRepositoryImpl: SettingsRepositoryImpl
    ): SettingsRepository

    companion object {
        @Provides
        @Singleton
        fun provideContext(@ApplicationContext context: Context): Context {
            return context
        }
    }
}