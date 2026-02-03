package terakoyalabo.core.domain.logic

import terakoyalabo.core.domain.primitive.model.ScalarD
import terakoyalabo.core.domain.primitive.model.Signum
import terakoyalabo.core.domain.logic.model.TargetPoint
import terakoyalabo.core.domain.logic.model.Threshold
import terakoyalabo.core.error.LawOfTerakoyaException

/**
 * 閾値という「法」に対して、対象が「規律」を守っているかを確認する。
 * 対象が、閾値未満であればtrue、それ以外はfalse。
 * @throws LawOfTerakoyaException
 */
val Threshold.discipline: (ScalarD) -> Boolean get() = { !this.isViolatedBy(current = it) }
@Throws(LawOfTerakoyaException::class)
fun Threshold.disciplineBy(current: ScalarD): Boolean = this.discipline.invoke(current)

/**
 * 境界値を含め、規律を守っているかを確認する。
 * 対象が、閾値以内であればtrue、それ以外はfalse。
 * @throws LawOfTerakoyaException
 */
val Threshold.inclusiveDiscipline: (ScalarD) -> Boolean get() = { !this.isIncludesAndViolatedBy(current = it) }
@Throws(LawOfTerakoyaException::class)
fun Threshold.inclusiveDisciplineBy(current: ScalarD): Boolean = this.inclusiveDiscipline.invoke(current)

val ScalarD.signum: Signum get() = Signum.of(scalar = this)
val ScalarD.asUpperLimit: Threshold get() = Threshold.upper(limit = this)
val ScalarD.asLowerLimit: Threshold get() = Threshold.lower(limit = this)
val ScalarD.asTarget: TargetPoint get() = TargetPoint(at = this)
fun ScalarD.asTargetWithEpsilon(epsilon: ScalarD): TargetPoint = TargetPoint(at = this, epsilon = epsilon)

// --- ScalarD への誘い（理学的な量の抽出） ---
val Int.sVal: ScalarD get() = ScalarD.of(raw = this.toDouble())
val Double.sVal: ScalarD get() = ScalarD.of(raw = this)
