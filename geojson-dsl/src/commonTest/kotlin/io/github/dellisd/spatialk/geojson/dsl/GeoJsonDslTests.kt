package io.github.dellisd.spatialk.geojson.dsl

import io.github.dellisd.spatialk.geojson.FeatureCollection.Companion.toFeatureCollection
import io.github.dellisd.spatialk.geojson.LngLat
import kotlinx.serialization.UnstableDefault
import kotlin.test.Test
import kotlin.test.assertEquals

@Suppress("MagicNumber")
class GeoJsonDslTests {

    private val collectionDsl = featureCollection {
        val simplePoint = point(-75.0, 45.0, 100.0)
        // Point
        +feature {
            geometry = simplePoint
            id = "point1"
            properties {
                "name" to "Hello World"
            }
        }
        // MultiPoint
        +feature {
            geometry = multiPoint {
                +simplePoint
                +LngLat(45.0, 45.0)
                +LngLat(0.0, 0.0)
            }
        }

        val simpleLine = lineString {
            +LngLat(45.0, 45.0)
            +LngLat(0.0, 0.0)
        }

        // LineString
        +feature {
            geometry = simpleLine
        }

        // MultiLineString
        +feature {
            geometry = multiLineString {
                +simpleLine
                +lineString {
                    +LngLat(44.4, 55.5)
                    +LngLat(55.5, 66.6)
                }
            }
        }

        val simplePolygon = polygon {
            ring {
                +simpleLine
                +LngLat(12.0, 12.0)
                complete()
            }
            ring {
                +LngLat(4.0, 4.0)
                +LngLat(2.0, 2.0)
                +LngLat(3.0, 3.0)
                complete()
            }
        }

        // Polygon
        +feature {
            geometry = simplePolygon
        }

        +feature {
            geometry = multiPolygon {
                +simplePolygon
                +polygon {
                    ring {
                        +LngLat(12.0, 0.0)
                        +LngLat(0.0, 12.0)
                        +LngLat(-12.0, 0.0)
                        +LngLat(5.0, 5.0)
                        complete()
                    }
                }
            }
        }

        +feature {
            geometry = geometryCollection {
                +simplePoint
                +simpleLine
                +simplePolygon
            }
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
|[12.0,12.0],[45.0,45.0]],[[4.0,4.0],[2.0,2.0],[3.0,3.0],[4.0,4.0]]]}]},"properties":{}}]}""".trimMargin()

    @UnstableDefault
    @Test
    fun testDslConstruction() {
        assertEquals(collectionJson.toFeatureCollection(), collectionDsl)
    }
}
