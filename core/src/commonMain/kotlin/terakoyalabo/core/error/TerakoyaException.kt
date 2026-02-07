package terakoyalabo.core.error

abstract class TerakoyaException(
    override val message: String,
    override val cause: Throwable?,
) : RuntimeException(message, cause)
