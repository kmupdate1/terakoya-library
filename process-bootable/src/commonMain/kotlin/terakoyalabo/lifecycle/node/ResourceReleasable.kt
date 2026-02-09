package terakoyalabo.lifecycle.node

interface ResourceReleasable {
    fun onRelease(): Result<Unit>
}
