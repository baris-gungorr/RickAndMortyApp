package com.barisgungorr.rickandmortyapp.ui.detail

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.barisgungorr.rickandmortyapp.data.entity.Favorite
import com.barisgungorr.rickandmortyapp.data.repository.CharacterRepository
import com.barisgungorr.rickandmortyapp.data.source.locale.FavoriteDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val characterRepository: CharacterRepository
): ViewModel() {

    private var uiModel = MutableStateFlow(DetailUiModel())
    private val currentUIModel get() = uiModel.value
/*


    fun save () {
        val character = currentUIModel.character ?: return
        viewModelScope.launch {
            try {
                characterRepository.save(
                    characterId = character.characterId,
                    characterName = character.characterName,
                    characterAlive = character.characterAlive,
                    characterStatus = character.characterStatus,
                    characterSpecies = character.characterSpecies,
                    characterGender = character.characterGender,
                    characterLocation = character.characterLocation,
                    characterImage = character.characterImage

                )
                Log.e("Save", "ViewModel: Character saved successfully.")
            }catch (e:Exception) {
                Log.e("Save", "ViewModel: Error saving character: $e")
            }*/


    fun save(
        characterId: Int,
        characterName: String,
        //characterAlive: String,
        characterStatus: String,
        characterSpecies: String,
        characterGender: String,
        characterLocation: String,
        characterImage: String,
    ) {
        viewModelScope.launch {
            try {
                characterRepository.save(
                    characterId = characterId,
                    characterName = characterName,
                  //  characterAlive = characterAlive,
                    characterStatus = characterStatus,
                    characterSpecies = characterSpecies,
                    characterGender = characterGender,
                    characterLocation = characterLocation,
                    characterImage = characterImage
                )
                Log.e("Save", "ViewModel: Character saved successfully.")
            }catch (e:Exception) {
                Log.e("Save", "ViewModel: Error saving character: $e")
            }
        }
    }
}







data class DetailUiModel(
    val character: Favorite? = null,
)


