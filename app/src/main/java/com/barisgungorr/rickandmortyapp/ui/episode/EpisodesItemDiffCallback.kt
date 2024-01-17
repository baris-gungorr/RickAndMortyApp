package com.barisgungorr.rickandmortyapp.ui.episode

import androidx.recyclerview.widget.DiffUtil
import com.barisgungorr.rickandmortyapp.data.dto.EpisodesItem


class EpisodesItemDiffCallback : DiffUtil.ItemCallback<EpisodesItem>() {
    override fun areItemsTheSame(oldItem: EpisodesItem, newItem: EpisodesItem): Boolean {
        return oldItem.id == newItem.id
    }
    override fun areContentsTheSame(oldItem: EpisodesItem, newItem: EpisodesItem): Boolean {
        return oldItem == newItem
    }
}
