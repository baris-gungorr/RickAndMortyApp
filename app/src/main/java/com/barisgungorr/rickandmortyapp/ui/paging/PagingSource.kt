package com.barisgungorr.rickandmortyapp.ui.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.barisgungorr.rickandmortyapp.data.dto.CharacterItem
import com.barisgungorr.rickandmortyapp.data.dto.ItemsInfo
import com.barisgungorr.rickandmortyapp.data.source.remote.ApiService
import com.barisgungorr.rickandmortyapp.domain.usecase.characters.GetCharacterUseCase
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/*
class PagingSource (
    private val service: ApiService
) : PagingSource<Int, CharacterItem>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, CharacterItem> {
        return try {
            val currentPage = params.key ?: 1
            val response = service.getAllCharacters(currentPage.toString())
            val data = response.body()?.results ?: emptyList()
            val responseData = mutableListOf<CharacterItem>()
            responseData.addAll(data)


            LoadResult.Page(
                data = responseData,
                prevKey = if (currentPage == 1) null else -1,
                nextKey = currentPage.plus(1)
            )

        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, CharacterItem>): Int? {
        return null
    }
}

 */
