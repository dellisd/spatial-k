package io.github.dellisd.spatialk.turf.utils

import kotlin.math.abs
import kotlin.test.asserter

fun assertDoubleEquals(expected: Double, actual: Double?, epsilon: Double, message: String? = null) {
    asserter.assertNotNull(null, actual)
    asserter.assertTrue(
        { (message ?: "") + "Expected <$expected>, actual <$actual>, should differ no more than <$epsilon>." },
        abs(expected - actual!!) <= epsilon
    )
}
