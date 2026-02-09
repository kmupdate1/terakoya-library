package terakoyalabo.lifecycle.core

interface ResourceVerifiable {
    fun onVerify(): Result<Unit>
}
