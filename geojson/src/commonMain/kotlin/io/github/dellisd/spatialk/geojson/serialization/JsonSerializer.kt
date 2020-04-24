package io.github.dellisd.spatialk.geojson.serialization

import kotlinx.serialization.Decoder
import kotlinx.serialization.Encoder
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.JsonInput
import kotlinx.serialization.json.JsonOutput

internal interface JsonSerializer<T> : KSerializer<T> {
    override fun deserialize(decoder: Decoder): T {
        val input = decoder as? JsonInput ?: throw SerializationException("This class can only be loaded from JSON")
        return deserialize(input)
    }

    override fun serialize(encoder: Encoder, value: T) {
        val output = encoder as? JsonOutput ?: throw SerializationException("This class can only be saved as JSON")
        serialize(output, value)
    }

    fun deserialize(input: JsonInput): T
    fun serialize(output: JsonOutput, value: T)
}
