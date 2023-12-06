package be.swsb.aoc2023

interface Node {
    fun isStart(): Boolean
    fun isEnd(): Boolean
}
typealias Path = List<Node>
typealias PathBuilder = MutableList<Node>

data class Edge<T : Node>(val first: T, val second: T) {
    fun toList() = listOf(first, second)
    fun flipped() = Edge(second, first)
}

class Graph<T: Node>(val edgeList: List<Edge<T>>) {

    val startNode: T = edgeList.first { it.first.isStart() }.first
    val endNode: T = edgeList.first { it.first.isEnd() }.first

    private val uniqueNodes = edgeList.flatMap { edge -> edge.toList() }.toSet()
    private val adjacencyList: Map<Node, List<Edge<T>>> by lazy {
        val directedEdges = edgeList.groupBy { edge -> edge.first }
        val reversedEdges = edgeList.groupBy({ edge -> edge.second }) { it.flipped() }
        val nodes: Set<Node> = directedEdges.keys + reversedEdges.keys
        val mergedEdges = nodes
            .associateWith { node ->
                directedEdges.getOrDefault(node, emptyList()) + reversedEdges.getOrDefault(node, emptyList())
            }
        mergedEdges
    }

    private val visited: MutableMap<Node, Boolean>
        get() = uniqueNodes.associateWith { false }.toMutableMap()

    private fun neighboursOf(node: Node) = adjacencyList[node]?.map { it.second } ?: emptyList()

    fun findAllPaths(from: Node = startNode, to: Node = endNode): List<Path> {
        return dfs(from, to, visited, path = mutableListOf(from), paths = mutableListOf())
    }

    /** Depth First Search **/
    private fun dfs(
        from: Node,
        to: Node,
        visited: MutableMap<Node, Boolean>,
        path: PathBuilder = mutableListOf(),
        paths: MutableList<Path>
    ): MutableList<Path> {
        if (from == to) {
            paths += path.toList()
            return paths
        }
        visited[from] = true
        neighboursOf(from).forEach { node ->
            if (!visited.getValue(node)) {
                path += node
                dfs(node, to, visited, path, paths)
                path.removeLast()
            }
        }
        visited[from] = false
        return paths
    }

    private fun MutableMap<Node, Int>.visit(from: Node) {
        this[from] = this[from]!! + 1
    }

    private fun MutableMap<Node, Int>.unvisit(from: Node) {
        this[from] = this[from]!! - 1
    }

    private fun MutableMap<Node, Int>.reset(from: Node) {
        this[from] = 0
    }
}
