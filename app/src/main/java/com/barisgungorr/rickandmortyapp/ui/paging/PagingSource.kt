package com.barisgungorr.rickandmortyapp.ui.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.barisgungorr.rickandmortyapp.data.dto.CharacterItem
import com.barisgungorr.rickandmortyapp.data.source.remote.ApiService

class PagingSource(
    private val apiService: ApiService
) : PagingSource<Int, CharacterItem>() {

    override fun getRefreshKey(state: PagingState<Int, CharacterItem>): Int? {
        return null
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, CharacterItem> {
         try {

            val currentPage = params.key ?: 1
            val response = apiService.getAllCharacter(currentPage)
            val data = response.body()?.results ?: emptyList()
            val responseData = mutableListOf<CharacterItem>()
            responseData.addAll(data)

            println("Page: $currentPage, Data: ${responseData.size}")

           return LoadResult.Page(
                data = responseData,
                prevKey = if (currentPage == 1) null else -1,
                nextKey = currentPage.plus(1)
            )

        } catch (e: Exception) {
          return  LoadResult.Error(e)
        }
    }
}