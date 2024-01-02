package com.barisgungorr.rickandmortyapp.data.source.remote

import com.barisgungorr.rickandmortyapp.data.dto.Character
import com.barisgungorr.rickandmortyapp.data.dto.CharacterItem
import com.barisgungorr.rickandmortyapp.data.dto.Episodes
import com.barisgungorr.rickandmortyapp.data.dto.EpisodesItem
import com.barisgungorr.rickandmortyapp.data.dto.ItemsInfo
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Url

interface ApiService {
    @GET()
    suspend fun getCharacters(@Url url:String): Response<Character>
    @GET
    suspend fun getCharactersById(@Url url:String): Response<CharacterItem>
    @GET
    suspend fun getCharactersByName(@Url url:String): Response<ItemsInfo>

    // EPISODES API-CALLS

    @GET
    suspend fun getEpisodes(@Url url: String): Response<Episodes>
    @GET
    suspend fun getEpisodesById(@Url url: String): Response<EpisodesItem>

}