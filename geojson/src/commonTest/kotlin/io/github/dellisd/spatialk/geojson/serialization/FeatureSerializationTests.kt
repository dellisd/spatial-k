package io.github.dellisd.spatialk.geojson.serialization

import io.github.dellisd.spatialk.geojson.BoundingBox
import io.github.dellisd.spatialk.geojson.Feature
import io.github.dellisd.spatialk.geojson.Position
import io.github.dellisd.spatialk.geojson.Point
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonPrimitive
import kotlin.test.Test
import kotlin.test.assertEquals

@Suppress("MagicNumber")
class FeatureSerializationTests {

    @Test
    fun testSerializeFeature() {
        val geometry = Point(Position(12.3, 45.6), foreignMembers = mapOf(
            "geom ext" to JsonPrimitive("extension value")
        ))
        val feature = Feature(
            geometry,
            mapOf(
                "size" to JsonPrimitive(45.1),
                "name" to JsonPrimitive("Nowhere")
            ),
            "001",
            BoundingBox(11.6, 45.1, 12.7, 45.7),
            foreignMembers = mapOf(
                "ext" to JsonPrimitive("extension value")
            )
        )

        val json =
            """{"type":"Feature",
                |"bbox":[11.6,45.1,12.7,45.7],
                |"geometry":{"type":"Point","coordinates":[12.3,45.6],"geom ext":"extension value"},
                |"id":"001",
                |"properties":{"size":45.1,"name":"Nowhere"},
                |"ext":"extension value"}
            """.trimMargin().replace("\n", "")

        assertEquals(json, feature.json(), "Feature (fast)")
        assertEquals(json, Json.encodeToString(feature), "Feature (kotlinx)")
    }

    @Test
    fun testDeserializeFeature() {
        val geometry = Point(Position(12.3, 45.6), foreignMembers = mapOf(
            "geom ext" to JsonPrimitive("extension value")
        ))
        val feature = Feature(
            geometry,
            properties = mapOf(
                "size" to JsonPrimitive(45.1),
                "name" to JsonPrimitive("Nowhere")
            ),
            id = "001",
            bbox = BoundingBox(11.6, 45.1, 12.7, 45.7),
            foreignMembers = mapOf(
                "ext" to JsonPrimitive("extension value")
            )
        )

        assertEquals(
            feature,
            Feature.fromJson(
                """{"type":"Feature",
                |"bbox":[11.6,45.1,12.7,45.7],
                |"geometry":{
                    |"type":"Point",
                    |"coordinates":[12.3,45.6],
                    |"geom ext":"extension value"},
                |"id":"001",
                |"properties":{
                    |"size":45.1,
                    |"name":"Nowhere"
                |},
                |"ext":"extension value"}
            """.trimMargin().replace("\n", "")
            )
        )
    }
}
