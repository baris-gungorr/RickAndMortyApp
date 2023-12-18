package com.barisgungorr.rickandmortyapp.domain.usecase.episodes

import com.barisgungorr.rickandmortyapp.data.dto.EpisodesItem
import com.barisgungorr.rickandmortyapp.data.repository.EpisodeRepository
import retrofit2.Response
import javax.inject.Inject

class GetEpisodeByIdUseCase @Inject constructor(
    private val repository: EpisodeRepository
){
    suspend operator fun invoke(query: String): Response<EpisodesItem> = repository.getEpisodeById(query)
}