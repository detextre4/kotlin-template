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
package com.detextre4.kotlinTemplate.ui.fragments.artist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager.HORIZONTAL
import com.detextre4.kotlinTemplate.R
import com.detextre4.kotlinTemplate.constants.Constants.ARTIST
import com.detextre4.kotlinTemplate.databinding.FragmentArtistDetailBinding
import com.detextre4.kotlinTemplate.extensions.addOnItemClick
import com.detextre4.kotlinTemplate.extensions.argument
import com.detextre4.kotlinTemplate.extensions.filter
import com.detextre4.kotlinTemplate.extensions.getExtraBundle
import com.detextre4.kotlinTemplate.extensions.inflateWithBinding
import com.detextre4.kotlinTemplate.extensions.observe
import com.detextre4.kotlinTemplate.extensions.safeActivity
import com.detextre4.kotlinTemplate.extensions.toSongIds
import com.detextre4.kotlinTemplate.models.Artist
import com.detextre4.kotlinTemplate.models.Song
import com.detextre4.kotlinTemplate.repository.AlbumRepository
import com.detextre4.kotlinTemplate.ui.adapters.AlbumAdapter
import com.detextre4.kotlinTemplate.ui.adapters.SongsAdapter
import com.detextre4.kotlinTemplate.ui.fragments.base.MediaItemFragment
import com.detextre4.kotlinTemplate.util.AutoClearedValue
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext
import org.koin.android.ext.android.inject

class ArtistDetailFragment : MediaItemFragment() {
    lateinit var artist: Artist
    var binding by AutoClearedValue<FragmentArtistDetailBinding>(this)

    private val albumRepository by inject<AlbumRepository>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        artist = argument(ARTIST)
        binding = inflater.inflateWithBinding(R.layout.fragment_artist_detail, container)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.artist = artist

        val adapter = SongsAdapter(this).apply {
            popupMenuListener = mainViewModel.popupMenuListener
        }
        binding.recyclerView.layoutManager = LinearLayoutManager(safeActivity)
        binding.recyclerView.adapter = adapter

        mediaItemFragmentViewModel.mediaItems
                .filter { it.isNotEmpty() }
                .observe(this) { list ->
                    @Suppress("UNCHECKED_CAST")
                    adapter.updateData(list as List<Song>)
                }

        binding.recyclerView.addOnItemClick { position: Int, _: View ->
            val extras = getExtraBundle(adapter.songs.toSongIds(), artist.name)
            mainViewModel.mediaItemClicked(adapter.songs[position], extras)
        }

        setupArtistAlbums()
    }

    private fun setupArtistAlbums() {
        val albumsAdapter = AlbumAdapter(true)
        binding.rvArtistAlbums.apply {
            layoutManager = LinearLayoutManager(safeActivity, HORIZONTAL, false)
            adapter = albumsAdapter
            addOnItemClick { position: Int, _: View ->
                mainViewModel.mediaItemClicked(albumsAdapter.albums[position], null)
            }
        }

        // TODO should this be in a view model?
        launch {
            val albums = withContext(IO) {
                albumRepository.getAlbumsForArtist(artist.id)
            }
            albumsAdapter.updateData(albums)
        }
    }
}
