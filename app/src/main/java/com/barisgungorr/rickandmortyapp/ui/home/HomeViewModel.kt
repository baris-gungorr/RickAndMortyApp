package com.barisgungorr.rickandmortyapp.ui.home

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.barisgungorr.rickandmortyapp.data.dto.CharacterItem
import com.barisgungorr.rickandmortyapp.data.dto.ItemsInfo
import com.barisgungorr.rickandmortyapp.data.dto.ResponseApi
import com.barisgungorr.rickandmortyapp.data.source.remote.ApiService
import com.barisgungorr.rickandmortyapp.domain.usecase.characters.GetCharacterByIdUseCase
import com.barisgungorr.rickandmortyapp.domain.usecase.characters.GetCharacterByNameUseCase
import com.barisgungorr.rickandmortyapp.ui.paging.PagingSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getCharacterByNameUseCase: GetCharacterByNameUseCase,
    private val getCharacterByIdUseCase: GetCharacterByIdUseCase,
    private val apiService: ApiService
): ViewModel(){

    val charactersResponse = MutableLiveData<Response<ResponseApi>>()
    private val characterListItemResponse = MutableLiveData<Response<ItemsInfo>>()
    val characterItemResponse = MutableLiveData<Response<CharacterItem>>()
    val isLoading = MutableLiveData<Boolean>()
    val searchResults = MutableLiveData<PagingData<ItemsInfo>>()
    var searchQuery = MutableLiveData<String>()

    var listData = Pager(PagingConfig(pageSize = 20)) {
        PagingSource(apiService)
    }.flow

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

    fun loadCharacterByName(characterName: String){
        viewModelScope.launch {
            isLoading.value = true
            listData = Pager(PagingConfig(pageSize = 20)) {
                PagingSource(apiService)
            }.flow
            isLoading.value = false
        }
    }

    fun loadCharacterItemById(characterId: String){
        viewModelScope.launch {
            isLoading.value = true
            val character = getCharacterByIdUseCase(characterId)
            character?.let {
                characterItemResponse.postValue(character)
                isLoading.value = false
            }
        }
    }
}
