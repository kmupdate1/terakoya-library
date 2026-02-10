package terakoyalabo.foundation.lifecycle

interface ResourceReleasable {
    fun onRelease(): Result<Unit>
}
