package com.barisgungorr.rickandmortyapp.domain.usecase

import com.barisgungorr.rickandmortyapp.data.dto.Character
import com.barisgungorr.rickandmortyapp.data.dto.CharacterItem
import com.barisgungorr.rickandmortyapp.data.repository.CharacterRepository
import retrofit2.Response
import javax.inject.Inject

class GetCharacterByIdUseCase@Inject constructor(private val repository: CharacterRepository) {

    suspend operator fun invoke(query: String) : Response<CharacterItem> = repository.getCharactersById(query)



}
