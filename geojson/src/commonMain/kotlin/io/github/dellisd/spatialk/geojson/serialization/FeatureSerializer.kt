package io.github.dellisd.spatialk.geojson.serialization

import io.github.dellisd.spatialk.geojson.BoundingBox
import io.github.dellisd.spatialk.geojson.Feature
import io.github.dellisd.spatialk.geojson.Geometry
import kotlinx.serialization.SerialDescriptor
import kotlinx.serialization.SerializationException
import kotlinx.serialization.StructureKind
import kotlinx.serialization.json.JsonInput
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonOutput
import kotlinx.serialization.json.content
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.json

internal object FeatureSerializer : JsonSerializer<Feature> {
    override val descriptor: SerialDescriptor = SerialDescriptor("Feature", StructureKind.CLASS)

    override fun deserialize(input: JsonInput): Feature {
        val tree = input.decodeJson().jsonObject

        tree["type"]?.contentOrNull?.takeIf { it == "Feature" }
            ?: throw SerializationException("Feature does not have \"type\" specified")

        val bbox = tree["bbox"]?.let {
            input.json.fromJson(BoundingBoxSerializer, it)
        }

        val geometry = tree["geometry"]?.let {
            input.json.fromJson(Geometry.serializer(), it)
        }

        val id = tree["id"]?.content
        val properties = tree["properties"]?.jsonObject?.content

        return Feature(geometry, properties ?: emptyMap(), id, bbox)
    }

    override fun serialize(output: JsonOutput, value: Feature) {
        val data = json {
            "type" to "Feature"
            value.bbox?.let { "bbox" to output.json.toJson(BoundingBox.serializer(), it) }
            value.geometry?.let { "geometry" to output.json.toJson(Geometry.serializer(), it) }
            value.id?.let { "id" to it }

            "properties" to JsonObject(value.properties)
        }
        output.encodeJson(data)
    }
}
