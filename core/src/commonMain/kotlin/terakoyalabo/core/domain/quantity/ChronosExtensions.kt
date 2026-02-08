package terakoyalabo.core.domain.quantity

import terakoyalabo.core.domain.logic.sd
import terakoyalabo.core.domain.primitive.model.ScalarD
import terakoyalabo.core.domain.quantity.model.ChronosMs

// ミリ秒：最小単位そのまま
val Int.ms: ChronosMs get() = ChronosMs.of(this.sd)
val Double.ms: ChronosMs get() = ChronosMs.of(this.sVal)

// 秒：量に KILO (1000) を掛けてミリ秒へ
val Int.s: ChronosMs get() = ChronosMs.of(this.sd * ScalarD.KILO)
val Double.s: ChronosMs get() = ChronosMs.of(this.sVal * ScalarD.KILO)

// 分：秒に SEXA (60) を掛けてミリ秒へ
val Int.min: ChronosMs get() = ChronosMs.of(this.sd * ScalarD.KILO * ScalarD.SEXA)
val Double.min: ChronosMs get() = ChronosMs.of(this.sVal * ScalarD.KILO * ScalarD.SEXA)

// 時：分に SEXA (60) を掛けてミリ秒へ
val Int.hour: ChronosMs get() = ChronosMs.of(this.sd * ScalarD.KILO * ScalarD.SEXA * ScalarD.SEXA)
val Double.hour: ChronosMs get() = ChronosMs.of(this.sVal * ScalarD.KILO * ScalarD.SEXA * ScalarD.SEXA)
