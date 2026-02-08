package terakoyalabo.lifecycle.node

interface ResourceVerifiable {
    fun verify(): Result<Unit>
}
