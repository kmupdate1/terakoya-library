package terakoyalabo.core.domain.identity

internal actual fun platformHash(input: String): ByteArray {
    // JVMの標準的な MessageDigest (SHA-1) を使用
    val md = java.security.MessageDigest.getInstance("SHA-1")
    val fullHash = md.digest(input.encodeToByteArray())
    return fullHash.copyOf(16) // UUIDは16バイトなので切り出す
}
