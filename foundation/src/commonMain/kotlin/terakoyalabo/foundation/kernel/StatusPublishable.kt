package terakoyalabo.foundation.kernel

interface StatusPublishable {
    fun publish(): NodeStatus

    enum class NodeStatus {
        STARTING, ALIVE, RETIRING, DEAD;
    }
}
