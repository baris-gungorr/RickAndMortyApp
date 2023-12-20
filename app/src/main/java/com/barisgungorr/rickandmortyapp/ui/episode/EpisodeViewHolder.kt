package com.barisgungorr.rickandmortyapp.ui.episode

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.barisgungorr.rickandmortyapp.data.dto.EpisodesItem
import com.barisgungorr.rickandmortyapp.databinding.ItemEpisodeBinding

class EpisodeViewHolder(view: View): RecyclerView.ViewHolder(view){

    private val binding = ItemEpisodeBinding.bind(view)

    fun bind(episode: EpisodesItem){
        binding.tvEpisodeName.text = episode.name
    }
}