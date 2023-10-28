/*
 * Copyright (c) 2019 Naman Dwivedi.
 *
 * Licensed under the GNU General Public License v3
 *
 * This is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 *
 */
@file:Suppress("unused")

package com.detextre4.kotlinTemplate

import android.app.Application
import com.detextre4.kotlinTemplate.BuildConfig.DEBUG
import com.detextre4.kotlinTemplate.db.roomModule
import com.detextre4.kotlinTemplate.logging.FabricTree
import com.detextre4.kotlinTemplate.network.lastFmModule
import com.detextre4.kotlinTemplate.network.lyricsModule
import com.detextre4.kotlinTemplate.network.networkModule
import com.detextre4.kotlinTemplate.notifications.notificationModule
import com.detextre4.kotlinTemplate.permissions.permissionsModule
import com.detextre4.kotlinTemplate.playback.mediaModule
import com.detextre4.kotlinTemplate.repository.repositoriesModule
import com.detextre4.kotlinTemplate.ui.viewmodels.viewModelsModule
import org.koin.android.ext.android.startKoin
import timber.log.Timber

class TimberXApp : Application() {

    override fun onCreate() {
        super.onCreate()

        if (DEBUG) {
            Timber.plant(Timber.DebugTree())
        } else {
            Timber.plant(FabricTree())
        }

        val modules = listOf(
                mainModule,
                permissionsModule,
                mediaModule,
                prefsModule,
                networkModule,
                roomModule,
                notificationModule,
                repositoriesModule,
                viewModelsModule,
                lyricsModule,
                lastFmModule
        )
        startKoin(
                androidContext = this,
                modules = modules
        )
    }
}
