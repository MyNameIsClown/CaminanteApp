package com.carrasco.caminante.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.carrasco.caminante.data.dao.PublicationDao
import com.carrasco.caminante.data.model.Publication
import kotlinx.coroutines.flow.Flow

class MainViewModel: ViewModel() {
    private val _state = MutableLiveData(UiState())
    val state: LiveData<UiState> get() = _state
    val publications = PublicationDao.getAll()

    init {
        _state.value = _state.value?.copy(publications = PublicationDao.getAll())
    }

    fun requestPublications(): Flow<List<Publication>> = PublicationDao.getAll()

    fun navigateTo(publication: Publication) {
        _state.value = _state.value?.copy(navigateTo = publication)
    }

    fun onNavigateDone(){
        _state.value = _state.value?.copy(navigateTo = null)
    }

    fun navigateToCreate() {
        _state.value = _state.value?.copy(navigateToCreate = true)
    }

    fun navigateToCreateDone() {
        _state.value = _state.value?.copy(navigateToCreate = false)
    }

    fun navigateToProfile(){
        _state.value = _state.value?.copy(navigateToProfile = true)
    }

    fun navigateToProfileDone(){
        _state.value = _state.value?.copy(navigateToProfile = false)
    }

    data class UiState(
        val loading: Boolean = false,
        val publications: Flow<List<Publication>>? = null,
        val navigateTo: Publication? = null,
        val navigateToCreate: Boolean = false,
        val navigateToProfile: Boolean = false
    )
}