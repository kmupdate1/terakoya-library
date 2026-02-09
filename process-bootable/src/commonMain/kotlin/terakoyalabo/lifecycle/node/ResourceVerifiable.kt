package terakoyalabo.lifecycle.node

interface ResourceVerifiable {
    fun onVerify(): Result<Unit>
}
