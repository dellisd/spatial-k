package io.github.dellisd.spatialk.turf.utils

import io.github.dellisd.spatialk.geojson.Position
import kotlin.math.abs
import kotlin.test.asserter

fun assertDoubleEquals(expected: Double, actual: Double?, epsilon: Double, message: String? = null) {
    asserter.assertNotNull(null, actual)
    asserter.assertTrue(
        { (message ?: "") + "Expected <$expected>, actual <$actual>, should differ no more than <$epsilon>." },
        abs(expected - actual!!) <= epsilon
    )
}

fun assertPositionEquals(expected: Position, actual: Position?, epsilon: Double = 0.0001, message: String? = null) {
    asserter.assertNotNull(null, actual)

    assertDoubleEquals(expected.latitude, actual?.latitude, epsilon, message)
    assertDoubleEquals(expected.longitude, actual?.longitude, epsilon, message)
}
