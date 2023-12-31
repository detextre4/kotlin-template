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
package com.detextre4.kotlinTemplate.ui.fragments

import android.animation.AnimatorInflater.loadStateListAnimator
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.afollestad.rxkprefs.Pref
import com.google.android.material.appbar.AppBarLayout
import com.detextre4.kotlinTemplate.PREF_START_PAGE
import com.detextre4.kotlinTemplate.R
import com.detextre4.kotlinTemplate.playback.TimberMusicService.Companion.TYPE_ALL_ALBUMS
import com.detextre4.kotlinTemplate.playback.TimberMusicService.Companion.TYPE_ALL_ARTISTS
import com.detextre4.kotlinTemplate.playback.TimberMusicService.Companion.TYPE_ALL_FOLDERS
import com.detextre4.kotlinTemplate.playback.TimberMusicService.Companion.TYPE_ALL_GENRES
import com.detextre4.kotlinTemplate.playback.TimberMusicService.Companion.TYPE_ALL_PLAYLISTS
import com.detextre4.kotlinTemplate.playback.TimberMusicService.Companion.TYPE_ALL_SONGS
import com.detextre4.kotlinTemplate.constants.StartPage
import com.detextre4.kotlinTemplate.databinding.MainFragmentBinding
import com.detextre4.kotlinTemplate.extensions.*
import com.detextre4.kotlinTemplate.models.MediaID
import com.detextre4.kotlinTemplate.ui.activities.MainActivity
import com.detextre4.kotlinTemplate.ui.activities.SettingsActivity
import com.detextre4.kotlinTemplate.ui.dialogs.AboutDialog
import com.detextre4.kotlinTemplate.ui.fragments.base.MediaItemFragment
import com.detextre4.kotlinTemplate.util.AutoClearedValue
import org.koin.android.ext.android.inject

class MainFragment : Fragment() {
    var binding by AutoClearedValue<MainFragmentBinding>(this)
    private val startPagePref by inject<Pref<StartPage>>(name = PREF_START_PAGE)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = inflater.inflateWithBinding(R.layout.main_fragment, container)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        setupViewPager(binding.viewpager)
        binding.tabLayout.setupWithViewPager(binding.viewpager)

        binding.appBar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { _, verticalOffset ->
            val animatorRes = if (verticalOffset == 0) {
                R.animator.appbar_elevation_disable
            } else {
                R.animator.appbar_elevation_enable
            }
            binding.appBar.stateListAnimator = loadStateListAnimator(context, animatorRes)
        })

        binding.toolbarLayout.toolbar.overflowIcon = safeActivity.drawable(R.drawable.ic_more_vert_black_24dp)

        val mainActivity = safeActivity as MainActivity
        mainActivity.setSupportActionBar(binding.toolbarLayout.toolbar)
        mainActivity.supportActionBar?.run {
            setDisplayShowTitleEnabled(false)
            setDisplayHomeAsUpEnabled(false)
        }

        binding.toolbarLayout.btnSearch.setOnClickListener { safeActivity.addFragment(fragment = SearchFragment()) }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (safeActivity as MainActivity).setupCastButton(binding.toolbarLayout.mediaRouteButton)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_main, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_item_about -> AboutDialog.show(safeActivity)
            R.id.menu_item_settings -> startActivity(Intent(activity, SettingsActivity::class.java))
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setupViewPager(viewPager: ViewPager) {
        val res = context?.resources ?: return
        val adapter = Adapter(childFragmentManager).apply {
            addFragment(
                    fragment = MediaItemFragment.newInstance(MediaID(TYPE_ALL_SONGS.toString(), null)),
                    title = res.getString(R.string.songs)
            )
            addFragment(
                    fragment = MediaItemFragment.newInstance(MediaID(TYPE_ALL_ALBUMS.toString(), null)),
                    title = res.getString(R.string.albums)
            )
            addFragment(
                    fragment = MediaItemFragment.newInstance(MediaID(TYPE_ALL_PLAYLISTS.toString(), null)),
                    title = res.getString(R.string.playlists)
            )
            addFragment(
                    fragment = MediaItemFragment.newInstance(MediaID(TYPE_ALL_ARTISTS.toString(), null)),
                    title = res.getString(R.string.artists)
            )
            addFragment(
                    fragment = MediaItemFragment.newInstance(MediaID(TYPE_ALL_FOLDERS.toString(), null)),
                    title = res.getString(R.string.folders)
            )
            addFragment(
                    fragment = MediaItemFragment.newInstance(MediaID(TYPE_ALL_GENRES.toString(), null)),
                    title = res.getString(R.string.genres)
            )
        }
        viewPager.adapter = adapter
        binding.viewpager.offscreenPageLimit = 1
        viewPager.setCurrentItem(startPagePref.get().index, false)
    }

    internal class Adapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
        private val fragments = ArrayList<Fragment>()
        private val titles = ArrayList<String>()

        fun addFragment(fragment: Fragment, title: String) {
            fragments.add(fragment)
            titles.add(title)
        }

        override fun getItem(position: Int) = fragments[position]

        override fun getCount() = fragments.size

        override fun getPageTitle(position: Int) = titles[position]
    }
}
