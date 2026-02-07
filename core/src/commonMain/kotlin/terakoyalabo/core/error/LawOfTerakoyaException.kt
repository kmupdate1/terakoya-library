package terakoyalabo.core.error

open class LawOfTerakoyaException(
    override val message: String,
    override val cause: Throwable? = null
) : TerakoyaException(message = message, cause = cause)
