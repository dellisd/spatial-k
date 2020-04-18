package io.github.dellisd.geojson

import io.github.dellisd.geojson.serialization.serializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.UnstableDefault
import kotlinx.serialization.builtins.list
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

@UnstableDefault
@Suppress("MagicNumber", "TooManyFunctions")
class SerializationTests {

    @Test
    fun testSerializePosition() {
        val position = LngLat(-75.1, 45.1)
        val result = Json.stringify(Position.serializer(), position)
        assertEquals("[-75.1,45.1]", result)

        val altitude = LngLat(60.2, 23.2354, 100.5)
        val altitudeResult = Json.stringify(Position.serializer(), altitude)
        assertEquals("[60.2,23.2354,100.5]", altitudeResult)

        val list = listOf(LngLat(12.3, 45.6), LngLat(78.9, 12.3))
        val listResult = Json.stringify(Position.serializer().list, list)
        assertEquals("[[12.3,45.6],[78.9,12.3]]", listResult)
    }

    @Test
    fun testDeserializePosition() {
        val position = Json.parse(Position.serializer(), "[32.4,54.1234]")
        assertEquals(LngLat(32.4, 54.1234), position)

        val altitude = Json.parse(Position.serializer(), "[60.2,23.2354,100.5]")
        assertEquals(LngLat(60.2, 23.2354, 100.5), altitude)

        val list = Json.parse(Position.serializer().list, "[[12.3,45.6],[78.9,12.3]]")
        assertEquals(listOf(LngLat(12.3, 45.6), LngLat(78.9, 12.3)), list)
    }

    @Test
    fun testSerializeBoundingBox() {
        val bbox = BoundingBox(LngLat(-10.5, -10.5), LngLat(10.5, 10.5))
        val result = Json.stringify(BoundingBox.serializer(), bbox)
        assertEquals("[-10.5,-10.5,10.5,10.5]", result)

        val bbox3D = BoundingBox(LngLat(-10.5, -10.5, -100.8), LngLat(10.5, 10.5, 5.5))
        val result3D = Json.stringify(BoundingBox.serializer(), bbox3D)
        assertEquals("[-10.5,-10.5,-100.8,10.5,10.5,5.5]", result3D)

        // One altitude unspecified
        val bboxFake3D = BoundingBox(LngLat(-10.5, -10.5, -100.8), LngLat(10.5, 10.5))
        val fakeResult = Json.stringify(BoundingBox.serializer(), bboxFake3D)
        assertEquals("[-10.5,-10.5,10.5,10.5]", fakeResult)
    }

    @Test
    fun testDeserializeBoundingBox() {
        val bbox = Json.parse(BoundingBox.serializer(), "[-10.5,-10.5,10.5,10.5]")
        assertEquals(BoundingBox(LngLat(-10.5, -10.5), LngLat(10.5, 10.5)), bbox)

        val bbox3D = Json.parse(BoundingBox.serializer(), "[-10.5,-10.5,-100.8,10.5,10.5,5.5]")
        assertEquals(BoundingBox(LngLat(-10.5, -10.5, -100.8), LngLat(10.5, 10.5, 5.5)), bbox3D)

        assertFailsWith<SerializationException> {
            Json.parse(BoundingBox.serializer(), "[12.3]")
        }
    }

    // Geometries
    // Point
    @Test
    fun testSerializePoint() {
        val point = Point(LngLat(12.3, 45.6))
        assertEquals("""{"type":"Point","coordinates":[12.3,45.6]}""", point.json, "Point")
    }

    @Test
    fun testDeserializePoint() {
        val point = """{"type":"Point","coordinates":[12.3,45.6]}""".toGeometry<Point>()
        assertEquals(Point(LngLat(12.3, 45.6)), point)
        assertNull("""{"type":"MultiPoint","coordinates":[12.3,45.6]}""".toGeometryOrNull<Point>())
    }

    // MultiPoint
    @Test
    fun testSerializeMultiPoint() {
        val multiPoint = MultiPoint(LngLat(12.3, 45.6), LngLat(78.9, 12.3))
        assertEquals(
            """{"type":"MultiPoint","coordinates":[[12.3,45.6],[78.9,12.3]]}""",
            multiPoint.json,
            "MultiPoint"
        )
    }

    @Test
    fun testDeserializeMultiPoint() {
        val point = """{"type":"MultiPoint","coordinates":[[12.3,45.6],[78.9,12.3]]}""".toGeometry<MultiPoint>()
        assertEquals(MultiPoint(LngLat(12.3, 45.6), LngLat(78.9, 12.3)), point)
    }

    // LineString
    @Test
    fun testSerializeLineString() {
        val lineString = LineString(LngLat(12.3, 45.6), LngLat(78.9, 12.3))
        assertEquals(
            """{"type":"LineString","coordinates":[[12.3,45.6],[78.9,12.3]]}""",
            lineString.json,
            "LineString"
        )
    }

    @Test
    fun testDeserializeLineString() {
        val lineString = """{"type":"LineString","coordinates":[[12.3,45.6],[78.9,12.3]]}""".toGeometry<LineString>()
        assertEquals(
            LineString(LngLat(12.3, 45.6), LngLat(78.9, 12.3)),
            lineString,
            "LineString"
        )
    }

    // MultiLineString
    @Test
    fun testSerializeMultiLineString() {
        val multiLineString = MultiLineString(
            listOf(LngLat(12.3, 45.6), LngLat(78.9, 12.3)),
            listOf(LngLat(87.6, 54.3), LngLat(21.9, 56.4))
        )
        assertEquals(
            """{"type":"MultiLineString","coordinates":[[[12.3,45.6],[78.9,12.3]],[[87.6,54.3],[21.9,56.4]]]}""",
            multiLineString.json,
            "MultiLineString"
        )
    }

    @Test
    fun testDeserializeMultiLineString() {
        val multiLineString =
            """{"type":"MultiLineString","coordinates":[[[12.3,45.6],[78.9,12.3]],[[87.6,54.3],[21.9,56.4]]]}"""
                .toGeometry<MultiLineString>()
        assertEquals(
            MultiLineString(
                listOf(LngLat(12.3, 45.6), LngLat(78.9, 12.3)),
                listOf(LngLat(87.6, 54.3), LngLat(21.9, 56.4))
            ),
            multiLineString,
            "MultiLineString"
        )
    }

    // Polygon
    @Test
    fun testSerializePolygon() {
        val polygon = Polygon(
            listOf(
                LngLat(-79.87, 43.42),
                LngLat(-78.89, 43.49),
                LngLat(-79.07, 44.02),
                LngLat(-79.95, 43.87),
                LngLat(-79.87, 43.42)
            ),
            listOf(
                LngLat(-79.75, 43.81),
                LngLat(-79.56, 43.85),
                LngLat(-79.7, 43.88),
                LngLat(-79.75, 43.81)
            )
        )
        assertEquals(
            """{"type":"Polygon","coordinates":[[[-79.87,43.42],[-78.89,43.49],[-79.07,44.02],[-79.95,43.87],
            |[-79.87,43.42]],[[-79.75,43.81],[-79.56,43.85],[-79.7,43.88],[-79.75,43.81]]]}"""
                .trimMargin()
                .replace("\n", ""),
            polygon.json,
            "Polygon"
        )
    }

    @Test
    fun testDeserializePolygon() {
        val polygon = """{"type":"Polygon","coordinates":[[[-79.87,43.42],[-78.89,43.49],[-79.07,44.02],[-79.95,43.87],
            |[-79.87,43.42]],[[-79.75,43.81],[-79.56,43.85],[-79.7,43.88],[-79.75,43.81]]]}"""
            .trimMargin()
            .replace("\n", "")
            .toGeometry<Polygon>()

        assertEquals(
            Polygon(
                listOf(
                    LngLat(-79.87, 43.42),
                    LngLat(-78.89, 43.49),
                    LngLat(-79.07, 44.02),
                    LngLat(-79.95, 43.87),
                    LngLat(-79.87, 43.42)
                ),
                listOf(
                    LngLat(-79.75, 43.81),
                    LngLat(-79.56, 43.85),
                    LngLat(-79.7, 43.88),
                    LngLat(-79.75, 43.81)
                )
            ),
            polygon,
            "Polygon"
        )
    }

    // MultiPolygon
    @Test
    fun testSerializeMultiPolygon() {
        val multiPolygon = MultiPolygon(
            listOf(
                listOf(
                    LngLat(-79.87, 43.42),
                    LngLat(-78.89, 43.49),
                    LngLat(-79.07, 44.02),
                    LngLat(-79.95, 43.87),
                    LngLat(-79.87, 43.42)
                ),
                listOf(
                    LngLat(-79.75, 43.81),
                    LngLat(-79.56, 43.85),
                    LngLat(-79.7, 43.88),
                    LngLat(-79.75, 43.81)
                )
            ),
            listOf(
                listOf(
                    LngLat(-79.87, 43.42),
                    LngLat(-78.89, 43.49),
                    LngLat(-79.07, 44.02),
                    LngLat(-79.95, 43.87),
                    LngLat(-79.87, 43.42)
                ),
                listOf(
                    LngLat(-79.75, 43.81),
                    LngLat(-79.56, 43.85),
                    LngLat(-79.7, 43.88),
                    LngLat(-79.75, 43.81)
                )
            )
        )
        assertEquals(
            """{"type":"MultiPolygon","coordinates":[[[[-79.87,43.42],[-78.89,43.49],[-79.07,44.02],[-79.95,43.87],
            |[-79.87,43.42]],[[-79.75,43.81],[-79.56,43.85],[-79.7,43.88],[-79.75,43.81]]],[[[-79.87,43.42],
            |[-78.89,43.49],[-79.07,44.02],[-79.95,43.87],[-79.87,43.42]],[[-79.75,43.81],[-79.56,43.85],[-79.7,43.88],
            |[-79.75,43.81]]]]}"""
                .trimMargin()
                .replace("\n", ""),
            multiPolygon.json,
            "MultiPolygon"
        )
    }

    @Test
    fun testDeserializeMultiPolygon() {
        val multiPolygon =
            """{"type":"MultiPolygon","coordinates":[[[[-79.87,43.42],[-78.89,43.49],[-79.07,44.02],[-79.95,43.87],
            |[-79.87,43.42]],[[-79.75,43.81],[-79.56,43.85],[-79.7,43.88],[-79.75,43.81]]],[[[-79.87,43.42],
            |[-78.89,43.49],[-79.07,44.02],[-79.95,43.87],[-79.87,43.42]],[[-79.75,43.81],[-79.56,43.85],[-79.7,43.88],
            |[-79.75,43.81]]]]}"""
                .trimMargin()
                .replace("\n", "")
                .toGeometry<MultiPolygon>()

        assertEquals(
            MultiPolygon(
                listOf(
                    listOf(
                        LngLat(-79.87, 43.42),
                        LngLat(-78.89, 43.49),
                        LngLat(-79.07, 44.02),
                        LngLat(-79.95, 43.87),
                        LngLat(-79.87, 43.42)
                    ),
                    listOf(
                        LngLat(-79.75, 43.81),
                        LngLat(-79.56, 43.85),
                        LngLat(-79.7, 43.88),
                        LngLat(-79.75, 43.81)
                    )
                ),
                listOf(
                    listOf(
                        LngLat(-79.87, 43.42),
                        LngLat(-78.89, 43.49),
                        LngLat(-79.07, 44.02),
                        LngLat(-79.95, 43.87),
                        LngLat(-79.87, 43.42)
                    ),
                    listOf(
                        LngLat(-79.75, 43.81),
                        LngLat(-79.56, 43.85),
                        LngLat(-79.7, 43.88),
                        LngLat(-79.75, 43.81)
                    )
                )
            ),
            multiPolygon,
            "MultiPolygon"
        )
    }

    // GeometryCollection
    @Test
    fun testSerializeGeometryCollection() {
        val point = Point(LngLat(12.3, 45.6))
        val multiPoint = MultiPoint(LngLat(12.3, 45.6), LngLat(78.9, 12.3))

        val collection = GeometryCollection(point, multiPoint)

        assertEquals(
            """{"type":"GeometryCollection","geometries":[{"type":"Point","coordinates":[12.3,45.6]},
            |{"type":"MultiPoint","coordinates":[[12.3,45.6],[78.9,12.3]]}]}"""
                .trimMargin()
                .replace("\n", ""),
            collection.json,
            "GeometryCollection"
        )
    }

    @Test
    fun testDeserializeGeometryCollection() {
        val point = Point(LngLat(12.3, 45.6))
        val multiPoint = MultiPoint(LngLat(12.3, 45.6), LngLat(78.9, 12.3))

        val collection = """{"type":"GeometryCollection","geometries":[{"type":"Point","coordinates":[12.3,45.6]},
            |{"type":"MultiPoint","coordinates":[[12.3,45.6],[78.9,12.3]]}]}"""
            .trimMargin()
            .replace("\n", "")
            .toGeometry<GeometryCollection>()

        assertEquals(
            GeometryCollection(point, multiPoint),
            collection,
            "GeometryCollection"
        )
    }
}
