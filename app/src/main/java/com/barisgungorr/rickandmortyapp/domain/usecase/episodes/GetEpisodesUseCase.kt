package com.barisgungorr.rickandmortyapp.domain.usecase.episodes

import com.barisgungorr.rickandmortyapp.data.dto.Episodes
import com.barisgungorr.rickandmortyapp.data.repository.EpisodeRepository
import retrofit2.Response
import javax.inject.Inject

class GetEpisodesUseCase @Inject constructor(
    private val repository: EpisodeRepository
){
    suspend operator fun invoke(query: String): Response<Episodes> = repository.getEpisodes(query)
}