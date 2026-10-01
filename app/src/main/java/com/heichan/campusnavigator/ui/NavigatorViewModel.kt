package com.heichan.campusnavigator.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.heichan.campusnavigator.data.CampusRepository
import com.heichan.campusnavigator.domain.Edge
import com.heichan.campusnavigator.domain.Route
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class NavigatorUiState(
    val places: List<String> = emptyList(),
    val from: String? = null,
    val to: String? = null,
    val route: Route? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
)

class NavigatorViewModel(
    private val repository: CampusRepository = CampusRepository(),
) : ViewModel() {

    // Bundled walkway graph so the app works offline; a real deployment
    // replaces this from the backend.
    private val campusEdges = listOf(
        Edge("Library", "Canteen", 120),
        Edge("Canteen", "Engineering Building", 200),
        Edge("Library", "Engineering Building", 500),
        Edge("Engineering Building", "Sports Centre", 350),
        Edge("Sports Centre", "University Station", 400),
    )

    private val _uiState = MutableStateFlow(NavigatorUiState())
    val uiState: StateFlow<NavigatorUiState> = _uiState

    init {
        refreshPlaces()
    }

    fun refreshPlaces() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                val places = repository.loadPlaces()
                _uiState.update {
                    it.copy(
                        places = places.map { place -> place.name },
                        isLoading = false,
                    )
                }
            } catch (error: Exception) {
                // Offline fallback keeps the app usable with the bundled graph.
                val fallback = campusEdges
                    .flatMap { edge -> listOf(edge.from, edge.to) }
                    .distinct()
                    .sorted()
                _uiState.update {
                    it.copy(places = fallback, isLoading = false, error = null)
                }
            }
        }
    }

    fun selectFrom(place: String) = _uiState.update { it.copy(from = place) }

    fun selectTo(place: String) = _uiState.update { it.copy(to = place) }

    fun findRoute() {
        val state = _uiState.value
        val from = state.from ?: return
        val to = state.to ?: return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null, route = null) }
            try {
                val route = repository.planRoute(from, to, campusEdges)
                _uiState.update {
                    it.copy(
                        route = route,
                        isLoading = false,
                        error = if (route == null) "No walkway connects these places." else null,
                    )
                }
            } catch (error: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = error.message ?: "Route lookup failed",
                    )
                }
            }
        }
    }
}
