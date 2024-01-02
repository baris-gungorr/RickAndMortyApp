package com.barisgungorr.rickandmortyapp.data.repository

import com.barisgungorr.rickandmortyapp.data.source.remote.ApiService
import com.barisgungorr.rickandmortyapp.data.dto.Episodes
import com.barisgungorr.rickandmortyapp.data.dto.EpisodesItem
import retrofit2.Response
import javax.inject.Inject

private const val EPISODE : String = "episode/"
class EpisodeRepository @Inject constructor(
    private val api: ApiService
) {
    suspend fun getEpisodes(query: String): Response<Episodes> = api.getEpisodes(EPISODE +query)
    suspend fun getEpisodeById(query: String): Response<EpisodesItem> = api.getEpisodesById(EPISODE +query)

}