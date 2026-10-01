package com.heichan.campusnavigator.domain

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class RoutePlannerTest {

    private val edges = listOf(
        Edge("Library", "Canteen", 120),
        Edge("Canteen", "Engineering Building", 200),
        Edge("Library", "Engineering Building", 500),
    )

    @Test
    fun `picks the shortest path through the canteen`() {
        val route = RoutePlanner.shortestRoute(edges, "Library", "Engineering Building")

        assertEquals(320, route?.distanceMetres)
        assertEquals(
            listOf("Library", "Canteen", "Engineering Building"),
            route?.path,
        )
    }

    @Test
    fun `returns null for an unknown place`() {
        assertNull(RoutePlanner.shortestRoute(edges, "Library", "Moon"))
    }

    @Test
    fun `returns null when no walkway connects the places`() {
        val disconnected = edges + listOf(Edge("Island", "Pier", 50))
        assertNull(RoutePlanner.shortestRoute(disconnected, "Library", "Island"))
    }

    @Test
    fun `same place has zero distance`() {
        val route = RoutePlanner.shortestRoute(edges, "Library", "Library")
        assertEquals(0, route?.distanceMetres)
        assertEquals(listOf("Library"), route?.path)
    }
}
