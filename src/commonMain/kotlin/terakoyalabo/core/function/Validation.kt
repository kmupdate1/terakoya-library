package terakoyalabo.core.function

import terakoyalabo.core.error.InvalidValidationException
import kotlin.jvm.JvmName

@Throws(InvalidValidationException::class)
@JvmName("validateRequirement")
internal inline fun <T> T.validate(requirement: (T) -> Boolean, lazyMessage: (T) -> String): T {
    if (!requirement.invoke(this)) throw InvalidValidationException(message = lazyMessage.invoke(this))

    return this
}

@Throws(InvalidValidationException::class)
@JvmName("validateRun")
internal inline fun <T, R> T.validate(run: (T) -> R, lazyMessage: (T) -> String?): R =
    try { run.invoke(this) } catch (e: Exception) {
        val prefix = lazyMessage.invoke(this)?.let { "$it " }
            ?: e::class.simpleName
            ?: "Unknown"

        throw InvalidValidationException(message = prefix + "[${e.message}]", cause = e)
    }
