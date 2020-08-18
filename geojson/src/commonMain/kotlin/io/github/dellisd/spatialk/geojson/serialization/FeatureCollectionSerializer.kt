package io.github.dellisd.spatialk.geojson.serialization

import io.github.dellisd.spatialk.geojson.BoundingBox
import io.github.dellisd.spatialk.geojson.Feature
import io.github.dellisd.spatialk.geojson.FeatureCollection
import kotlinx.serialization.SerializationException
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonEncoder
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.put

object FeatureCollectionSerializer : JsonSerializer<FeatureCollection> {
    override val descriptor: SerialDescriptor = buildClassSerialDescriptor("FeatureCollection")

    override fun deserialize(input: JsonDecoder): FeatureCollection {
        val tree = input.decodeJsonElement().jsonObject

        val bbox = tree["bbox"]?.let { input.json.decodeFromJsonElement(BoundingBox.serializer(), it) }

        val features = tree["features"]?.let { input.json.decodeFromJsonElement(
            ListSerializer(Feature.serializer()),
            it
        ) }
            ?: throw SerializationException("FeatureCollection has no member \"features\"")

        return FeatureCollection(features.toMutableList(), bbox)
    }

    override fun serialize(output: JsonEncoder, value: FeatureCollection) {
        val data = buildJsonObject {
            put("type", "FeatureCollection")
            value.bbox?.let { put("bbox", output.json.encodeToJsonElement(BoundingBox.serializer(), it)) }
            put(
                "features",
                output.json.encodeToJsonElement(ListSerializer(Feature.serializer()), value.features)
            )
        }
        output.encodeJsonElement(data)
    }
}
