package dijkstra


    fun <T> calculateShortestPathFromSource(source: Node<T>) {
        source.distance = 0
        val settledNodes = mutableSetOf<Node<T>>()
        val unsettledNodes = mutableSetOf(source)

        while (unsettledNodes.size != 0) {
            val currentNode = getLowestDistanceNode(unsettledNodes)
            unsettledNodes -= currentNode
            for ((adjacentNode, edgeWeigh) in currentNode.adjacentNodes) {
                if (adjacentNode !in settledNodes) {
                    calculateMinimumDistance(adjacentNode, edgeWeigh, currentNode)
                    unsettledNodes += adjacentNode
                }
            }
            settledNodes.add(currentNode)
        }
    }

    private fun <T> calculateMinimumDistance(evaluationNode: Node<T>, edgeWeigh: Int, sourceNode: Node<T>) {
        val sourceDistance = sourceNode.distance
        if (sourceDistance + edgeWeigh < evaluationNode.distance) {
            evaluationNode.distance = sourceDistance + edgeWeigh
            val shortestPath = sourceNode.shortestPath.toMutableList()
            shortestPath += sourceNode
            evaluationNode.shortestPath = shortestPath
        }
    }

    private fun <T> getLowestDistanceNode(unsettledNodes: Set<Node<T>>): Node<T> {
        var lowestDistanceNode: Node<T>? = null
        var lowestDistance = Int.MAX_VALUE
        for (node in unsettledNodes) {
            val nodeDistance = node.distance
            if (nodeDistance < lowestDistance) {
                lowestDistance = nodeDistance
                lowestDistanceNode = node
            }
        }
        return lowestDistanceNode!!
    }
