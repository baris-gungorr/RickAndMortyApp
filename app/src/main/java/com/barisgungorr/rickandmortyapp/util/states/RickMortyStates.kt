package com.barisgungorr.rickandmortyapp.util.states

import com.barisgungorr.rickandmortyapp.domain.model.RickMortyModel

class RickMortyStates (
    val load: Boolean  = false,
    val success: List<RickMortyModel> = emptyList(),
    val fail :String = "")

