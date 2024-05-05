package io.github.dellisd.spatialk.geojson.dsl

import io.github.dellisd.spatialk.geojson.FeatureCollection
import io.github.dellisd.spatialk.geojson.LineString
import io.github.dellisd.spatialk.geojson.MultiPoint
import io.github.dellisd.spatialk.geojson.Polygon
import io.github.dellisd.spatialk.geojson.Position
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails

@Suppress("MagicNumber")
class GeoJsonDslTests {

    private val collectionDsl = featureCollection {
        val simplePoint = point(-75.0, 45.0, 100.0)
        // Point
        feature(geometry = simplePoint, id = "point1") {
            put("name", "Hello World")
        }
        // MultiPoint
        feature(geometry = multiPoint {
            +simplePoint
            +Position(45.0, 45.0)
            +Position(0.0, 0.0)
        })


        val simpleLine = lineString {
            +Position(45.0, 45.0)
            +Position(0.0, 0.0)
        }

        // LineString
        feature(geometry = simpleLine)

        // MultiLineString
        feature(geometry = multiLineString {
            +simpleLine
            lineString {
                +Position(44.4, 55.5)
                +Position(55.5, 66.6)
            }
        })

        val simplePolygon = polygon {
            ring {
                +simpleLine
                point(12.0, 12.0)
                complete()
            }
            ring {
                point(4.0, 4.0)
                point(2.0, 2.0)
                point(3.0, 3.0)
                complete()
            }
        }

        // Polygon
        feature(geometry = simplePolygon)

        feature(geometry = multiPolygon {
            +simplePolygon
            polygon {
                ring {
                    point(12.0, 0.0)
                    point(0.0, 12.0)
                    point(-12.0, 0.0)
                    point(5.0, 5.0)
                    complete()
                }
            }
        })

        feature(geometry = geometryCollection {
            +simplePoint
            +simpleLine
            +simplePolygon
            +point(-70.0, 40.0) {
                foreignMembers {
                    put("geometry extension", "value")
                }
            }
        }, foreignMembers = {
            put("fm string", "str")
            put("fm number", 5)
            put("fm array", JsonArray(listOf(JsonPrimitive("elem1"), JsonPrimitive("elem2"))))
            put("fm bool", true)
            put("fm object", JsonObject(mapOf("prop1" to JsonPrimitive("value1"), "prop2" to JsonPrimitive("value2"))))
        })

        foreignMembers {
            put("feature collection extension", "value")
        }
    }

    private val collectionJson =
        """{"type":"FeatureCollection","features":[{"type":"Feature","geometry":{"type":"Point","coordinates":
|[-75.0,45.0,100.0]},"id":"point1","properties":{"name":"Hello World"}},{"type":"Feature","geometry":
|{"type":"MultiPoint","coordinates":[[-75.0,45.0,100.0],[45.0,45.0],[0.0,0.0]]},"properties":{}},{"type":"Feature",
|"geometry":{"type":"LineString","coordinates":[[45.0,45.0],[0.0,0.0]]},"properties":{}},{"type":"Feature",
|"geometry":{"type":"MultiLineString","coordinates":[[[45.0,45.0],[0.0,0.0]],[[44.4,55.5],[55.5,66.6]]]},
|"properties":{}},{"type":"Feature","geometry":{"type":"Polygon","coordinates":[[[45.0,45.0],[0.0,0.0],[12.0,12.0],
|[45.0,45.0]],[[4.0,4.0],[2.0,2.0],[3.0,3.0],[4.0,4.0]]]},"properties":{}},{"type":"Feature",
|"geometry":{"type":"MultiPolygon","coordinates":[[[[45.0,45.0],[0.0,0.0],[12.0,12.0],[45.0,45.0]],[[4.0,4.0],[2.0,2.0],
|[3.0,3.0],[4.0,4.0]]],[[[12.0,0.0],[0.0,12.0],[-12.0,0.0],[5.0,5.0],[12.0,0.0]]]]},"properties":{}},{"type":"Feature",
|"geometry":{"type":"GeometryCollection","geometries":[{"type":"Point","coordinates":[-75.0,45.0,100.0]},
|{"type":"LineString","coordinates":[[45.0,45.0],[0.0,0.0]]},{"type":"Polygon","coordinates":[[[45.0,45.0],[0.0,0.0],
|[12.0,12.0],[45.0,45.0]],[[4.0,4.0],[2.0,2.0],[3.0,3.0],[4.0,4.0]]]},{"type":"Point","coordinates":[-70.0,40.0],"geometry extension":"value"}]},
|"properties":{},"fm string":"str","fm number":5,"fm array":["elem1","elem2"],"fm bool":true,"fm object":{"prop1":"value1","prop2":"value2"}}],
|"feature collection extension":"value"}
|""".trimMargin()

    @Test
    fun testDslConstruction() {
        assertEquals(FeatureCollection.fromJson(collectionJson), collectionDsl)
    }

    @Test
    fun testLngLatRequirements() {
        assertFails { lngLat(-200.0, 50.0) }
        assertFails { lngLat(0.0, 99.0) }
        assertFails { lngLat(500.0, -180.0) }
    }

    @Test
    fun testNoInlinePositionRequirements() {
        assertEquals(MultiPoint(Position(-200.0, 0.0), Position(200.0, 99.0)), multiPoint {
            point(-200.0, 0.0)
            point(200.0, 99.0)
        })

        assertEquals(LineString(Position(-200.0, 0.0), Position(200.0, 99.0)), lineString {
            point(-200.0, 0.0)
            point(200.0, 99.0)
        })

        assertEquals(Polygon(
            listOf(
                Position(-200.0, 0.0),
                Position(200.0, 99.0),
                Position(500.0, 99.0),
                Position(-200.0, 0.0)
            )
        ),
            polygon {
                ring {
                    point(-200.0, 0.0)
                    point(200.0, 99.0)
                    point(500.0, 99.0)
                    complete()
                }
            })
    }
}
