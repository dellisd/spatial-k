package io.github.dellisd.spatialk.geojson.serialization

import io.github.dellisd.spatialk.geojson.BoundingBox
import io.github.dellisd.spatialk.geojson.Feature
import io.github.dellisd.spatialk.geojson.Geometry
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonEncoder
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.json.put

object FeatureSerializer : JsonSerializer<Feature> {
    override val descriptor: SerialDescriptor = buildClassSerialDescriptor("Feature")

    override fun deserialize(input: JsonDecoder): Feature {
        val tree = input.decodeJsonElement().jsonObject

        tree["type"]?.jsonPrimitive?.contentOrNull?.takeIf { it == "Feature" }
            ?: throw SerializationException("Feature does not have \"type\" specified")

        val bbox = tree["bbox"]?.let {
            input.json.decodeFromJsonElement(BoundingBoxSerializer, it)
        }

        val geometry = tree["geometry"]?.let {
            input.json.decodeFromJsonElement(Geometry.serializer(), it)
        }

        val id = tree["id"]?.jsonPrimitive?.content
        val properties = tree["properties"]?.jsonObject

        return Feature(geometry, properties ?: emptyMap(), id, bbox)
    }

    override fun serialize(output: JsonEncoder, value: Feature) {
        val data = buildJsonObject {
            put("type", "Feature")
            value.bbox?.let {
                put(
                    "bbox",
                    output.json.encodeToJsonElement(BoundingBox.serializer(), it)
                )
            }
            value.geometry?.let {
                put(
                    "geometry",
                    output.json.encodeToJsonElement(Geometry.serializer(), it)
                )
            }
            value.id?.let { put("id", it) }

            put("properties", JsonObject(value.properties))
        }
        output.encodeJsonElement(data)
    }
}
