package com.heichan.campusnavigator.data

import com.heichan.campusnavigator.data.remote.ApiClient
import com.heichan.campusnavigator.data.remote.CampusApi
import com.heichan.campusnavigator.data.remote.dto.PlaceDto
import com.heichan.campusnavigator.domain.Edge
import com.heichan.campusnavigator.domain.Route
import com.heichan.campusnavigator.domain.RoutePlanner

class CampusRepository(private val api: CampusApi = ApiClient.campusApi) {

    suspend fun loadPlaces(): List<PlaceDto> = api.getPlaces()

    /** Plan locally with Dijkstra; fall back to the server when offline. */
    suspend fun planRoute(from: String, to: String, edges: List<Edge>): Route? {
        val local = RoutePlanner.shortestRoute(edges, from, to)
        if (local != null) return local

        val remote = api.getRoute(from, to)
        return Route(
            from = from,
            to = to,
            distanceMetres = remote.distanceMetres,
            path = remote.path,
        )
    }
}
