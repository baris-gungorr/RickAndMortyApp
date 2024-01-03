package com.barisgungorr.rickandmortyapp.data.source.locale

import androidx.room.Database
import androidx.room.RoomDatabase
import com.barisgungorr.rickandmortyapp.data.entity.Favorite

@Database(entities = [Favorite::class], version = 3)
abstract class Database : RoomDatabase() {
    abstract fun getFavoritesDao() : FavoriteDao
}