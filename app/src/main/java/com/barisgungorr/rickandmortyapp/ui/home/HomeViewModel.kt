package com.barisgungorr.rickandmortyapp.ui.home

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.barisgungorr.rickandmortyapp.data.dto.CharacterItem
import com.barisgungorr.rickandmortyapp.data.dto.ResponseApi
import com.barisgungorr.rickandmortyapp.data.source.remote.ApiService
import com.barisgungorr.rickandmortyapp.domain.usecase.characters.GetCharacterByIdUseCase
import com.barisgungorr.rickandmortyapp.ui.paging.PagingSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getCharacterByIdUseCase: GetCharacterByIdUseCase,
    private val apiService: ApiService
): ViewModel(){

    val charactersResponse = MutableLiveData<Response<ResponseApi>>()
    val characterItemResponse = MutableLiveData<Response<CharacterItem>>()
    val isLoading = MutableLiveData<Boolean>()
    private val characterCache = mutableMapOf<String, CharacterItem>()


    var listData: Flow<PagingData<CharacterItem>> = Pager(PagingConfig(pageSize = 1)) {
        PagingSource(apiService,"")
    }.flow


    private fun filterCharacters(characters: List<CharacterItem>, searchQuery: String): List<CharacterItem> {
        return characters.filter {
            it.name.contains(searchQuery, ignoreCase = true)
        }
    }

    fun loadList() {
        viewModelScope.launch {
            isLoading.value = true

            try {
                val response = apiService.getAllCharacter(1)
                charactersResponse.postValue(response)
            } catch (e: Exception) {
                Log.e("HomeViewModel", "Error loading list: ${e.message}")
            } finally {
                isLoading.value = false
            }
        }
    }
    fun loadCharacterByName(characterName: String) {
        viewModelScope.launch {
            isLoading.value = true

            try {
                val response = apiService.getCharactersByName(characterName,1)
                charactersResponse.postValue(response)

                if (response.isSuccessful) {
                    val filteredCharacters = filterCharacters(response.body()?.results ?: emptyList(), characterName)
                    listData = Pager(PagingConfig(pageSize = 20)) {
                        PagingSource(apiService, characterName)
                    }.flow
                    charactersResponse.postValue(Response.success(ResponseApi(results = filteredCharacters)))

                    characterCache[filteredCharacters.first().id.toString()] = filteredCharacters.first()
                } else {
                    val errorBody = response.errorBody()
                    if (errorBody != null) {
                        characterItemResponse.postValue(Response.error(response.code(), errorBody))
                    }
                }
            } catch (e: Exception) {
                Log.e("HomeViewModel", "Error loading character: ${e.message}")
            } finally {
                isLoading.value = false
            }
        }
    }

    fun loadCharacterItemById(characterId: String){
        viewModelScope.launch {
            isLoading.value = true
            val character = getCharacterFromCacheOrApi(characterId)
            character?.let {
                characterItemResponse.postValue(Response.success(it))
            }
            isLoading.value = false
        }
    }
    private suspend fun getCharacterFromCacheOrApi(characterId: String): CharacterItem? {
        return characterCache[characterId] ?: getCharacterByIdUseCase(characterId).body()
    }
}