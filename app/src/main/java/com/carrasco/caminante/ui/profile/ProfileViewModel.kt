package com.carrasco.caminante.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.carrasco.caminante.data.dao.PublicationDao
import com.carrasco.caminante.data.dao.UserDao
import com.carrasco.caminante.data.model.Publication
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class ProfileViewModel(): ViewModel() {
    private val _state = MutableLiveData(UiState())
    val state: LiveData<UiState> get() = _state
    lateinit var publications: Flow<List<Publication>>

    init {
        lateinit var result: Flow<List<Publication>>
        viewModelScope.launch {
            result = requestPublications()
        }
        _state.value = _state.value?.copy(publications = result)
    }

    suspend fun requestPublications(): Flow<List<Publication>> = UserDao.getSavedPublications()

    fun navigateTo(publication: Publication) {
        _state.value = _state.value?.copy(navigateTo = publication)
    }

    fun onNavigateDone(){
        _state.value = _state.value?.copy(navigateTo = null)
    }

    data class UiState(
        val loading: Boolean = false,
        val publications: Flow<List<Publication>>? = null,
        val navigateTo: Publication? = null
    )
}