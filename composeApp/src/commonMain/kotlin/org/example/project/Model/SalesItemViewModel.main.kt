package org.example.project.Model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.salesapp.model.SaleItem
import com.example.salesapp.repository.ItemsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class SalesUiState(
    val items: List<SaleItem> = emptyList(),
    val errorMessage: String? = null,
    val isLoading: Boolean = false,
    val lastLoadedItems: List<SaleItem> = emptyList()
)

class SaleItemViewModel(
    private val repository: ItemsRepository // Koin injicerer denne
) : ViewModel() {

    private val _state = MutableStateFlow(SalesUiState(isLoading = true))
    val state: StateFlow<SalesUiState> = _state

    init { loadItems() }

    fun loadItems() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, errorMessage = null) }
            repository.getSalesItems()
                .onSuccess { items ->
                    _state.update { it.copy(items = items, lastLoadedItems = items, isLoading = false) }
                }
                .onFailure { error ->
                    _state.update { it.copy(errorMessage = error.message, isLoading = false) }
                }
        }
    }

    fun add(item: SaleItem) { viewModelScope.launch { repository.add(item).onSuccess { loadItems() } } }
    fun delete(item: SaleItem) { viewModelScope.launch { repository.delete(item.id).onSuccess { loadItems() } } }

    fun sortItemByPrice(ascending: Boolean) {
        val sortedList = if (ascending) { _state.value.items.sortedBy { it.price } }
        else { _state.value.items.sortedByDescending { it.price } }
        _state.update { it.copy(items = sortedList) }
    }

    fun sortItemByDateTime(ascending: Boolean) {
        val sortedList = if (ascending) { _state.value.items.sortedBy { it.time } }
        else { _state.value.items.sortedByDescending { it.time } }
        _state.update { it.copy(items = sortedList) }
    }

    fun filterItemByDescription(descriptionFragment: String) {
        val filteredList = if (descriptionFragment.isBlank()) _state.value.lastLoadedItems else _state.value.lastLoadedItems.filter {
            it.description.contains(descriptionFragment, ignoreCase = true)
        }
        _state.update { it.copy(items = filteredList) }
    }

    fun filterItemByPrice(maxPrice: Int?) {
        val filteredList = if (maxPrice == null) {
            _state.value.lastLoadedItems
        } else {
            _state.value.lastLoadedItems.filter { it.price <= maxPrice }
        }
        _state.update { it.copy(items = filteredList) }
    }
}

