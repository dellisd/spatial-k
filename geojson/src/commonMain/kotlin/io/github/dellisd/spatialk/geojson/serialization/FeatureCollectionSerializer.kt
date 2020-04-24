package io.github.dellisd.spatialk.geojson.serialization

import io.github.dellisd.spatialk.geojson.BoundingBox
import io.github.dellisd.spatialk.geojson.Feature
import io.github.dellisd.spatialk.geojson.FeatureCollection
import kotlinx.serialization.SerialDescriptor
import kotlinx.serialization.SerializationException
import kotlinx.serialization.builtins.list
import kotlinx.serialization.json.JsonInput
import kotlinx.serialization.json.JsonOutput
import kotlinx.serialization.json.json

object FeatureCollectionSerializer : JsonSerializer<FeatureCollection> {
    override val descriptor: SerialDescriptor = SerialDescriptor("FeatureCollection")

    override fun deserialize(input: JsonInput): FeatureCollection {
        val tree = input.decodeJson().jsonObject

        val bbox = tree["bbox"]?.let { input.json.fromJson(BoundingBox.serializer(), it) }

        val features = tree["features"]?.let { input.json.fromJson(Feature.serializer().list, it) }
            ?: throw SerializationException("FeatureCollection has no member \"features\"")

        return FeatureCollection(features.toMutableList(), bbox)
    }

    override fun serialize(output: JsonOutput, value: FeatureCollection) {
        val data = json {
            "type" to "FeatureCollection"
            value.bbox?.let { "bbox" to output.json.toJson(BoundingBox.serializer(), it) }
            "features" to output.json.toJson(Feature.serializer().list, value.features)
        }
        output.encodeJson(data)
    }
}
