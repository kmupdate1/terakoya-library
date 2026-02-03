package terakoyalabo.core.domain.primitive.model

enum class Signum {
    POSITIVE, NEGATIVE, NEUTRAL;

    companion object {
        /**
         * 具象（Double）から理（Signum）を抽出する。
         * これこそが Signum 関数の真髄。
         */
        fun of(scalar: ScalarD): Signum = when {
            scalar.isPositive -> POSITIVE
            scalar.isNegative -> NEGATIVE
            else -> NEUTRAL
        }
    }

    /**
     * 物理演算時の係数。
     * これにより、理学的な「向き」を実数演算へと橋渡しする。
     */
    val multiplier: ScalarD
        get() = when (this) {
            POSITIVE -> ScalarD.ONE
            NEGATIVE -> ScalarD.ONE.invert()
            NEUTRAL -> ScalarD.ZERO
        }

    /**
     * 作用反作用の法則（極性の反転）。
     * ロードバイクの「向かい風」や、ミツバチの「備蓄の消費」を記述する際に使用。
     */
    fun opposite(): Signum = when (this) {
        POSITIVE -> NEGATIVE
        NEGATIVE -> POSITIVE
        NEUTRAL -> NEUTRAL
    }
}
