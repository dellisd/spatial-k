package io.github.dellisd.spatialk.geojson.serialization

import io.github.dellisd.spatialk.geojson.BoundingBox
import io.github.dellisd.spatialk.geojson.Feature
import io.github.dellisd.spatialk.geojson.GeoJson
import io.github.dellisd.spatialk.geojson.Position
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.buildJsonArray
import kotlinx.serialization.json.double
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonPrimitive

internal fun DoubleArray.jsonJoin(transform: ((Double) -> CharSequence)? = null) =
    joinToString(separator = ",", prefix = "[", postfix = "]", transform = transform)

internal fun <T> Iterable<T>.jsonJoin(transform: ((T) -> CharSequence)? = null) =
    joinToString(separator = ",", prefix = "[", postfix = "]", transform = transform)

internal fun BoundingBox?.jsonProp(): String = if (this == null) "" else """"bbox":${this.json()},"""

internal fun Feature.idProp(): String = if (this.id == null) "" else """"id":"${this.id}","""

internal fun GeoJson.foreignMembersJsonProps(): String {
    if (this.foreignMembers.isEmpty()) return ""
    return this.foreignMembers.entries.joinToString(prefix = ",", separator = ",") {
        """"${it.key}":${Json.encodeToString(JsonElement.serializer(), it.value)}"""
    }
}

internal fun JsonArray.toPosition(): Position =
    Position(this[0].jsonPrimitive.double, this[1].jsonPrimitive.double, this.getOrNull(2)?.jsonPrimitive?.double)

internal fun JsonArray.toBbox(): BoundingBox =
    BoundingBox(this.map { it.jsonPrimitive.double }.toDoubleArray())

private val FEATURE_MEMBERS = listOf("type", "geometry", "properties", "id", "bbox")
private val FEATURE_COLLECTION_MEMBERS = listOf("type", "features", "bbox")
private val GEOMETRY_COLLECTION_MEMBERS = listOf("type", "geometries", "bbox")
private val GEOMETRY_ELEMENT_MEMBERS = listOf("type", "coordinates", "bbox")

internal fun JsonObject.foreignMembers(): Map<String, JsonElement> {
    val knownMembers = when(this.getValue("type").jsonPrimitive.content) {
        "Feature"            -> FEATURE_MEMBERS
        "FeatureCollection"  -> FEATURE_COLLECTION_MEMBERS
        "GeometryCollection" -> GEOMETRY_COLLECTION_MEMBERS
        else                 -> GEOMETRY_ELEMENT_MEMBERS
    }
    return this.filterKeys { !knownMembers.contains(it) }
}

