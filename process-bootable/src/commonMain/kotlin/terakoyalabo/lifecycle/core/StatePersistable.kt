package terakoyalabo.lifecycle.core

interface StatePersistable {
    fun persist(): Result<Unit>
}
