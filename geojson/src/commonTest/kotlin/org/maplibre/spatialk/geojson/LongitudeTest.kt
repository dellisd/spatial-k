package org.maplibre.spatialk.geojson

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertSame
import kotlin.test.assertTrue
import org.maplibre.spatialk.testutil.assertDoubleEquals

class LongitudeTest {
    @Test
    fun outOfRangeCoordinatesRoundTripUnchanged() {
        val position = Position(550.25, 100.5, 12.5)
        assertEquals("[550.25,100.5,12.5]", position.toJson())
        assertCoordinates(position, Position.fromJson(position.toJson()))
        val negative = Position(-550.25, -100.5)
        assertCoordinates(negative, Position.fromJson(negative.toJson()))

        val box = BoundingBox(Position(170.25, 100.5, 20.5), Position(-170.25, -100.5, 10.5))
        assertEquals("[170.25,100.5,20.5,-170.25,-100.5,10.5]", box.toJson())
        assertCoordinates(box, BoundingBox.fromJson(box.toJson()))
        val continuous = BoundingBox(170.0, -10.0, 550.0, 10.0)
        assertCoordinates(continuous, BoundingBox.fromJson(continuous.toJson()))
    }

    @Test
    fun wrappedNormalizesLongitudeAndIsIdempotent() {
        val cases =
            listOf(
                -900.0 to -180.0,
                -540.0 to -180.0,
                -190.25 to 169.75,
                -180.0 to -180.0,
                -75.125 to -75.125,
                0.0 to 0.0,
                180.0 to -180.0,
                190.25 to -169.75,
                540.0 to -180.0,
                900.0 to -180.0,
            )
        for ((longitude, expected) in cases) {
            val position = Position(longitude, 100.5, 12.5)
            val wrapped = position.wrapped()
            assertCoordinates(Position(expected, 100.5, 12.5), wrapped)
            assertCoordinates(wrapped, wrapped.wrapped())
            assertDoubleEquals(longitude, position.longitude)
        }
        assertCoordinates(Position(-170.0, 10.0), Position(190.0, 10.0).wrapped())
    }

    @Test
    fun splitPreservesNonCrossingIdentityIncludingEdges() {
        for ((west, east) in
            listOf(
                -75.0 to 45.0,
                -170.0 to 170.0,
                170.0 to 180.0,
                -180.0 to -170.0,
                -180.0 to 180.0,
                180.0 to 180.0,
                -180.0 to -180.0,
            )) {
            val box = BoundingBox(west, -10.0, east, 10.0)
            assertSame(box, box.splitAtAntimeridian().single())
        }
    }

    @Test
    fun splitAcceptsRfcAndContinuousCrossingsInEitherWorldCopy() {
        for ((west, east) in
            listOf(
                170.0 to -170.0,
                170.0 to 190.0,
                -190.0 to -170.0,
                530.0 to 550.0,
                -550.0 to -530.0,
            )) {
            val box = BoundingBox(Position(west, -10.0, 12.5), Position(east, 10.0, 25.5))
            val parts = box.splitAtAntimeridian()
            assertEquals(2, parts.size)
            assertCoordinates(BoundingBox(170.0, -10.0, 12.5, 180.0, 10.0, 25.5), parts[0])
            assertCoordinates(BoundingBox(-180.0, -10.0, 12.5, -170.0, 10.0, 25.5), parts[1])
        }
    }

    @Test
    fun splitFullWorldSpansPreservesOtherBounds() {
        for ((west, east) in
            listOf(
                0.0 to 360.0,
                170.0 to 530.0,
                -200.0 to 200.0,
                -540.0 to 540.0,
                -Double.MAX_VALUE to Double.MAX_VALUE,
            )) {
            val box = BoundingBox(Position(west, -100.0, 12.5), Position(east, 100.0, 25.5))
            assertCoordinates(
                BoundingBox(-180.0, -100.0, 12.5, 180.0, 100.0, 25.5),
                box.splitAtAntimeridian().single(),
            )
        }
    }

    @Test
    fun splitNormalizesOtherWorldCopiesAndAvoidsEmptyEdgePieces() {
        val cases =
            listOf(
                (190.0 to 200.0) to (-170.0 to -160.0),
                (-200.0 to -190.0) to (160.0 to 170.0),
                (170.0 to -180.0) to (170.0 to 180.0),
                (180.0 to -170.0) to (-180.0 to -170.0),
                (180.0 to 190.0) to (-180.0 to -170.0),
                (-190.0 to -180.0) to (170.0 to 180.0),
                (180.0 to -180.0) to (-180.0 to -180.0),
                (540.0 to 540.0) to (-180.0 to -180.0),
                (550.0 to 190.0) to (-170.0 to -170.0),
            )
        for ((input, expected) in cases) {
            val box = BoundingBox(input.first, -10.0, input.second, 10.0)
            assertCoordinates(
                BoundingBox(expected.first, -10.0, expected.second, 10.0),
                box.splitAtAntimeridian().single(),
            )
        }
    }

    @Test
    @OptIn(SensitiveGeoJsonApi::class)
    fun additionalAxesArePreserved() {
        val west = Position(170.0, -10.0, 12.5, 42.0)
        val east = Position(190.0, 10.0, 25.5, 84.0)
        assertCoordinates(Position(-170.0, 10.0, 25.5, 84.0), east.wrapped())
        val boxes =
            listOf(
                BoundingBox(west, east),
                BoundingBox(170.0, -10.0, 12.5, 190.0, 10.0, 25.5, 42.0, 84.0),
            )
        for (box in boxes) {
            val parts = box.splitAtAntimeridian()
            assertEquals(2, parts.size)
            assertCoordinates(BoundingBox(west, Position(180.0, 10.0, 25.5, 84.0)), parts[0])
            assertCoordinates(
                BoundingBox(
                    Position(-180.0, -10.0, 12.5, 42.0),
                    Position(-170.0, 10.0, 25.5, 84.0),
                ),
                parts[1],
            )
        }
    }

    @Test
    fun nonFiniteLongitudes() {
        for (longitude in listOf(Double.NaN, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY)) {
            assertTrue(Position(longitude, 10.0).wrapped().longitude.isNaN())
            val box = BoundingBox(longitude, -10.0, longitude, 10.0)
            assertFailsWith<IllegalArgumentException> { box.splitAtAntimeridian() }
            assertFailsWith<IllegalArgumentException> {
                BoundingBox(0.0, -10.0, longitude, 10.0).splitAtAntimeridian()
            }
        }
    }

    private fun assertCoordinates(expected: Iterable<Double>, actual: Iterable<Double>) {
        assertEquals(expected.count(), actual.count())
        expected.zip(actual).forEach { (expectedValue, actualValue) ->
            assertDoubleEquals(expectedValue, actualValue)
        }
    }
}
