package terakoyalabo.foundation.lifecycle

interface StatePersistable {
    fun persist(): Result<Unit>
}
