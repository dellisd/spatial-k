package io.github.dellisd.spatialk.geojson.serialization

import io.github.dellisd.spatialk.geojson.LngLat
import io.github.dellisd.spatialk.geojson.Position
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.Serializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.StructureKind
import kotlinx.serialization.descriptors.buildSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonEncoder
import kotlinx.serialization.json.add
import kotlinx.serialization.json.buildJsonArray
import kotlinx.serialization.json.double
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonPrimitive

/**
 * [KSerializer] implementation for implementations of the [Position] interface.
 * Serializes a Position down to an array of numbers as specified by GeoJSON.
 * This serializer only works for converting to and from JSON.
 * A position maps to `[longitude, latitude, altitude]`.
 *
 * A position's [altitude][Position.altitude] is only included in the array if it is not null.
 *
 * An instance of the serializer can be obtained from the [Position.serializer][Position.Companion.serializer]
 * extension function.
 *
 * @see Position.Companion.serializer
 */
@InternalSerializationApi
@ExperimentalSerializationApi
@Serializer(forClass = Position::class)
object PositionSerializer : KSerializer<Position> {
    override val descriptor: SerialDescriptor
        get() = buildSerialDescriptor("Position", StructureKind.LIST)

    override fun deserialize(decoder: Decoder): Position {
        val input = decoder as? JsonDecoder ?: throw SerializationException("This class can only be loaded from JSON")

        val array = input.decodeJsonElement().jsonArray

        return LngLat(
            array[0].jsonPrimitive.double,
            array[1].jsonPrimitive.double,
            array.getOrNull(2)?.jsonPrimitive?.double
        )
    }

    override fun serialize(encoder: Encoder, value: Position) {
        val output = encoder as? JsonEncoder ?: throw SerializationException("This class can only be saved as JSON")

        val array = buildJsonArray {
            add((value.longitude as Double?))
            add((value.latitude as Double?))
            if (value.altitude != null) {
                add(value.altitude)
            }
        }

        output.encodeJsonElement(array)
    }
}

@OptIn(InternalSerializationApi::class, ExperimentalSerializationApi::class)
fun Position.Companion.serializer(): KSerializer<Position> = PositionSerializer
