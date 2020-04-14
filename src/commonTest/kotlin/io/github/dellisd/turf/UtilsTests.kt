package io.github.dellisd.turf

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith


class UtilsTests {

    @Test
    fun testRadiansToLength() {
        assertEquals(1.0, radiansToLength(1.0, Units.Radians))
        assertEquals(EARTH_RADIUS / 1000, radiansToLength(1.0, Units.Kilometers))
        assertEquals(EARTH_RADIUS / 1609.344, radiansToLength(1.0, Units.Miles))

        assertFailsWith<IllegalArgumentException> {
            radiansToLength(1.0, Units.Acres)
        }
    }

}