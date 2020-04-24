package io.github.dellisd.spatialk.geojson.serialization

import io.github.dellisd.spatialk.geojson.LngLat
import io.github.dellisd.spatialk.geojson.Position
import kotlinx.serialization.Decoder
import kotlinx.serialization.Encoder
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialDescriptor
import kotlinx.serialization.SerializationException
import kotlinx.serialization.Serializer
import kotlinx.serialization.StructureKind
import kotlinx.serialization.json.JsonInput
import kotlinx.serialization.json.JsonOutput
import kotlinx.serialization.json.jsonArray

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
@Serializer(forClass = Position::class)
internal object PositionSerializer : KSerializer<Position> {
    override val descriptor: SerialDescriptor
        get() = SerialDescriptor("Position", StructureKind.LIST)

    override fun deserialize(decoder: Decoder): Position {
        val input = decoder as? JsonInput ?: throw SerializationException("This class can only be loaded from JSON")

        val array = input.decodeJson().jsonArray

        return LngLat(
            array.getPrimitive(0).double,
            array.getPrimitive(1).double,
            array.getPrimitiveOrNull(2)?.double
        )
    }

    override fun serialize(encoder: Encoder, value: Position) {
        val output = encoder as? JsonOutput ?: throw SerializationException("This class can only be saved as JSON")

        val array = jsonArray {
            +(value.longitude as Double?)
            +(value.latitude as Double?)
            if (value.altitude != null) {
                +value.altitude
            }
        }

        output.encodeJson(array)
    }
}

fun Position.Companion.serializer(): KSerializer<Position> = PositionSerializer
