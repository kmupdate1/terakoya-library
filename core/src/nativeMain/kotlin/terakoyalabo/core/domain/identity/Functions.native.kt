package terakoyalabo.core.domain.identity

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.reinterpret
import kotlinx.cinterop.usePinned

@OptIn(ExperimentalForeignApi::class)
internal actual fun platformHash(input: String): ByteArray {
    val data = input.encodeToByteArray()
    // SHA-1 のハッシュサイズは 20バイト (CC_SHA1_DIGEST_LENGTH)
    val digest = ByteArray(CC_SHA1_DIGEST_LENGTH)

    digest.usePinned { pinnedDigest ->
        data.usePinned { pinnedData ->
            // Apple の C 関数を直接叩く「プリミティブな探求」
            CC_SHA1(
                pinnedData.addressOf(0),
                data.size.toUInt(),
                pinnedDigest.addressOf(0).reinterpret()
            )
        }
    }

    // UUID (128bit) に必要なのは 16バイト なので切り出す
    return digest.copyOf(16)
}
