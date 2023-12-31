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
package com.detextre4.kotlinTemplate.models

import android.database.Cursor
import android.provider.MediaStore.Audio.Genres.NAME
import android.provider.MediaStore.Audio.Genres._ID
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaDescriptionCompat
import com.detextre4.kotlinTemplate.playback.TimberMusicService.Companion.TYPE_GENRE
import com.detextre4.kotlinTemplate.extensions.value
import com.detextre4.kotlinTemplate.extensions.valueOrEmpty
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Genre(
    val id: Long,
    val name: String,
    val songCount: Int
) : MediaBrowserCompat.MediaItem(
        MediaDescriptionCompat.Builder()
                .setMediaId(MediaID("$TYPE_GENRE", "$id").asString())
                .setTitle(name)
                .setSubtitle("$songCount songs")
                .build(), FLAG_BROWSABLE) {
    companion object {
        fun fromCursor(cursor: Cursor, songCount: Int): Genre {
            return Genre(
                    id = cursor.value(_ID),
                    name = cursor.valueOrEmpty(NAME),
                    songCount = songCount
            )
        }
    }
}
