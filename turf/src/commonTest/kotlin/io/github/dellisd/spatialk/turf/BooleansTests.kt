package io.github.dellisd.spatialk.turf

import io.github.dellisd.spatialk.geojson.Feature.Companion.toFeature
import io.github.dellisd.spatialk.geojson.MultiPolygon
import io.github.dellisd.spatialk.geojson.Point
import io.github.dellisd.spatialk.geojson.Polygon
import io.github.dellisd.spatialk.turf.utils.readResource
import kotlinx.serialization.ExperimentalSerializationApi
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@ExperimentalSerializationApi
class BooleansTests {

    @Test
    fun testFeatureCollection() {
        // test for a simple polygon
        val poly = Polygon(arrayOf(
            arrayOf(
                doubleArrayOf(0.0, 0.0),
                doubleArrayOf(0.0, 100.0),
                doubleArrayOf(100.0, 0.0),
                doubleArrayOf(0.0, 0.0)
            )
        ))
        val ptIn = Point(doubleArrayOf(50.0, 50.0))
        val ptOut = Point(doubleArrayOf(140.0, 150.0))

        assertTrue(booleanPointInPolygon(ptIn, poly), "point inside simple polygon")
        assertFalse(booleanPointInPolygon(ptOut, poly), "point outside simple polygon")

        // test for a concave polygon
        val concavePoly = Polygon(arrayOf(
            arrayOf(
                doubleArrayOf(0.0, 0.0),
                doubleArrayOf(50.0, 50.0),
                doubleArrayOf(0.0, 100.0),
                doubleArrayOf(100.0, 100.0),
                doubleArrayOf(100.0, 0.0),
                doubleArrayOf(0.0, 0.0)
            )
        ))
        val ptConcaveIn = Point(doubleArrayOf(75.0, 75.0))
        val ptConcaveOut = Point(doubleArrayOf(25.0, 50.0))

        assertTrue(
            booleanPointInPolygon(ptConcaveIn, concavePoly),
            "point inside concave polygon"
        )
        assertFalse(
            booleanPointInPolygon(ptConcaveOut, concavePoly),
            "point outside concave polygon"
        )
    }

    @Test
    fun testPolyWithHole() {
        val ptInHole = Point(doubleArrayOf(-86.69208526611328, 36.20373274711739))
        val ptInPoly = Point(doubleArrayOf(-86.72229766845702, 36.20258997094334))
        val ptOutsidePoly = Point(doubleArrayOf(-86.75079345703125, 36.18527313913089))
        val polyHole = readResource("booleans/in/poly-with-hole.geojson")
            .toFeature()
            .geometry as Polygon

        assertFalse(booleanPointInPolygon(ptInHole, polyHole))
        assertTrue(booleanPointInPolygon(ptInPoly, polyHole))
        assertFalse(booleanPointInPolygon(ptOutsidePoly, polyHole))
    }

    @Test
    fun testMultipolygonWithHole() {
        val ptInHole = Point(doubleArrayOf(-86.69208526611328, 36.20373274711739))
        val ptInPoly = Point(doubleArrayOf(-86.72229766845702, 36.20258997094334))
        val ptInPoly2 = Point(doubleArrayOf(-86.75079345703125, 36.18527313913089))
        val ptOutsidePoly = Point(doubleArrayOf(-86.75302505493164, 36.23015046460186))
        val multiPolyHole = readResource("booleans/in/multipoly-with-hole.geojson")
            .toFeature()
            .geometry as MultiPolygon

        assertFalse(booleanPointInPolygon(ptInHole, multiPolyHole))
        assertTrue(booleanPointInPolygon(ptInPoly, multiPolyHole))
        assertTrue(booleanPointInPolygon(ptInPoly2, multiPolyHole))
        assertTrue(booleanPointInPolygon(ptInPoly, multiPolyHole))
        assertFalse(booleanPointInPolygon(ptOutsidePoly, multiPolyHole))
    }

    @Test
    fun testBoundaryTest() {
        val poly1 = Polygon(arrayOf(
            arrayOf(
                doubleArrayOf(10.0, 10.0),
                doubleArrayOf(30.0, 20.0),
                doubleArrayOf(50.0, 10.0),
                doubleArrayOf(30.0, 0.0),
                doubleArrayOf(10.0, 10.0)
            )
        ))
        val poly2 = Polygon(arrayOf(
            arrayOf(
                doubleArrayOf(10.0, 0.0),
                doubleArrayOf(30.0, 20.0),
                doubleArrayOf(50.0, 0.0),
                doubleArrayOf(30.0, 10.0),
                doubleArrayOf(10.0, 0.0)
            )
        ))
        val poly3 = Polygon(arrayOf(
            arrayOf(
                doubleArrayOf(10.0, 0.0),
                doubleArrayOf(30.0, 20.0),
                doubleArrayOf(50.0, 0.0),
                doubleArrayOf(30.0, -20.0),
                doubleArrayOf(10.0, 0.0)
            )
        ))
        val poly4 = Polygon(arrayOf(
            arrayOf(
                doubleArrayOf(0.0, 0.0),
                doubleArrayOf(0.0, 20.0),
                doubleArrayOf(50.0, 20.0),
                doubleArrayOf(50.0, 0.0),
                doubleArrayOf(40.0, 0.0),
                doubleArrayOf(30.0, 10.0),
                doubleArrayOf(30.0, 0.0),
                doubleArrayOf(20.0, 10.0),
                doubleArrayOf(10.0, 10.0),
                doubleArrayOf(10.0, 0.0),
                doubleArrayOf(0.0, 0.0)
            )
        ))
        val poly5 = Polygon(arrayOf(
            arrayOf(
                doubleArrayOf(0.0, 20.0),
                doubleArrayOf(20.0, 40.0),
                doubleArrayOf(40.0, 20.0),
                doubleArrayOf(20.0, 0.0),
                doubleArrayOf(0.0, 20.0)
            ),
            arrayOf(
                doubleArrayOf(10.0, 20.0),
                doubleArrayOf(20.0, 30.0),
                doubleArrayOf(30.0, 20.0),
                doubleArrayOf(20.0, 10.0),
                doubleArrayOf(10.0, 20.0)
            )
        ))
        fun runTest(ignoreBoundary: Boolean) {
            val isBoundaryIncluded = !ignoreBoundary
            val tests = arrayOf(
                Triple(poly1, Point(doubleArrayOf(10.0, 10.0)), isBoundaryIncluded), //0
                Triple(poly1, Point(doubleArrayOf(30.0, 20.0)), isBoundaryIncluded),
                Triple(poly1, Point(doubleArrayOf(50.0, 10.0)), isBoundaryIncluded),
                Triple(poly1, Point(doubleArrayOf(30.0, 10.0)), true),
                Triple(poly1, Point(doubleArrayOf(0.0, 10.0)), false),
                Triple(poly1, Point(doubleArrayOf(60.0, 10.0)), false),
                Triple(poly1, Point(doubleArrayOf(30.0, -10.0)), false),
                Triple(poly1, Point(doubleArrayOf(30.0, 30.0)), false),
                Triple(poly2, Point(doubleArrayOf(30.0, 0.0)), false),
                Triple(poly2, Point(doubleArrayOf(0.0, 0.0)), false),
                Triple(poly2, Point(doubleArrayOf(60.0, 0.0)), false), //10
                Triple(poly3, Point(doubleArrayOf(30.0, 0.0)), true),
                Triple(poly3, Point(doubleArrayOf(0.0, 0.0)), false),
                Triple(poly3, Point(doubleArrayOf(60.0, 0.0)), false),
                Triple(poly4, Point(doubleArrayOf(0.0, 20.0)), isBoundaryIncluded),
                Triple(poly4, Point(doubleArrayOf(10.0, 20.0)), isBoundaryIncluded),
                Triple(poly4, Point(doubleArrayOf(50.0, 20.0)), isBoundaryIncluded),
                Triple(poly4, Point(doubleArrayOf(0.0, 10.0)), isBoundaryIncluded),
                Triple(poly4, Point(doubleArrayOf(5.0, 10.0)), true),
                Triple(poly4, Point(doubleArrayOf(25.0, 10.0)), true),
                Triple(poly4, Point(doubleArrayOf(35.0, 10.0)), true), //20
                Triple(poly4, Point(doubleArrayOf(0.0, 0.0)), isBoundaryIncluded),
                Triple(poly4, Point(doubleArrayOf(20.0, 0.0)), false),
                Triple(poly4, Point(doubleArrayOf(35.0, 0.0)), false),
                Triple(poly4, Point(doubleArrayOf(50.0, 0.0)), isBoundaryIncluded),
                Triple(poly4, Point(doubleArrayOf(50.0, 10.0)), isBoundaryIncluded),
                Triple(poly4, Point(doubleArrayOf(5.0, 0.0)), isBoundaryIncluded),
                Triple(poly4, Point(doubleArrayOf(10.0, 0.0)), isBoundaryIncluded),
                Triple(poly5, Point(doubleArrayOf(20.0, 30.0)), isBoundaryIncluded),
                Triple(poly5, Point(doubleArrayOf(25.0, 25.0)), isBoundaryIncluded),
                Triple(poly5, Point(doubleArrayOf(30.0, 20.0)), isBoundaryIncluded), //30
                Triple(poly5, Point(doubleArrayOf(25.0, 15.0)), isBoundaryIncluded),
                Triple(poly5, Point(doubleArrayOf(20.0, 10.0)), isBoundaryIncluded),
                Triple(poly5, Point(doubleArrayOf(15.0, 15.0)), isBoundaryIncluded),
                Triple(poly5, Point(doubleArrayOf(10.0, 20.0)), isBoundaryIncluded),
                Triple(poly5, Point(doubleArrayOf(15.0, 25.0)), isBoundaryIncluded),
                Triple(poly5, Point(doubleArrayOf(20.0, 20.0)), false),
            )

            val testTitle =
                "Boundary " + (if (ignoreBoundary) "ignored " else "") + "test number "
            tests.forEachIndexed { i, item ->
                assertEquals(
                    booleanPointInPolygon(item.second, item.first, ignoreBoundary),
                    item.third,
                    testTitle + i
                )
            }
        }
        runTest(false)
        runTest(true)
    }

    // https://github.com/Turfjs/turf-inside/issues/15
    @Test
    fun testIssue15() {
        val pt1 = Point(doubleArrayOf(-9.9964077, 53.8040989))
        val poly = Polygon(arrayOf(
            arrayOf(
                doubleArrayOf(5.080336744095521, 67.89398938540765),
                doubleArrayOf(0.35070899909145403, 69.32470003971179),
                doubleArrayOf(-24.453622256504122, 41.146696777884564),
                doubleArrayOf(-21.6445524714804, 40.43225902006474),
                doubleArrayOf(5.080336744095521, 67.89398938540765)
            )
        ))

        assertTrue(booleanPointInPolygon(pt1, poly))
    }
}
