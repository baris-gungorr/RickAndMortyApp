package com.barisgungorr.rickandmortyapp.ui.favorite

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.barisgungorr.rickandmortyapp.data.entity.Favorite
import com.barisgungorr.rickandmortyapp.data.repository.CharacterRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    private val characterRepository: CharacterRepository
): ViewModel() {

    val favoriteList = MutableLiveData<List<Favorite>>()
    init {
        getFavorites()
    }

    fun getFavorites() {
        viewModelScope.launch {
            favoriteList.value = characterRepository.getFavorites()
        }
    }

    fun deleteFavorite(characterId: Int) {
        viewModelScope.launch {
            characterRepository.deleteFavorite(characterId)
            getFavorites()
        }
    }
   fun searchFavorite(searchKeyword: String) {
        viewModelScope.launch {
            favoriteList.value = characterRepository.searchFavorite(searchKeyword)
        }
    }
}


