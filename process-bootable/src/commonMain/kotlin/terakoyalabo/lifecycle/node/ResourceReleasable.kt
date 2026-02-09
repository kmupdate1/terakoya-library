package terakoyalabo.lifecycle.node

interface ResourceReleasable {
    fun releaseResources(): Result<Unit>
}
