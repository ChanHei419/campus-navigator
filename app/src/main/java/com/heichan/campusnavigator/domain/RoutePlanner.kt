package com.heichan.campusnavigator.domain

import java.util.PriorityQueue

/** A weighted, undirected walkway between two places. */
data class Edge(val from: String, val to: String, val metres: Int)

/** A planned walking route. */
data class Route(
    val from: String,
    val to: String,
    val distanceMetres: Int,
    val path: List<String>,
)

/**
 * Dijkstra's shortest path over the campus walkway graph.
 *
 * Pure Kotlin with no Android dependencies so it can be unit-tested on the JVM.
 */
object RoutePlanner {

    fun shortestRoute(edges: List<Edge>, from: String, to: String): Route? {
        val graph = buildGraph(edges)
        if (!graph.containsKey(from) || !graph.containsKey(to)) return null

        val distances = mutableMapOf<String, Int>().withDefault { Int.MAX_VALUE }
        val previous = mutableMapOf<String, String>()
        val visited = mutableSetOf<String>()
        val queue = PriorityQueue<Pair<String, Int>>(compareBy { it.second })

        distances[from] = 0
        queue.add(from to 0)

        while (queue.isNotEmpty()) {
            val (node, distance) = queue.poll()
            if (!visited.add(node)) continue
            if (node == to) break

            for ((neighbour, weight) in graph[node].orEmpty()) {
                val candidate = distance + weight
                if (candidate < distances.getValue(neighbour)) {
                    distances[neighbour] = candidate
                    previous[neighbour] = node
                    queue.add(neighbour to candidate)
                }
            }
        }

        val total = distances.getValue(to)
        if (total == Int.MAX_VALUE) return null

        val path = ArrayDeque<String>()
        var current: String? = to
        while (current != null) {
            path.addFirst(current)
            current = previous[current]
        }

        return Route(
            from = from,
            to = to,
            distanceMetres = total,
            path = path.toList(),
        )
    }

    private fun buildGraph(edges: List<Edge>): Map<String, List<Pair<String, Int>>> {
        val graph = mutableMapOf<String, MutableList<Pair<String, Int>>>()
        for (edge in edges) {
            graph.getOrPut(edge.from) { mutableListOf() }.add(edge.to to edge.metres)
            graph.getOrPut(edge.to) { mutableListOf() }.add(edge.from to edge.metres)
        }
        return graph
    }
}
