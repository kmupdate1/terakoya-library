package terakoyalabo.core.error

abstract class CoreDomainException(
    override val message: String,
    override val cause: Throwable?,
) : RuntimeException(message, cause)
