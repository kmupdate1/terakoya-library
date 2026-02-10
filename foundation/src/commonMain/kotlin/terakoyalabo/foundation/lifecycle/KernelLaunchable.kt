package terakoyalabo.foundation.lifecycle

interface KernelLaunchable {
    fun onLaunch(): Result<Unit>
}
