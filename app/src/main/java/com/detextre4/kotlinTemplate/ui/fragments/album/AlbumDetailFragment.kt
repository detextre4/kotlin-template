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
package com.detextre4.kotlinTemplate.ui.fragments.album

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.detextre4.kotlinTemplate.R
import com.detextre4.kotlinTemplate.constants.Constants.ALBUM
import com.detextre4.kotlinTemplate.databinding.FragmentAlbumDetailBinding
import com.detextre4.kotlinTemplate.extensions.addOnItemClick
import com.detextre4.kotlinTemplate.extensions.argument
import com.detextre4.kotlinTemplate.extensions.filter
import com.detextre4.kotlinTemplate.extensions.getExtraBundle
import com.detextre4.kotlinTemplate.extensions.inflateWithBinding
import com.detextre4.kotlinTemplate.extensions.observe
import com.detextre4.kotlinTemplate.extensions.safeActivity
import com.detextre4.kotlinTemplate.extensions.toSongIds
import com.detextre4.kotlinTemplate.models.Album
import com.detextre4.kotlinTemplate.models.Song
import com.detextre4.kotlinTemplate.ui.adapters.SongsAdapter
import com.detextre4.kotlinTemplate.ui.fragments.base.MediaItemFragment
import com.detextre4.kotlinTemplate.util.AutoClearedValue

class AlbumDetailFragment : MediaItemFragment() {
    private lateinit var songsAdapter: SongsAdapter
    lateinit var album: Album
    var binding by AutoClearedValue<FragmentAlbumDetailBinding>(this)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        album = argument(ALBUM)
        binding = inflater.inflateWithBinding(R.layout.fragment_album_detail, container)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.album = album

        songsAdapter = SongsAdapter(this).apply {
            popupMenuListener = mainViewModel.popupMenuListener
        }
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(safeActivity)
            adapter = songsAdapter
            addOnItemClick { position: Int, _: View ->
                val extras = getExtraBundle(songsAdapter.songs.toSongIds(), album.title)
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
