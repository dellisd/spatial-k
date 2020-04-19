package io.github.dellisd.geojson.serialization

import io.github.dellisd.geojson.Feature
import io.github.dellisd.geojson.FeatureCollection
import io.github.dellisd.geojson.FeatureCollection.Companion.toFeatureCollection
import io.github.dellisd.geojson.LngLat
import io.github.dellisd.geojson.Point
import kotlinx.serialization.json.JsonLiteral
import kotlin.test.Test
import kotlin.test.assertEquals

@Suppress("EXPERIMENTAL_API_USAGE", "MagicNumber")
class FeatureCollectionSerializationTests {

    @Test
    fun testSerializeFeatureCollection() {
        val geometry = Point(LngLat(12.3, 45.6))
        val feature = Feature(
            geometry, mapOf(
                "size" to JsonLiteral(45.1),
                "name" to JsonLiteral("Nowhere")
            )
        )
        val collection = FeatureCollection(feature, feature)

        assertEquals(
            """{"type":"FeatureCollection","features":[{"type":"Feature","geometry":{"type":"Point","coordinates":
                |[12.3,45.6]},"properties":{"size":45.1,"name":"Nowhere"}},{"type":"Feature","geometry":{"type":"Point",
                |"coordinates":[12.3,45.6]},"properties":{"size":45.1,"name":"Nowhere"}}]}"""
                .trimMargin()
                .replace("\n", ""),
            collection.json,
            "FeatureCollection"
        )
    }

    @Test
    fun testDeserializeFeatureCollection() {
        val geometry = Point(LngLat(12.3, 45.6))
        val feature = Feature(
            geometry, mapOf(
                "size" to JsonLiteral(45.1),
                "name" to JsonLiteral("Nowhere")
            )
        )
        val collection = FeatureCollection(feature, feature)

        assertEquals(
            collection,
            """{"type":"FeatureCollection","features":[{"type":"Feature","geometry":{"type":"Point","coordinates":
                |[12.3,45.6]},"properties":{"size":45.1,"name":"Nowhere"}},{"type":"Feature","geometry":{"type":"Point",
                |"coordinates":[12.3,45.6]},"properties":{"size":45.1,"name":"Nowhere"}}]}"""
                .trimMargin()
                .replace("\n", "")
                .toFeatureCollection()
        )
    }
}
