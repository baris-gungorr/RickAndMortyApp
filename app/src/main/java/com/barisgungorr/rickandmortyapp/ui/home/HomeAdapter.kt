package com.barisgungorr.rickandmortyapp.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.barisgungorr.rickandmortyapp.R
import com.barisgungorr.rickandmortyapp.data.dto.CharacterItem

class HomeAdapter( private val result: ArrayList<CharacterItem>,
                   private val onItemSelected:(CharacterItem) -> Unit)
                    :RecyclerView.Adapter<HomeViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return HomeViewHolder(layoutInflater.inflate(R.layout.item_character, parent, false))
    }
    override fun getItemCount(): Int = result.size

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        val item = result[position]
        item.let {
            holder.bind(item, onItemSelected)
        }
    }
}