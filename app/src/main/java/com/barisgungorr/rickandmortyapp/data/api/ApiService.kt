package com.barisgungorr.rickandmortyapp.data.api

import com.barisgungorr.rickandmortyapp.data.dto.Character
import com.barisgungorr.rickandmortyapp.data.dto.CharacterItem
import com.barisgungorr.rickandmortyapp.data.dto.ItemsInfo
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Url

interface ApiService {
    @GET
    suspend fun getCharacters(@Url url:String): Response<Character>


    @GET
    suspend fun getCharactersById(@Url url:String): Response<CharacterItem>

    @GET
    suspend fun getCharactersByName(@Url url:String): Response<ItemsInfo>





}