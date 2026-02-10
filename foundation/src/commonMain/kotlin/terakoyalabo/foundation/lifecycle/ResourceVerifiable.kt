package terakoyalabo.foundation.lifecycle

interface ResourceVerifiable {
    fun onVerify(): Result<Unit>
}
