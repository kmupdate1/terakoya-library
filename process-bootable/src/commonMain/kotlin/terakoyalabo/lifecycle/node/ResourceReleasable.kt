package terakoyalabo.lifecycle.node

interface ResourceReleasable {
    fun release(): Result<Unit>
}
