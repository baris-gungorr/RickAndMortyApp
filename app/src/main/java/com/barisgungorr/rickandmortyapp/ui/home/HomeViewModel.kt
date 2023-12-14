package com.barisgungorr.rickandmortyapp.ui.home

import android.os.Build
import androidx.annotation.RequiresExtension
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.barisgungorr.rickandmortyapp.domain.repository.RickAndMortyRepository
import com.barisgungorr.rickandmortyapp.domain.usecase.RickMortyUseCase
import com.barisgungorr.rickandmortyapp.util.Resource
import com.barisgungorr.rickandmortyapp.util.RickMortyStates
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject



@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@HiltViewModel
class HomeViewModel @Inject constructor(private val useCase: RickMortyUseCase) : ViewModel() {
    private val _state = MutableStateFlow(RickMortyStates())
    var state : MutableStateFlow<RickMortyStates> = _state

    init {
        getItems()
    }


    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    private fun getItems() = viewModelScope.launch {
        useCase().collect {
            when (it) {
                is Resource.Success -> _state.value = RickMortyStates(success = it.data ?: emptyList())
                is Resource.Loading -> _state.value = RickMortyStates(load = true)
                is Resource.Error -> _state.value = RickMortyStates(fail = it.message ?: "")
            }
        }
    }
}