package com.barisgungorr.rickandmortyapp.data.repository

import android.util.Log
import com.barisgungorr.rickandmortyapp.data.source.remote.ApiService
import com.barisgungorr.rickandmortyapp.data.dto.CharacterItem
import com.barisgungorr.rickandmortyapp.data.dto.ItemsInfo
import com.barisgungorr.rickandmortyapp.data.dto.ResponseApi
import com.barisgungorr.rickandmortyapp.data.entity.Favorite
import com.barisgungorr.rickandmortyapp.data.source.locale.FavoriteDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import javax.inject.Inject

private const val CHARACTER : String = "character/"
class CharacterRepository @Inject constructor(
    private val apiService: ApiService,
    private val favoriteDao: FavoriteDao
){
    suspend fun getCharacters(query: String): Response<ResponseApi> {
        val page: Int = try {
            query.toInt()
        } catch (e: NumberFormatException) {
            0
        }
        return apiService.getAllCharacter(page)
    }

   // suspend fun getCharacters(query: String): Response<ResponseApi> = apiService.getAllCharacters(
     //     CHARACTER +query)

    suspend fun getCharactersByName(name: String): Response<ItemsInfo> = apiService.getCharactersByName(
            CHARACTER +name)

    suspend fun getCharactersById(id: String): Response<CharacterItem> = apiService.getCharactersById(
            CHARACTER +id)

    // FAVORITE
  suspend fun save(characterId:Int,
                     characterName:String,
                     characterStatus:String,
                     characterSpecies:String,
                     characterGender:String,
                     characterLocation:String,
                     characterImage:String,
                     ) {
                 val newFavorite = Favorite(
                characterId,
                characterName,
                characterStatus,
                characterSpecies,
                characterGender,
                characterLocation,
                characterImage,
        )
        favoriteDao.save(newFavorite)
        Log.e("Save", "Character saved successfully: $newFavorite")
    }

    suspend fun getFavorites(): List<Favorite> = withContext(Dispatchers.IO) {
        return@withContext favoriteDao.getFavorites()
    }

   suspend fun deleteFavorite(characterId: Int) {
       val deleteFavorite = Favorite(characterId = characterId)
       favoriteDao.deleteFavorite(deleteFavorite)
    }

    suspend fun searchFavorite(searchKeyword: String): List<Favorite> = withContext(Dispatchers.IO) {
        return@withContext favoriteDao.searchFavorite(searchKeyword)
    }
}