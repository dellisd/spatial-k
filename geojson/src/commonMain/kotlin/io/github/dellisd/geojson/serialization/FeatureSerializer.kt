package io.github.dellisd.geojson.serialization

import io.github.dellisd.geojson.Feature
import kotlinx.serialization.Decoder
import kotlinx.serialization.Encoder
import kotlinx.serialization.KSerializer
import kotlinx.serialization.PrimitiveDescriptor
import kotlinx.serialization.PrimitiveKind
import kotlinx.serialization.SerialDescriptor
import kotlinx.serialization.Serializer

@Serializer(forClass = Feature::class)
object FeatureSerializer : KSerializer<Feature> {
    override val descriptor: SerialDescriptor = PrimitiveDescriptor("Feature", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): Feature {
        TODO("Not yet implemented")
    }

    override fun serialize(encoder: Encoder, value: Feature) {
        TODO("Not yet implemented")
    }
}
