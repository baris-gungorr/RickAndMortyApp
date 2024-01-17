package com.barisgungorr.rickandmortyapp.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
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
    apiService: ApiService
): ViewModel(){

    val charactersResponse = MutableLiveData<Response<ResponseApi>>()
    val characterListItemResponse = MutableLiveData<Response<ItemsInfo>>()
    val characterItemResponse = MutableLiveData<Response<CharacterItem>>()
    val isLoading = MutableLiveData<Boolean>()


    val listData = Pager(PagingConfig(pageSize = 20)) {
        PagingSource(apiService)
    }.flow


    fun onCreateCharacterByName(characterName: String){
        viewModelScope.launch {
            isLoading.postValue(true)
            val character = getCharacterByNameUseCase("?name=$characterName")
            character?.let {
                characterListItemResponse.postValue(character)
                isLoading.postValue(false)
            }
        }
    }

    fun onCreateCharacterItemById(characterId: String){
        viewModelScope.launch {
            isLoading.postValue(true)
            val character = getCharacterByIdUseCase(characterId)
            character?.let {
                characterItemResponse.postValue(character)
                isLoading.postValue(false)
            }
        }
    }
}





