package com.barisgungorr.rickandmortyapp.ui.detail

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.barisgungorr.rickandmortyapp.data.dto.CharacterItem
import com.barisgungorr.rickandmortyapp.data.entity.Favorite
import com.barisgungorr.rickandmortyapp.data.repository.CharacterRepository
import com.barisgungorr.rickandmortyapp.data.source.locale.FavoriteDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val characterRepository: CharacterRepository,

): ViewModel() {

    val startAnimation = MutableLiveData<Unit>()
    val stopAnimation = MutableLiveData<Unit>()

    fun save(characterItem: CharacterItem) {
        viewModelScope.launch {
            try {
                characterRepository.save(
                    characterId = characterItem.id,
                    characterName = characterItem.name,
                    characterStatus = characterItem.status,
                    characterSpecies = characterItem.species,
                    characterGender = characterItem.gender,
                    characterLocation = characterItem.location.name,
                    characterImage = characterItem.image
                )
                Log.e("Save", "ViewModel: Character saved successfully.")
            }catch (e:Exception) {
                Log.e("Save", "ViewModel: Error saving character: $e")
            }
        }
    }

    fun startAnimation() {
        startAnimation.value = Unit
    }

    fun stopAnimation() {
        stopAnimation.value = Unit
    }
}



