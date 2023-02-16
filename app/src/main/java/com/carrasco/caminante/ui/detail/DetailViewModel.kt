package com.carrasco.caminante.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.carrasco.caminante.data.model.Publication

class DetailViewModel(publication: Publication): ViewModel() {
    private val _publication = MutableLiveData(publication)
    val publication: LiveData<Publication> get() = _publication
}

@Suppress("UNCHECKED_CAST")
class DetailViewModelFactory(private val publication: Publication): ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return DetailViewModel(publication) as T
    }

}