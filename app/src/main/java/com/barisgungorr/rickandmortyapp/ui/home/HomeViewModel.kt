package com.barisgungorr.rickandmortyapp.ui.home

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.barisgungorr.rickandmortyapp.data.dto.Character
import com.barisgungorr.rickandmortyapp.data.dto.CharacterItem
import com.barisgungorr.rickandmortyapp.data.dto.ItemsInfo
import com.barisgungorr.rickandmortyapp.data.dto.ResponseApi
import com.barisgungorr.rickandmortyapp.data.source.remote.ApiService
import com.barisgungorr.rickandmortyapp.domain.usecase.characters.GetCharacterByIdUseCase
import com.barisgungorr.rickandmortyapp.domain.usecase.characters.GetCharacterByNameUseCase
import com.barisgungorr.rickandmortyapp.domain.usecase.characters.GetCharacterUseCase
import com.barisgungorr.rickandmortyapp.ui.paging.PagingSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getCharacterUseCase: GetCharacterUseCase,
    private val getCharacterByNameUseCase: GetCharacterByNameUseCase,
    private val getCharacterByIdUseCase: GetCharacterByIdUseCase,
    apiService: ApiService
): ViewModel(){

    val charactersResponse = MutableLiveData<Response<ResponseApi>>()
    val characterListItemResponse = MutableLiveData<Response<ItemsInfo>>()
    val characterItemResponse = MutableLiveData<Response<CharacterItem>>()
    private val _loadingState = MutableLiveData<Boolean>()
    val isLoading = MutableLiveData<Boolean>()


    val listData = Pager(PagingConfig(pageSize = 20)) {
        PagingSource(apiService)
    }.flow

/*
  fun onCreateList(){
        viewModelScope.launch {
            isLoading.postValue(true)
            val query = "1,2,3,4,5,6,7,8,9,10,11,12"
            val listResult = getCharacterUseCase(query)

            listResult.let {
                charactersResponse.postValue(listResult)
                isLoading.postValue(false)
            }
        }
    }
 */
fun onCreateList() {
    viewModelScope.launch {
        _loadingState.value = true
        try {

            listData.collect{

            }
        } catch (e: Exception) {
            Log.e("Error", "Error during data collection: ${e.message}", e)
        } finally {
            _loadingState.value = false
        }
    }
}



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





