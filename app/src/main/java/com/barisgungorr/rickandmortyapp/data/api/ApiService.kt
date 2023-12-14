package com.barisgungorr.rickandmortyapp.data.api

import com.barisgungorr.rickandmortyapp.data.dto.Characters
import com.barisgungorr.rickandmortyapp.util.Constants
import retrofit2.http.GET

interface ApiService {
    @GET(Constants.END_POINT)
    suspend fun getCharacters(): Characters
}