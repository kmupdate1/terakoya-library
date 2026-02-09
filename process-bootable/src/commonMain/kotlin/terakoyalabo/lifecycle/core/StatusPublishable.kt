package terakoyalabo.lifecycle.core

interface StatusPublishable {
    fun publish(): NodeStatus

    enum class NodeStatus {
        STARTING, ALIVE, RETIRING, DEAD;
    }
}
