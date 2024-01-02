package com.barisgungorr.rickandmortyapp.data.source.locale

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.barisgungorr.rickandmortyapp.data.dto.CharacterItem
import com.barisgungorr.rickandmortyapp.data.entity.Favorite

@Dao
interface FavoriteDao {
    @Query("SELECT * FROM character")
   suspend fun getFavorites(): List<Favorite>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(favorite: Favorite)

    @Delete
    suspend fun deleteFavorite(favorite: Favorite)

    @Query("SELECT * FROM character WHERE character_name like '%' || :searchKeyword || '%' ")
    suspend fun searchFavorite(searchKeyword: String): List<Favorite>
}