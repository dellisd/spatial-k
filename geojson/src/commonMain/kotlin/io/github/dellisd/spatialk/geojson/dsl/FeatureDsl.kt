@file:JvmName("-FeatureDslKt")
@file:Suppress("MatchingDeclarationName")

package io.github.dellisd.spatialk.geojson.dsl

import io.github.dellisd.spatialk.geojson.BoundingBox
import io.github.dellisd.spatialk.geojson.Feature
import io.github.dellisd.spatialk.geojson.Geometry
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonPrimitive
import kotlin.jvm.JvmName

@GeoJsonDsl
class PropertiesBuilder {
    private val properties = mutableMapOf<String, JsonElement>()

    fun put(key: String, value: String?) {
        properties[key] = JsonPrimitive(value)
    }

    fun put(key: String, value: Number?) {
        properties[key] = JsonPrimitive(value)
    }

    fun put(key: String, value: Boolean?) {
        properties[key] = JsonPrimitive(value)
    }

    fun put(key: String, value: JsonElement) {
        properties[key] = value
    }

    fun build(): Map<String, JsonElement> = properties
}

@GeoJsonDsl
inline fun feature(
    geometry: Geometry? = null,
    id: String? = null,
    bbox: BoundingBox? = null,
    foreignMembers: ForeignMembersBuilder.() -> Unit = {},
    properties: PropertiesBuilder.() -> Unit = {}
) = Feature(
    geometry = geometry,
    properties = PropertiesBuilder().apply(properties).build(),
    id = id,
    bbox = bbox,
    foreignMembers = ForeignMembersBuilder().apply(foreignMembers).build())
