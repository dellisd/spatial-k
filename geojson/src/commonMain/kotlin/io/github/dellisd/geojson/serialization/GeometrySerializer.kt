package io.github.dellisd.geojson.serialization

import io.github.dellisd.geojson.BoundingBox
import io.github.dellisd.geojson.Geometry
import io.github.dellisd.geojson.GeometryCollection
import io.github.dellisd.geojson.LineString
import io.github.dellisd.geojson.MultiLineString
import io.github.dellisd.geojson.MultiPoint
import io.github.dellisd.geojson.MultiPolygon
import io.github.dellisd.geojson.Point
import io.github.dellisd.geojson.Polygon
import io.github.dellisd.geojson.Position
import kotlinx.serialization.Decoder
import kotlinx.serialization.Encoder
import kotlinx.serialization.KSerializer
import kotlinx.serialization.PolymorphicKind
import kotlinx.serialization.SerialDescriptor
import kotlinx.serialization.SerializationException
import kotlinx.serialization.Serializer
import kotlinx.serialization.builtins.list
import kotlinx.serialization.json.JsonInput
import kotlinx.serialization.json.JsonLiteral
import kotlinx.serialization.json.JsonOutput
import kotlinx.serialization.json.content
import kotlinx.serialization.json.json

@Serializer(forClass = Geometry::class)
internal object GeometrySerializer : KSerializer<Geometry> {
    override val descriptor: SerialDescriptor
        get() = SerialDescriptor("Geometry", PolymorphicKind.SEALED)

    override fun deserialize(decoder: Decoder): Geometry {
        val input = decoder as? JsonInput ?: throw SerializationException("This class can only be loaded from JSON")

        val tree = input.decodeJson().jsonObject
        val bbox = tree["bbox"]?.let {
            input.json.fromJson(BoundingBoxSerializer, it)
        }

        return when (val type = tree["type"]?.content) {
            "Point" -> {
                Point(input.json.fromJson(Position.serializer(), tree["coordinates"]!!), bbox)
            }
            "MultiPoint" -> {
                MultiPoint(input.json.fromJson(Position.serializer().list, tree["coordinates"]!!), bbox)
            }
            "LineString" -> {
                LineString(input.json.fromJson(Position.serializer().list, tree["coordinates"]!!), bbox)
            }
            "MultiLineString" -> {
                MultiLineString(input.json.fromJson(Position.serializer().list.list, tree["coordinates"]!!), bbox)
            }
            "Polygon" -> {
                Polygon(input.json.fromJson(Position.serializer().list.list, tree["coordinates"]!!), bbox)
            }
            "MultiPolygon" -> {
                MultiPolygon(input.json.fromJson(Position.serializer().list.list.list, tree["coordinates"]!!), bbox)
            }
            "GeometryCollection" -> {
                GeometryCollection(input.json.fromJson(Geometry.serializer().list, tree["geometries"]!!), bbox)
            }
            else -> throw SerializationException("Unknown Geometry type $type")
        }
    }

    override fun serialize(encoder: Encoder, value: Geometry) {
        val output = encoder as? JsonOutput ?: throw SerializationException("This class can only be saved as JSON")

        val tree = json {
            when (value) {
                is Point -> {
                    "type" to JsonLiteral("Point")
                    "coordinates" to output.json.toJson(Position.serializer(), value.coordinates)
                }
                is MultiPoint -> {
                    "type" to JsonLiteral("MultiPoint")
                    "coordinates" to output.json.toJson(Position.serializer().list, value.coordinates)
                }
                is LineString -> {
                    "type" to JsonLiteral("LineString")
                    "coordinates" to output.json.toJson(Position.serializer().list, value.coordinates)
                }
                is MultiLineString -> {
                    "type" to JsonLiteral("MultiLineString")
                    "coordinates" to output.json.toJson(Position.serializer().list.list, value.coordinates)
                }
                is Polygon -> {
                    "type" to JsonLiteral("Polygon")
                    "coordinates" to output.json.toJson(Position.serializer().list.list, value.coordinates)
                }
                is MultiPolygon -> {
                    "type" to JsonLiteral("MultiPolygon")
                    "coordinates" to output.json.toJson(Position.serializer().list.list.list, value.coordinates)
                }
                is GeometryCollection -> {
                    "type" to JsonLiteral("GeometryCollection")
                    "geometries" to output.json.toJson(Geometry.serializer().list, value.geometries)
                }
            }

            if (value.bbox != null) {
                "bbox" to output.json.toJson(BoundingBox.serializer(), value.bbox)
            }
        }

        encoder.encodeJson(tree)
    }
}
