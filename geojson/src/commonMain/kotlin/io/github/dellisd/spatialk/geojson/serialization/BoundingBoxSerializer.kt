package io.github.dellisd.spatialk.geojson.serialization

import io.github.dellisd.spatialk.geojson.BoundingBox
import io.github.dellisd.spatialk.geojson.LngLat
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

@Serializer(forClass = BoundingBox::class)
internal object BoundingBoxSerializer : KSerializer<BoundingBox> {
    private const val ARRAY_SIZE_2D = 4
    private const val ARRAY_SIZE_3D = 6

    override val descriptor: SerialDescriptor
        get() = SerialDescriptor("BoundingBox", StructureKind.LIST)

    @Suppress("MagicNumber")
    override fun deserialize(decoder: Decoder): BoundingBox {
        val input = decoder as? JsonInput ?: throw SerializationException("This class can only be loaded from JSON")
        val array = input.decodeJson().jsonArray

        return when (array.size) {
            ARRAY_SIZE_2D -> {
                BoundingBox(
                    LngLat(array.getPrimitive(0).double, array.getPrimitive(1).double),
                    LngLat(array.getPrimitive(2).double, array.getPrimitive(3).double)
                )
            }
            ARRAY_SIZE_3D -> {
                BoundingBox(
                    LngLat(
                        array.getPrimitive(0).double,
                        array.getPrimitive(1).double,
                        array.getPrimitiveOrNull(2)?.double
                    ),
                    LngLat(
                        array.getPrimitive(3).double,
                        array.getPrimitive(4).double,
                        array.getPrimitiveOrNull(5)?.double
                    )
                )
            }
            else -> {
                throw SerializationException("Expected array of size 4 or 6. Got array of size ${array.size}")
            }
        }
    }

    override fun serialize(encoder: Encoder, value: BoundingBox) {
        val output = encoder as? JsonOutput ?: throw SerializationException("This class can only be saved as JSON")

        val includeAltitudes = value.southwest.altitude != null && value.northeast.altitude != null
        val array = jsonArray {
            +(value.southwest.longitude as Double?)
            +(value.southwest.latitude as Double?)
            if (includeAltitudes) {
                +value.southwest.altitude
            }

            +(value.northeast.longitude as Double?)
            +(value.northeast.latitude as Double?)
            if (includeAltitudes) {
                +value.northeast.altitude
            }
        }

        output.encodeJson(array)
    }
}
