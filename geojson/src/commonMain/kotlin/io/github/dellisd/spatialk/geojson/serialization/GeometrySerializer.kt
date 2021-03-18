package io.github.dellisd.spatialk.geojson.serialization

import io.github.dellisd.spatialk.geojson.BoundingBox
import io.github.dellisd.spatialk.geojson.Geometry
import io.github.dellisd.spatialk.geojson.GeometryCollection
import io.github.dellisd.spatialk.geojson.LineString
import io.github.dellisd.spatialk.geojson.MultiLineString
import io.github.dellisd.spatialk.geojson.MultiPoint
import io.github.dellisd.spatialk.geojson.MultiPolygon
import io.github.dellisd.spatialk.geojson.Point
import io.github.dellisd.spatialk.geojson.Polygon
import io.github.dellisd.spatialk.geojson.Position
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.descriptors.PolymorphicKind
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonEncoder
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

@Suppress("LongMethod")
object GeometrySerializer : KSerializer<Geometry> {
    override val descriptor: SerialDescriptor
        get() = buildSerialDescriptor("Geometry", PolymorphicKind.SEALED)

    override fun deserialize(decoder: Decoder): Geometry {
        val input = decoder as? JsonDecoder ?: throw SerializationException("This class can only be loaded from JSON")

        val tree = input.decodeJsonElement().jsonObject
        val bbox = tree["bbox"]?.let {
            input.json.decodeFromJsonElement(BoundingBoxSerializer, it)
        }

        return when (val type = tree["type"]?.jsonPrimitive?.content) {
            "Point" -> {
                Point(input.json.decodeFromJsonElement(Position.serializer(), tree["coordinates"]!!), bbox)
            }
            "MultiPoint" -> {
                MultiPoint(
                    input.json.decodeFromJsonElement(
                        ListSerializer(Position.serializer()),
                        tree["coordinates"]!!
                    ), bbox
                )
            }
            "LineString" -> {
                LineString(
                    input.json.decodeFromJsonElement(
                        ListSerializer(Position.serializer()),
                        tree["coordinates"]!!
                    ), bbox
                )
            }
            "MultiLineString" -> {
                MultiLineString(
                    input.json.decodeFromJsonElement(
                        ListSerializer(ListSerializer(Position.serializer())),
                        tree["coordinates"]!!
                    ), bbox
                )
            }
            "Polygon" -> {
                Polygon(
                    input.json.decodeFromJsonElement(
                        ListSerializer(ListSerializer(Position.serializer())),
                        tree["coordinates"]!!
                    ), bbox
                )
            }
            "MultiPolygon" -> {
                MultiPolygon(
                    input.json.decodeFromJsonElement(
                        ListSerializer(ListSerializer(ListSerializer(Position.serializer()))),
                        tree["coordinates"]!!
                    ), bbox
                )
            }
            "GeometryCollection" -> {
                GeometryCollection(
                    input.json.decodeFromJsonElement(
                        ListSerializer(Geometry.serializer()),
                        tree["geometries"]!!
                    ), bbox
                )
            }
            else -> throw SerializationException("Unknown Geometry type $type")
        }
    }

    override fun serialize(encoder: Encoder, value: Geometry) {
        val output = encoder as? JsonEncoder ?: throw SerializationException("This class can only be saved as JSON")

        val tree = buildJsonObject {
            when (value) {
                is Point -> {
                    put("type", JsonPrimitive("Point"))
                    put(
                        "coordinates",
                        output.json.encodeToJsonElement(Position.serializer(), value.coordinates)
                    )
                }
                is MultiPoint -> {
                    put("type", JsonPrimitive("MultiPoint"))
                    put(
                        "coordinates",
                        output.json.encodeToJsonElement(ListSerializer(Position.serializer()), value.coordinates)
                    )
                }
                is LineString -> {
                    put("type", JsonPrimitive("LineString"))
                    put(
                        "coordinates",
                        output.json.encodeToJsonElement(ListSerializer(Position.serializer()), value.coordinates)
                    )
                }
                is MultiLineString -> {
                    put("type", JsonPrimitive("MultiLineString"))
                    put(
                        "coordinates", output.json.encodeToJsonElement(
                            ListSerializer(ListSerializer(Position.serializer())),
                            value.coordinates
                        )
                    )
                }
                is Polygon -> {
                    put("type", JsonPrimitive("Polygon"))
                    put(
                        "coordinates", output.json.encodeToJsonElement(
                            ListSerializer(ListSerializer(Position.serializer())),
                            value.coordinates
                        )
                    )
                }
                is MultiPolygon -> {
                    put("type", JsonPrimitive("MultiPolygon"))
                    put(
                        "coordinates", output.json.encodeToJsonElement(
                            ListSerializer(ListSerializer(ListSerializer(Position.serializer()))),
                            value.coordinates
                        )
                    )
                }
                is GeometryCollection -> {
                    put("type", JsonPrimitive("GeometryCollection"))
                    put(
                        "geometries",
                        output.json.encodeToJsonElement(ListSerializer(Geometry.serializer()), value.geometries)
                    )
                }
            }

            value.bbox?.let {
                put("bbox", output.json.encodeToJsonElement(BoundingBox.serializer(), it))
            }
        }

        encoder.encodeJsonElement(tree)
    }
}
