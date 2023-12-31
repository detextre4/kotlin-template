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
package com.detextre4.kotlinTemplate.ui.adapters

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.detextre4.kotlinTemplate.R
import com.detextre4.kotlinTemplate.databinding.ItemGenreBinding
import com.detextre4.kotlinTemplate.models.Genre
import com.detextre4.kotlinTemplate.extensions.inflateWithBinding

class GenreAdapter : RecyclerView.Adapter<GenreAdapter.ViewHolder>() {
    var genres: List<Genre> = emptyList()
        private set

    fun updateData(genres: List<Genre>) {
        this.genres = genres
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(parent.inflateWithBinding(R.layout.item_genre))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(genres[position])
    }

    override fun getItemCount() = genres.size

    class ViewHolder constructor(var binding: ItemGenreBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(genre: Genre) {
            binding.genre = genre
            binding.executePendingBindings()
        }
    }
}
