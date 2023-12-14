package com.barisgungorr.rickandmortyapp.domain.repository

import com.barisgungorr.rickandmortyapp.data.dto.Characters

interface RickAndMortyRepository {
    suspend fun getCharacters() : Characters
}