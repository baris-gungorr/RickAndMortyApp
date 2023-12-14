package com.barisgungorr.rickandmortyapp.di

import com.barisgungorr.rickandmortyapp.data.api.ApiService
import com.barisgungorr.rickandmortyapp.data.repository.RickAndMortyRepositoryImpl
import com.barisgungorr.rickandmortyapp.domain.repository.RickAndMortyRepository
import com.barisgungorr.rickandmortyapp.util.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
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
    fun providesRepository(apiService: ApiService):RickAndMortyRepository = RickAndMortyRepositoryImpl(apiService)

}