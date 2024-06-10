package io.github.dellisd.spatialk.geojson

import io.github.dellisd.spatialk.geojson.serialization.GeometrySerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlin.jvm.JvmStatic

@Serializable(with = GeometrySerializer::class)
sealed class Geometry protected constructor(
    override val bbox: BoundingBox? = null,
    override val foreignMembers: Map<String, JsonElement> = emptyMap()
) : GeoJson {

    protected abstract val coordinatesOrGeometries: Any

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as Geometry

        if (coordinatesOrGeometries != other.coordinatesOrGeometries) return false
        if (bbox != other.bbox) return false
        if (foreignMembers != other.foreignMembers) return false

        return true
    }

    override fun hashCode(): Int {
        var result = coordinatesOrGeometries.hashCode()
        result = 31 * result + (bbox?.hashCode() ?: 0)
        result = 31 * result + foreignMembers.hashCode()
        return result
    }

    override fun toString(): String = json()

    companion object {
        @JvmStatic
        public fun fromJson(json: String): Geometry = fromJson(Json.decodeFromString(JsonObject.serializer(), json))

        @JvmStatic
        public fun fromJsonOrNull(json: String): Geometry? = try {
            fromJson(json)
        } catch (_: Exception) {
            null
        }

        @JvmStatic
        public fun fromJson(json: JsonObject): Geometry =
            when (val type = json.getValue("type").jsonPrimitive.content) {
                "Point" -> Point.fromJson(json)
                "MultiPoint" -> MultiPoint.fromJson(json)
                "LineString" -> LineString.fromJson(json)
                "MultiLineString" -> MultiLineString.fromJson(json)
                "Polygon" -> Polygon.fromJson(json)
                "MultiPolygon" -> MultiPolygon.fromJson(json)
                "GeometryCollection" -> GeometryCollection.fromJson(json)
                else -> throw IllegalArgumentException("Unsupported Geometry type \"$type\"")
            }
    }
}



