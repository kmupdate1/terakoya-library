package terakoyalabo.foundation.lifecycle

interface Endurable {
    fun onEndue(): Result<Unit>
}
