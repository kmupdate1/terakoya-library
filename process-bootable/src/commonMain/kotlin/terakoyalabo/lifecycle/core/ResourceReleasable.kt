package terakoyalabo.lifecycle.core

interface ResourceReleasable {
    fun onRelease(): Result<Unit>
}
