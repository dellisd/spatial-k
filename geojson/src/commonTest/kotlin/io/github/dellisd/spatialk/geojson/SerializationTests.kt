package io.github.dellisd.spatialk.geojson

import kotlinx.serialization.SerializationException
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

@ExperimentalGeoJsonApi
@Suppress("MagicNumber", "TooManyFunctions")
class SerializationTests {

    @Test
    fun testSerializePosition() {
        val position = Position(-75.1, 45.1)
        val result = Json.encodeToString(Position.serializer(), position)
        assertEquals("[-75.1,45.1]", result)

        val altitude = Position(60.2, 23.2354, 100.5)
        val altitudeResult = Json.encodeToString(Position.serializer(), altitude)
        assertEquals("[60.2,23.2354,100.5]", altitudeResult)

        val list = listOf(Position(12.3, 45.6), Position(78.9, 12.3))
        val listResult = Json.encodeToString(ListSerializer(Position.serializer()), list)
        assertEquals("[[12.3,45.6],[78.9,12.3]]", listResult)
    }

    @Test
    fun testDeserializePosition() {
        val position = Json.decodeFromString(Position.serializer(), "[32.4,54.1234]")
        assertEquals(Position(32.4, 54.1234), position)

        val altitude = Json.decodeFromString(Position.serializer(), "[60.2,23.2354,100.5]")
        assertEquals(Position(60.2, 23.2354, 100.5), altitude)

        val list = Json.decodeFromString(ListSerializer(Position.serializer()), "[[12.3,45.6],[78.9,12.3]]")
        assertEquals(listOf(Position(12.3, 45.6), Position(78.9, 12.3)), list)
    }

    @Test
    fun testSerializeBoundingBox() {
        val bbox =
            BoundingBox(Position(-10.5, -10.5), Position(10.5, 10.5))
        val result = Json.encodeToString(BoundingBox.serializer(), bbox)
        assertEquals("[-10.5,-10.5,10.5,10.5]", result)

        val bbox3D = BoundingBox(
            Position(-10.5, -10.5, -100.8),
            Position(10.5, 10.5, 5.5)
        )
        val result3D = Json.encodeToString(BoundingBox.serializer(), bbox3D)
        assertEquals("[-10.5,-10.5,-100.8,10.5,10.5,5.5]", result3D)

        // One altitude unspecified
        val bboxFake3D = BoundingBox(
            Position(-10.5, -10.5, -100.8),
            Position(10.5, 10.5)
        )
        val fakeResult = Json.encodeToString(BoundingBox.serializer(), bboxFake3D)
        assertEquals("[-10.5,-10.5,10.5,10.5]", fakeResult)
    }

    @Test
    fun testDeserializeBoundingBox() {
        val bbox = Json.decodeFromString(BoundingBox.serializer(), "[-10.5,-10.5,10.5,10.5]")
        assertEquals(
            BoundingBox(
                Position(-10.5, -10.5),
                Position(10.5, 10.5)
            ), bbox)

        val bbox3D = Json.decodeFromString(BoundingBox.serializer(), "[-10.5,-10.5,-100.8,10.5,10.5,5.5]")
        assertEquals(
            BoundingBox(
                Position(-10.5, -10.5, -100.8),
                Position(10.5, 10.5, 5.5)
            ), bbox3D)

        assertFailsWith<SerializationException> {
            Json.decodeFromString(BoundingBox.serializer(), "[12.3]")
        }
    }

    // Geometries
    // Point
    @Test
    fun testSerializePoint() {
        val point = Point(Position(12.3, 45.6))
        assertEquals("""{"type":"Point","coordinates":[12.3,45.6]}""", point.json, "Point")
    }

    @Test
    fun testDeserializePoint() {
        val point = """{"type":"Point","coordinates":[12.3,45.6]}""".toGeometry<Point>()
        assertEquals(Point(Position(12.3, 45.6)), point)
        assertNull("""{"type":"MultiPoint","coordinates":[12.3,45.6]}""".toGeometryOrNull<Point>())
    }

    // MultiPoint
    @Test
    fun testSerializeMultiPoint() {
        val multiPoint = MultiPoint(Position(12.3, 45.6), Position(78.9, 12.3))
        assertEquals(
            """{"type":"MultiPoint","coordinates":[[12.3,45.6],[78.9,12.3]]}""",
            multiPoint.json,
            "MultiPoint"
        )
    }

    @Test
    fun testDeserializeMultiPoint() {
        val point = """{"type":"MultiPoint","coordinates":[[12.3,45.6],[78.9,12.3]]}""".toGeometry<MultiPoint>()
        assertEquals(MultiPoint(Position(12.3, 45.6), Position(78.9, 12.3)), point)
    }

    // LineString
    @Test
    fun testSerializeLineString() {
        val lineString = LineString(Position(12.3, 45.6), Position(78.9, 12.3))
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
            LineString(Position(12.3, 45.6), Position(78.9, 12.3)),
            lineString,
            "LineString"
        )
    }

    // MultiLineString
    @Test
    fun testSerializeMultiLineString() {
        val multiLineString = MultiLineString(
            listOf(Position(12.3, 45.6), Position(78.9, 12.3)),
            listOf(Position(87.6, 54.3), Position(21.9, 56.4))
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
                listOf(Position(12.3, 45.6), Position(78.9, 12.3)),
                listOf(Position(87.6, 54.3), Position(21.9, 56.4))
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
                Position(-79.87, 43.42),
                Position(-78.89, 43.49),
                Position(-79.07, 44.02),
                Position(-79.95, 43.87),
                Position(-79.87, 43.42)
            ),
            listOf(
                Position(-79.75, 43.81),
                Position(-79.56, 43.85),
                Position(-79.7, 43.88),
                Position(-79.75, 43.81)
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
                    Position(-79.87, 43.42),
                    Position(-78.89, 43.49),
                    Position(-79.07, 44.02),
                    Position(-79.95, 43.87),
                    Position(-79.87, 43.42)
                ),
                listOf(
                    Position(-79.75, 43.81),
                    Position(-79.56, 43.85),
                    Position(-79.7, 43.88),
                    Position(-79.75, 43.81)
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
                    Position(-79.87, 43.42),
                    Position(-78.89, 43.49),
                    Position(-79.07, 44.02),
                    Position(-79.95, 43.87),
                    Position(-79.87, 43.42)
                ),
                listOf(
                    Position(-79.75, 43.81),
                    Position(-79.56, 43.85),
                    Position(-79.7, 43.88),
                    Position(-79.75, 43.81)
                )
            ),
            listOf(
                listOf(
                    Position(-79.87, 43.42),
                    Position(-78.89, 43.49),
                    Position(-79.07, 44.02),
                    Position(-79.95, 43.87),
                    Position(-79.87, 43.42)
                ),
                listOf(
                    Position(-79.75, 43.81),
                    Position(-79.56, 43.85),
                    Position(-79.7, 43.88),
                    Position(-79.75, 43.81)
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
                        Position(-79.87, 43.42),
                        Position(-78.89, 43.49),
                        Position(-79.07, 44.02),
                        Position(-79.95, 43.87),
                        Position(-79.87, 43.42)
                    ),
                    listOf(
                        Position(-79.75, 43.81),
                        Position(-79.56, 43.85),
                        Position(-79.7, 43.88),
                        Position(-79.75, 43.81)
                    )
                ),
                listOf(
                    listOf(
                        Position(-79.87, 43.42),
                        Position(-78.89, 43.49),
                        Position(-79.07, 44.02),
                        Position(-79.95, 43.87),
                        Position(-79.87, 43.42)
                    ),
                    listOf(
                        Position(-79.75, 43.81),
                        Position(-79.56, 43.85),
                        Position(-79.7, 43.88),
                        Position(-79.75, 43.81)
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
        val point = Point(Position(12.3, 45.6))
        val multiPoint = MultiPoint(Position(12.3, 45.6), Position(78.9, 12.3))

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
        val point = Point(Position(12.3, 45.6))
        val multiPoint = MultiPoint(Position(12.3, 45.6), Position(78.9, 12.3))

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
