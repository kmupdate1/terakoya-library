package terakoyalabo.lifecycle.node

interface ResourceVerifiable {
    fun verifyResources(): Result<Unit>
}
