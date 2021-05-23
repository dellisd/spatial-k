package io.github.dellisd.spatialk.turf

import io.github.dellisd.spatialk.geojson.FeatureCollection.Companion.toFeatureCollection
import io.github.dellisd.spatialk.geojson.LineString
import io.github.dellisd.spatialk.geojson.Position
import io.github.dellisd.spatialk.geojson.MultiLineString
import io.github.dellisd.spatialk.geojson.Point
import io.github.dellisd.spatialk.geojson.toGeometry
import io.github.dellisd.spatialk.turf.utils.assertDoubleEquals
import io.github.dellisd.spatialk.turf.utils.assertPositionEquals
import io.github.dellisd.spatialk.turf.utils.readResource
import kotlin.test.Test
import kotlin.test.assertEquals

@Suppress("MagicNumber")
class TurfMiscTest {

    @Test
    fun testLineIntersect() {
        val features = readResource("misc/lineIntersect/twoPoints.json").toFeatureCollection()
        val intersect =
            lineIntersect(features.features[0].geometry as LineString, features.features[1].geometry as LineString)

        assertEquals(Position(-120.93653884065287, 51.287945374086675), intersect[0])
    }

    @Test
    fun testLineSlice() {
        val features = readResource("misc/lineSlice/route.json").toFeatureCollection()
        val slice = readResource("misc/lineSlice/slice.json").toGeometry<LineString>()

        val (lineString, start, stop) = features.features

        val result = lineSlice(
            (start.geometry as Point).coordinates,
            (stop.geometry as Point).coordinates,
            lineString.geometry as LineString
        )
        slice.coordinates.forEachIndexed { i, position ->
            assertPositionEquals(position, result.coordinates[i])
        }
    }

    @Test
    fun testNearestPointOnLine() {
        val (multiLine, point) = readResource("misc/nearestPointOnLine/multiLine.json").toFeatureCollection().features

        val result = nearestPointOnLine(multiLine.geometry as MultiLineString, (point.geometry as Point).coordinates)
        assertDoubleEquals(123.924613, result.point.longitude, 0.00001)
        assertDoubleEquals(-19.025117, result.point.latitude, 0.00001)
        assertDoubleEquals(120.886021, result.distance, 0.00001)
        assertDoubleEquals(214.548785, result.location, 0.00001)
        assertEquals(0, result.index)
    }
}
