package com.barisgungorr.rickandmortyapp.domain.model

import com.barisgungorr.rickandmortyapp.data.dto.Location
import com.barisgungorr.rickandmortyapp.data.dto.Origin
import com.barisgungorr.rickandmortyapp.data.dto.CharacterItem

data class RickMortyModel(

    val created: String,
    val episode: List<String>,
    val gender: String,
    val id: Int,
    val image: String,
    val location: Location,
    val name: String,
    val origin: Origin,
    val species: String,
    val status: String,
    val type: String,
    val url: String
)
    fun CharacterItem.toDomain() = RickMortyModel(
        created = created,
        episode = episode,
        gender = gender,
        id = id,
        image = image,
        location = location,
        name = name,
        origin = origin,
        species = species,
        status = status,
        type = type,
        url = url
    )


