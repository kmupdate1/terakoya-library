package terakoyalabo.auth.error

import terakoyalabo.core.error.LawOfTerakoyaException

class InvalidIdentityTokenException(
    override val message: String,
    override val cause: Throwable? = null
) : LawOfTerakoyaException(message = message, cause = cause)
