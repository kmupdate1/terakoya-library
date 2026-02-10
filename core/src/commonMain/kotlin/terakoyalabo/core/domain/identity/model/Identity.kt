package terakoyalabo.core.domain.identity.model

import terakoyalabo.core.domain.identity.platformHash
import terakoyalabo.core.error.InvalidValidationException
import terakoyalabo.core.function.validate
import kotlin.jvm.JvmInline
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@JvmInline
value class Identity @OptIn(ExperimentalUuidApi::class)
@Throws(InvalidValidationException::class)
private constructor(val id: Uuid) {
    companion object {
        @OptIn(ExperimentalUuidApi::class)
        fun fromString(id: String): Identity {
            val validId = id.validate(
                run = { Uuid.parse(it) },
                lazyMessage = { "Failed to generate identity (input: $it)." },
            )

            return Identity(id = validId)
        }

        @OptIn(ExperimentalUuidApi::class)
        fun genV4(): Identity = Identity(Uuid.random())

        /**
         * 【静的】UUID v3/v5 相当 (名前ベース)
         * 文字列から決定論的に生成される。同じ名前からは常に同じIDが生まれる。
         * 例：FeatureのID、特定の永続的なリソース、ハードウェア固有ID
         */
        @OptIn(ExperimentalUuidApi::class)
        fun genV5(name: String): Identity {
            // 文字列をバイト列に変換し、プラットフォームごとのハッシュ関数で16バイトに落とし込む
            val hash = platformHash(name)
            return Identity(Uuid.fromByteArray(hash))
        }
    }

    /**
     * 1. 「完全な真名」
     * 例: 550e8400-e29b-41d4-a716-446655440000
     */
    val full: String @OptIn(ExperimentalUuidApi::class) get() = id.toString()

    /**
     * 2. 「短縮名称（ラベル）」
     * ログやデバッグ時に、パッと見で個体を識別するために使う。
     * 例: 550e8400 (最初の8文字)
     */
    val short8: String @OptIn(ExperimentalUuidApi::class) get() = full.take(8)

    /**
     * 3. 「圧縮された識別子」
     * ハイフンを除去したもの。DBのキーやURLパス、ファイル名に使いやすい。
     * 例: 550e8400e29b41d4a716446655440000
     */
    val compact: String @OptIn(ExperimentalUuidApi::class) get() = full.replace("-", "")

    @OptIn(ExperimentalUuidApi::class)
    override fun toString(): String = full
}
