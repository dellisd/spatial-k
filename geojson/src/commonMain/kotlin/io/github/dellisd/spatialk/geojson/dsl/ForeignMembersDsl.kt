package io.github.dellisd.spatialk.geojson.dsl

import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonPrimitive

@GeoJsonDsl
class ForeignMembersBuilder {
    private val foreignMembers = mutableMapOf<String, JsonElement>()

    fun put(key: String, value: String?) {
        foreignMembers[key] = JsonPrimitive(value)
    }

    fun put(key: String, value: Number?) {
        foreignMembers[key] = JsonPrimitive(value)
    }

    fun put(key: String, value: Boolean?) {
        foreignMembers[key] = JsonPrimitive(value)
    }

    fun put(key: String, value: JsonElement) {
        foreignMembers[key] = value
    }

    fun build(): Map<String, JsonElement> = foreignMembers
}