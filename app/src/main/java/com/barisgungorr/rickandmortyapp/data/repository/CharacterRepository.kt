package com.barisgungorr.rickandmortyapp.data.repository

import com.barisgungorr.rickandmortyapp.data.api.ApiService
import com.barisgungorr.rickandmortyapp.data.dto.Character
import com.barisgungorr.rickandmortyapp.data.dto.CharacterItem
import com.barisgungorr.rickandmortyapp.data.dto.ItemsInfo
import retrofit2.Response
import javax.inject.Inject

private const val CHARACTER : String = "character/"
class CharacterRepository @Inject constructor(private val apiService: ApiService
){
      //  override suspend fun getCharacters(): ItemsInfo = apiService.getCharacters()
        suspend fun getCharacters(query: String): Response<Character> = apiService.getCharacters(
          CHARACTER +query)

        suspend fun getCharactersByName(name: String): Response<ItemsInfo> = apiService.getCharactersByName(
            CHARACTER +name)

        suspend fun getCharactersById(id: String): Response<CharacterItem> = apiService.getCharactersById(
            CHARACTER +id)


}