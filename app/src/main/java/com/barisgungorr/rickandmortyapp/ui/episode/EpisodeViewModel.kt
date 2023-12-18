package com.barisgungorr.rickandmortyapp.ui.episode

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.barisgungorr.rickandmortyapp.data.dto.Episodes
import com.barisgungorr.rickandmortyapp.data.dto.EpisodesItem
import com.barisgungorr.rickandmortyapp.domain.usecase.episodes.GetEpisodeByIdUseCase
import com.barisgungorr.rickandmortyapp.domain.usecase.episodes.GetEpisodesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class EpisodeViewModel @Inject constructor(
    private val getEpisodeUseCase: GetEpisodesUseCase,
    private val getEpisodeByIdUseCase : GetEpisodeByIdUseCase,
): ViewModel(){

    val episodesListResponse = MutableLiveData<Response<Episodes>>()
    val episodesResponse = MutableLiveData<Response<EpisodesItem>>()

    fun onCreateListEpisodes(episodesN: String){
        viewModelScope.launch {
            val listEpisodes = getEpisodeUseCase(episodesN)

            listEpisodes?.let {
                episodesListResponse.postValue(listEpisodes)
            }
        }
    }
    fun onCreateEpisode(episodeN: String){
        viewModelScope.launch {
            val episode = getEpisodeByIdUseCase(episodeN)

            episode?.let {
                episodesResponse.postValue(episode)
            }
        }
    }
}