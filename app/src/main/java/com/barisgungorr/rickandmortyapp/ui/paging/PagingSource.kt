package com.barisgungorr.rickandmortyapp.ui.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.barisgungorr.rickandmortyapp.data.dto.CharacterItem
import com.barisgungorr.rickandmortyapp.data.source.remote.ApiService
import retrofit2.HttpException

class PagingSource(
    private val apiService: ApiService,
    private val characterName: String
) : PagingSource<Int, CharacterItem>() {

    override fun getRefreshKey(state: PagingState<Int, CharacterItem>): Int? {
        return null
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, CharacterItem> {
        try {
            val currentPage = params.key ?: 1
            val response = apiService.getCharactersByName(characterName, currentPage)
            return if (response.isSuccessful) {
                val data = response.body()?.results ?: emptyList()
                LoadResult.Page(
                    data = data,
                    prevKey = if (currentPage == 1) null else currentPage - 1,
                    nextKey = currentPage.plus(1)
                )
            } else {
                LoadResult.Error(HttpException(response))
            }
        } catch (e: Exception) {
            return LoadResult.Error(e)
        }
    }
}

