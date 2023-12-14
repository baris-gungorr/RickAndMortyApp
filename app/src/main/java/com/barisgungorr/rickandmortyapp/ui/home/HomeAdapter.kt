package com.barisgungorr.rickandmortyapp.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.barisgungorr.rickandmortyapp.R
import com.barisgungorr.rickandmortyapp.databinding.ItemCharacterBinding
import com.barisgungorr.rickandmortyapp.domain.model.RickMortyModel
import com.barisgungorr.rickandmortyapp.util.extension.load

class HomeAdapter : RecyclerView.Adapter<HomeAdapter.HomeViewHolder>() {
    inner class HomeViewHolder (val binding: ItemCharacterBinding) : RecyclerView.ViewHolder(binding.root)

    private val differCallback = object : DiffUtil.ItemCallback<RickMortyModel>() {

        override fun areItemsTheSame(oldItem: RickMortyModel, newItem: RickMortyModel): Boolean = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: RickMortyModel, newItem: RickMortyModel): Boolean = oldItem == newItem
    }
    private  val differ = AsyncListDiffer(this, differCallback)
    fun submitList(list: List<RickMortyModel>) = differ.submitList(list)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder = HomeViewHolder(
        ItemCharacterBinding.inflate(
            LayoutInflater.from(
                parent.context
            ), parent, false
        )
    )

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
       val item = differ.currentList[position]
        holder.binding.apply {
           ivCharacter.load(item.image)
            tvName.text = item.name
            tvStatus.text = item.status
            tvSpecies.text = item.species


        }
    }
    override fun getItemCount(): Int = differ.currentList.size }






