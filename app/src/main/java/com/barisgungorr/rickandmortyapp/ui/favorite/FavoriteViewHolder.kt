package com.barisgungorr.rickandmortyapp.ui.favorite

import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.barisgungorr.rickandmortyapp.R
import com.barisgungorr.rickandmortyapp.data.dto.CharacterItem
import com.barisgungorr.rickandmortyapp.data.entity.Favorite
import com.barisgungorr.rickandmortyapp.databinding.ItemCharacterBinding
import com.barisgungorr.rickandmortyapp.databinding.ItemViewFavoriteCardBinding
import com.bumptech.glide.Glide

class FavoriteViewHolder(
    private val binding: ItemViewFavoriteCardBinding,
    private val favoriteCallBacks: FavoriteAdapter.FavoriteCallBack
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(favorite: Favorite) = with(binding) {
        tvName.text = favorite.characterName
        tvStatusFavorite.text = favorite.characterStatus
        tvSpeciesFavorite.text = favorite.characterSpecies
        tvGender.text = favorite.characterGender
        tvLocation.text = favorite.characterLocation

        when (favorite.characterStatus.lowercase()) {
            "alive" -> binding.ivDetailAlive.background =
                ContextCompat.getDrawable(itemView.context, R.drawable.baseline_alive)

            "dead" -> binding.ivDetailAlive.background =
                ContextCompat.getDrawable(itemView.context, R.drawable.baseline_dead)

            "unknown" -> binding.ivDetailAlive.background = ContextCompat.getDrawable(
                itemView.context,
                R.drawable.baseline_unknown
            )
        }
        Glide.with(itemView.context).load(favorite.characterImage).into(binding.ivCharacter)

        ivFavoriteDelete.setOnClickListener {
            favoriteCallBacks.onDeleteFavorite(favorite = favorite)
        }
    }
}