package com.example.viewmodel

import androidx.lifecycle.ViewModel
import com.example.data.MockData
import com.example.models.LiveCategory
import com.example.models.LiveChannel
import com.example.models.VodItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class AppViewModel : ViewModel() {
    private val _favorites = MutableStateFlow<Set<String>>(emptySet())
    val favorites: StateFlow<Set<String>> = _favorites.asStateFlow()
    
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    fun toggleFavorite(id: String) {
        _favorites.update { current ->
            if (current.contains(id)) current - id else current + id
        }
    }

    fun isFavorite(id: String): Boolean = _favorites.value.contains(id)

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun getSearchResults(query: String): List<VodItem> {
        if (query.isBlank()) return emptyList()
        return MockData.vodContent.filter { 
            it.title.contains(query, ignoreCase = true) || it.category.contains(query, ignoreCase = true) 
        }
    }

    fun getFavoriteItems(): List<VodItem> {
        return MockData.vodContent.filter { _favorites.value.contains(it.id) }
    }
}
