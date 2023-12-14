package com.barisgungorr.rickandmortyapp.domain.usecase

import android.net.http.HttpException
import android.os.Build
import androidx.annotation.RequiresExtension
import com.barisgungorr.rickandmortyapp.domain.model.RickMortyModel
import com.barisgungorr.rickandmortyapp.domain.model.toDomain
import com.barisgungorr.rickandmortyapp.domain.repository.RickAndMortyRepository
import com.barisgungorr.rickandmortyapp.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException
import javax.inject.Inject

class RickMortyUseCase@Inject constructor(private val repository: RickAndMortyRepository) {
    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    suspend operator fun invoke() : Flow<Resource<List<RickMortyModel>>> = flow {
        try {
            emit(Resource.Loading())
            val data = repository.getCharacters().results.map {
                it.toDomain()
            }
            emit(Resource.Success(data))
        } catch (e: HttpException) {
            emit(Resource.Error(e.localizedMessage ?: "No internet connection"))
        }catch (e: IOException) {
            emit(Resource.Error(e.localizedMessage ?: "ERROR"))
        }
    }
}
