package terakoyalabo.lifecycle.node

interface StatePersistant {
    fun persist(): Result<Unit>
}
