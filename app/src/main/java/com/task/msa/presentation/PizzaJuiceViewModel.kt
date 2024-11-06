package com.task.msa.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.task.msa.data.PizzaJuiceRepository
import com.task.msa.data.Place
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ViewModelFactory(
    private val repository: PizzaJuiceRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(PizzaJuiceViewModel::class.java) -> {
                PizzaJuiceViewModel(repository) as T
            }

            else -> throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}

sealed class ResponseHandler<out T> {
    object Loading : ResponseHandler<Nothing>()
    data class Success<out T>(val data: T) : ResponseHandler<T>()
    data class Error(val exception: Throwable) : ResponseHandler<Nothing>()
}

class PizzaJuiceViewModel(private val repository: PizzaJuiceRepository) : ViewModel() {

    private val _pizzaPlaces = MutableStateFlow<ResponseHandler<List<Place>>>(ResponseHandler.Loading)
    val pizzaPlaces: StateFlow<ResponseHandler<List<Place>>> = _pizzaPlaces

    private val _juicePlaces = MutableStateFlow<ResponseHandler<List<Place>>>(ResponseHandler.Loading)
    val juicePlaces: StateFlow<ResponseHandler<List<Place>>> = _juicePlaces


      fun loadPizzaPlaces() {
        viewModelScope.launch {
            _pizzaPlaces.value = ResponseHandler.Loading
            try {
                val places = repository.getPizzaPlaces( )
                _pizzaPlaces.value = ResponseHandler.Success(places)
            } catch (e: Exception) {
                _pizzaPlaces.value = ResponseHandler.Error(e)
            }
        }
    }

      fun loadJuicePlaces() {
        viewModelScope.launch {
            _juicePlaces.value = ResponseHandler.Loading
            try {
                val places = repository.getJuicePlaces( )
                _juicePlaces.value = ResponseHandler.Success(places)
            } catch (e: Exception) {
                _juicePlaces.value = ResponseHandler.Error(e)
            }
        }
    }
}
