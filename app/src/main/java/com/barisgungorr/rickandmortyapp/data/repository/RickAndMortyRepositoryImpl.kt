package com.barisgungorr.rickandmortyapp.data.repository

import com.barisgungorr.rickandmortyapp.data.api.ApiService
import com.barisgungorr.rickandmortyapp.data.dto.Characters
import com.barisgungorr.rickandmortyapp.domain.repository.RickAndMortyRepository
import javax.inject.Inject

class RickAndMortyRepositoryImpl @Inject constructor(private val apiService: ApiService):
    RickAndMortyRepository {
        override suspend fun getCharacters(): Characters = apiService.getCharacters()

}