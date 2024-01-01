package com.barisgungorr.rickandmortyapp.ui.home

import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.barisgungorr.rickandmortyapp.R
import com.barisgungorr.rickandmortyapp.data.dto.CharacterItem
import com.barisgungorr.rickandmortyapp.databinding.ItemCharacterBinding
import com.bumptech.glide.Glide

class HomeViewHolder(view: View): RecyclerView.ViewHolder(view){

    private val binding = ItemCharacterBinding.bind(view)

    fun bind(characterItem: CharacterItem, onItemSelected:(CharacterItem) -> Unit){
        binding.tvCharactersName.text = characterItem.name
        binding.tvStatus.text = characterItem.status
        //binding.tv = characterItem.species

        when(characterItem.status.lowercase()){
            "alive" -> binding.ivAlive.background = ContextCompat.getDrawable(itemView.context , R.drawable.baseline_alive)
            "dead" -> binding.ivAlive.background = ContextCompat.getDrawable(itemView.context ,R.drawable.baseline_dead)
            "unknown" -> binding.ivAlive.background = ContextCompat.getDrawable(itemView.context ,R.drawable.baseline_unknown)
        }

        Glide.with(itemView.context).load(characterItem.image).into(binding.ivCharacters)

        itemView.setOnClickListener{
            onItemSelected(characterItem)
        }
    }
}