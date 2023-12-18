package com.barisgungorr.rickandmortyapp.domain.usecase.characters

import com.barisgungorr.rickandmortyapp.data.dto.ItemsInfo
import com.barisgungorr.rickandmortyapp.data.repository.CharacterRepository
import retrofit2.Response
import javax.inject.Inject

class GetCharacterByNameUseCase @Inject constructor(
    private val repository: CharacterRepository
) {
    suspend operator fun invoke(name: String) : Response<ItemsInfo> = repository.getCharactersByName(name)
}