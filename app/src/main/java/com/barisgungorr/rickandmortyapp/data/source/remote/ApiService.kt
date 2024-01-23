package com.barisgungorr.rickandmortyapp.data.source.remote

import com.barisgungorr.rickandmortyapp.data.dto.CharacterItem
import com.barisgungorr.rickandmortyapp.data.dto.Episodes
import com.barisgungorr.rickandmortyapp.data.dto.EpisodesItem
import com.barisgungorr.rickandmortyapp.data.dto.ItemsInfo
import com.barisgungorr.rickandmortyapp.data.dto.ResponseApi
import com.barisgungorr.rickandmortyapp.util.constanst.Constants
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

interface ApiService {

    @GET(Constants.CHAR_POINT)
    suspend fun getAllCharacter(
        @Query("page") page: Int
    ): Response<ResponseApi>

    @GET
    suspend fun getCharactersById(@Url url:String): Response<CharacterItem>

   // @GET
   // suspend fun getCharactersByName(query:String): Response<ResponseApi>
   @GET(Constants.CHAR_POINT)
   suspend fun getCharactersByName(
       @Query("name") name: String
   ): Response<ResponseApi>


    // EPISODES API-CALLS

    @GET
    suspend fun getEpisodes(@Url url: String): Response<Episodes>
    @GET
    suspend fun getEpisodesById(@Url url: String): Response<EpisodesItem>

}