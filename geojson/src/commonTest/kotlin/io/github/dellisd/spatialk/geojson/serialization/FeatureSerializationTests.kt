package io.github.dellisd.spatialk.geojson.serialization

import io.github.dellisd.spatialk.geojson.BoundingBox
import io.github.dellisd.spatialk.geojson.Feature
import io.github.dellisd.spatialk.geojson.Feature.Companion.toFeature
import io.github.dellisd.spatialk.geojson.LngLat
import io.github.dellisd.spatialk.geojson.Point
import kotlinx.serialization.UnstableDefault
import kotlinx.serialization.json.JsonLiteral
import kotlin.test.Test
import kotlin.test.assertEquals

@UnstableDefault
@Suppress("MagicNumber")
class FeatureSerializationTests {

    @Test
    fun testSerializeFeature() {
        val geometry = Point(LngLat(12.3, 45.6))
        val feature = Feature(
            geometry,
            id = "001",
            bbox = BoundingBox(11.6, 45.1, 12.7, 45.7),
            properties = mapOf(
                "size" to JsonLiteral(45.1),
                "name" to JsonLiteral("Nowhere")
            )
        )

        assertEquals(
            """{"type":"Feature","bbox":[11.6,45.1,12.7,45.7],"geometry":{"type":"Point","coordinates":[12.3,45.6]},
                |"id":"001","properties":{"size":45.1,"name":"Nowhere"}}
            """.trimMargin().replace("\n", ""),
            feature.json
        )
    }

    @Test
    fun testDeserializeFeature() {
        val geometry = Point(LngLat(12.3, 45.6))
        val feature = Feature(
            geometry,
            properties = mapOf(
                "size" to JsonLiteral(45.1),
                "name" to JsonLiteral("Nowhere")
            ),
            id = "001",
            bbox = BoundingBox(11.6, 45.1, 12.7, 45.7)
        )

        assertEquals(
            feature,
            """{"type":"Feature",
                |"bbox":[11.6,45.1,12.7,45.7],
                |"geometry":{
                    |"type":"Point",
                    |"coordinates":[12.3,45.6]},
                |"id":"001",
                |"properties":{
                    |"size":45.1,
                    |"name":"Nowhere"
                |}}
            """.trimMargin().replace("\n", "").toFeature()
        )
    }
}
