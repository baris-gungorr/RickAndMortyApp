package com.barisgungorr.rickandmortyapp.ui.home

import android.os.Build
import androidx.annotation.RequiresExtension
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.barisgungorr.rickandmortyapp.domain.model.RickMortyModel
import com.barisgungorr.rickandmortyapp.domain.usecase.GetCharacterByIdUseCase
import com.barisgungorr.rickandmortyapp.util.resource.Resource
import com.barisgungorr.rickandmortyapp.util.states.RickMortyStates
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject



@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@HiltViewModel
class HomeViewModel @Inject constructor(private val useCase: GetCharacterByIdUseCase) : ViewModel() {
    private val _state = MutableStateFlow(RickMortyStates())
    var state: MutableStateFlow<RickMortyStates> = _state
    private var cacheList = mutableListOf<RickMortyModel>()

    init {
        getItems()
    }


    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    private fun getItems() = viewModelScope.launch {
        useCase().collect {
            when (it) {
                is Resource.Success -> {
                    cacheList = it.data?.toMutableList() ?: mutableListOf()
                    _state.value = RickMortyStates(success = cacheList)
                }
                is Resource.Loading -> _state.value = RickMortyStates(load = true)
                is Resource.Error -> _state.value = RickMortyStates(fail = it.message ?: "")
            }
        }
    }

    fun search(searchKeyword: String) {
        viewModelScope.launch {
            val filteredList = if (searchKeyword.isEmpty()) {
                cacheList
            } else {
                cacheList.filter {
                    it.name.lowercase().contains(searchKeyword.lowercase())
                }.toMutableList()
            }
            _state.value = RickMortyStates(success = filteredList)
        }
    }
}
