package com.barisgungorr.rickandmortyapp.di

import android.content.Context
import androidx.room.Room
import com.barisgungorr.rickandmortyapp.data.source.remote.ApiService
import com.barisgungorr.rickandmortyapp.data.repository.CharacterRepository
import com.barisgungorr.rickandmortyapp.data.source.locale.Database
import com.barisgungorr.rickandmortyapp.data.source.locale.FavoriteDao
import com.barisgungorr.rickandmortyapp.util.constanst.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ModuleApp {

    @Singleton
    @Provides
    fun providesRetrofit(): ApiService = Retrofit.Builder()
        .baseUrl(Constants.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(ApiService::class.java)

    @Singleton
    @Provides
    fun providesRepository(apiService: ApiService,favoriteDao: FavoriteDao):CharacterRepository {
        return CharacterRepository(apiService,favoriteDao)
    }


    @Provides
    @Singleton
    fun provideFavoriteDao(@ApplicationContext context: Context): FavoriteDao {
        val vt = Room.databaseBuilder(context, Database::class.java,"character.sqlite").createFromAsset("character.sqlite").build()
        return vt.getFavoritesDao()
    }
}