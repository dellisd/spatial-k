@file:JvmName("GeometryFactory")

package io.github.dellisd.spatialk.geojson

import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import kotlin.jvm.JvmName

/**
 * Converts a JSON string to a Geometry object.
 *
 * @receiver The JSON string to convert.
 * @return One of the seven types of [Geometry].
 *
 * @throws SerializationException if the string could not be deserialized to a Geometry object.
 */
@Suppress("UNCHECKED_CAST")
@JvmName("fromJson")
@Deprecated("Replace with T.fromJson()", replaceWith = ReplaceWith("T.fromJson(this)"))
fun <T : Geometry> String.toGeometry(): T = Json.decodeFromString(Geometry.serializer(), this) as T

/**
 * Converts a JSON string to a Geometry object.
 *
 * @receiver The JSON string to convert.
 * @return One of the seven types of [Geometry], or null if it could not be converted
 */
@Suppress("UNCHECKED_CAST")
@JvmName("fromJsonOrNull")
@Deprecated("Replace with T.fromJsonOrNull()", replaceWith = ReplaceWith("T.fromJsonOrNull(this)"))
fun <T : Geometry> String.toGeometryOrNull(): T? = try {
    Json.decodeFromString(Geometry.serializer(), this) as? T
} catch (_: SerializationException) {
    null
} catch (_: IllegalArgumentException) {
    null
}
