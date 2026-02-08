package terakoyalabo.lifecycle.node

interface StatusPublishable {
    fun publish(): NodeStatus

    enum class NodeStatus {
        STARTING, ALIVE, RETIRING, DEAD;
    }
}
