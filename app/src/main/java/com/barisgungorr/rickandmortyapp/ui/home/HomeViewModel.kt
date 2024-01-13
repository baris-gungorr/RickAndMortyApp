package com.barisgungorr.rickandmortyapp.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.barisgungorr.rickandmortyapp.data.dto.Character
import com.barisgungorr.rickandmortyapp.data.dto.CharacterItem
import com.barisgungorr.rickandmortyapp.data.dto.ItemsInfo
import com.barisgungorr.rickandmortyapp.domain.usecase.characters.GetCharacterByIdUseCase
import com.barisgungorr.rickandmortyapp.domain.usecase.characters.GetCharacterByNameUseCase
import com.barisgungorr.rickandmortyapp.domain.usecase.characters.GetCharacterUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getCharacterUseCase: GetCharacterUseCase,
    private val getCharacterByNameUseCase: GetCharacterByNameUseCase,
    private val getCharacterByIdUseCase: GetCharacterByIdUseCase,
): ViewModel(){

    val charactersResponse = MutableLiveData<Response<Character>>()
    val characterListItemResponse = MutableLiveData<Response<ItemsInfo>>()
    val characterItemResponse = MutableLiveData<Response<CharacterItem>>()
    val isLoading = MutableLiveData<Boolean>()

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

