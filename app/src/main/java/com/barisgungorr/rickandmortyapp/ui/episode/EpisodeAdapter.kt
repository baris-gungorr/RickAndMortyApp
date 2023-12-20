package com.barisgungorr.rickandmortyapp.ui.episode

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.barisgungorr.rickandmortyapp.R
import com.barisgungorr.rickandmortyapp.data.dto.Episodes

class EpisodeAdapter(private val episodes: Episodes): RecyclerView.Adapter<EpisodeViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EpisodeViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return EpisodeViewHolder(layoutInflater.inflate(R.layout.item_episode, parent, false))
    }

    override fun getItemCount(): Int = episodes.size

    override fun onBindViewHolder(holder: EpisodeViewHolder, position: Int) {
        val item = episodes[position]
        item?.let {
            holder.bind(item)
        }
    }
}