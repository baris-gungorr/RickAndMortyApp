package com.barisgungorr.rickandmortyapp.domain.usecase.characters

import com.barisgungorr.rickandmortyapp.data.dto.Character
import com.barisgungorr.rickandmortyapp.data.dto.ResponseApi
import com.barisgungorr.rickandmortyapp.data.repository.CharacterRepository
import retrofit2.Response
import javax.inject.Inject

class GetCharacterUseCase @Inject constructor(
    private val repository: CharacterRepository
){
   // suspend operator fun invoke(query: String) : Response<ResponseApi> = repository.getCharacters(query)
   suspend operator fun invoke(query: String) : Response<Character> = repository.getCharacters(query)

}