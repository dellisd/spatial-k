package io.github.dellisd.geojson

import kotlin.test.Test
import kotlin.test.assertEquals

class GeometryTests {

    @Test
    fun testPointEquality() {
        val point = Point(LngLat(-45.0, 75.0))
        val point2 = Point(LngLat(-45.0, 75.0))

        assertEquals(point, point2)
    }

    @Test
    fun testMultiPointEquality() {
        val multiPoint = MultiPoint(LngLat(12.3, 45.6), LngLat(78.9, 12.3))
        val multiPoint2 = MultiPoint(LngLat(12.3, 45.6), LngLat(78.9, 12.3))

        assertEquals(multiPoint, multiPoint2)
    }
}