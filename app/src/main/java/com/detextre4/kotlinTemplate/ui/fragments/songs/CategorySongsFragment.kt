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
package com.detextre4.kotlinTemplate.ui.fragments.songs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.detextre4.kotlinTemplate.R
import com.detextre4.kotlinTemplate.playback.TimberMusicService.Companion.TYPE_PLAYLIST
import com.detextre4.kotlinTemplate.constants.Constants.CATEGORY_SONG_DATA
import com.detextre4.kotlinTemplate.databinding.FragmentCategorySongsBinding
import com.detextre4.kotlinTemplate.extensions.addOnItemClick
import com.detextre4.kotlinTemplate.extensions.argument
import com.detextre4.kotlinTemplate.extensions.filter
import com.detextre4.kotlinTemplate.extensions.getExtraBundle
import com.detextre4.kotlinTemplate.extensions.inflateWithBinding
import com.detextre4.kotlinTemplate.extensions.observe
import com.detextre4.kotlinTemplate.extensions.safeActivity
import com.detextre4.kotlinTemplate.extensions.toSongIds
import com.detextre4.kotlinTemplate.models.CategorySongData
import com.detextre4.kotlinTemplate.models.Song
import com.detextre4.kotlinTemplate.ui.adapters.SongsAdapter
import com.detextre4.kotlinTemplate.ui.fragments.base.MediaItemFragment
import com.detextre4.kotlinTemplate.util.AutoClearedValue

class CategorySongsFragment : MediaItemFragment() {
    private lateinit var songsAdapter: SongsAdapter
    private lateinit var categorySongData: CategorySongData
    var binding by AutoClearedValue<FragmentCategorySongsBinding>(this)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        categorySongData = argument(CATEGORY_SONG_DATA)
        binding = inflater.inflateWithBinding(R.layout.fragment_category_songs, container)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.categorySongData = categorySongData

        songsAdapter = SongsAdapter(this).apply {
            popupMenuListener = mainViewModel.popupMenuListener
            if (categorySongData.type == TYPE_PLAYLIST) {
                playlistId = categorySongData.id
            }
        }

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(safeActivity)
            adapter = songsAdapter
            addOnItemClick { position: Int, _: View ->
                val extras = getExtraBundle(songsAdapter.songs.toSongIds(), categorySongData.title)
                mainViewModel.mediaItemClicked(songsAdapter.songs[position], extras)
            }
        }

        mediaItemFragmentViewModel.mediaItems
                .filter { it.isNotEmpty() }
                .observe(this) { list ->
                    @Suppress("UNCHECKED_CAST")
                    songsAdapter.updateData(list as List<Song>)
                }
    }
}
