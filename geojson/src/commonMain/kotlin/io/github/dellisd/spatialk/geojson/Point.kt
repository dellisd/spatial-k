package io.github.dellisd.spatialk.geojson

import io.github.dellisd.spatialk.geojson.serialization.GeometrySerializer
import io.github.dellisd.spatialk.geojson.serialization.jsonProp
import io.github.dellisd.spatialk.geojson.serialization.toBbox
import io.github.dellisd.spatialk.geojson.serialization.toPosition
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonPrimitive
import kotlin.jvm.JvmOverloads
import kotlin.jvm.JvmStatic

@Suppress("SERIALIZER_TYPE_INCOMPATIBLE")
@Serializable(with = GeometrySerializer::class)
class Point @JvmOverloads constructor(val coordinates: Position, override val bbox: BoundingBox? = null) : Geometry() {
    @JvmOverloads
    constructor(coordinates: DoubleArray, bbox: BoundingBox? = null) : this(Position(coordinates), bbox)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as Point

        if (coordinates != other.coordinates) return false
        if (bbox != other.bbox) return false

        return true
    }

    override fun hashCode(): Int {
        var result = coordinates.hashCode()
        result = 31 * result + (bbox?.hashCode() ?: 0)
        return result
    }

    override fun json(): String = """{"type":"Point",${bbox.jsonProp()}"coordinates":${coordinates.json()}}"""

    companion object {
        @JvmStatic
        public fun fromJson(json: String): Point = fromJson(Json.decodeFromString(JsonObject.serializer(), json))

        @JvmStatic
        public fun fromJsonOrNull(json: String): Point? = try {
            fromJson(json)
        } catch (_: Exception) {
            null
        }

        @JvmStatic
        internal fun fromJson(json: JsonObject): Point {
            if (json.getValue("type").jsonPrimitive.content != "Point") {
                throw IllegalArgumentException("Object \"type\" is not \"Point\".")
            }

            val coords = json.getValue("coordinates").jsonArray.toPosition()
            val bbox = json["bbox"]?.jsonArray?.toBbox()

            return Point(coords, bbox)
        }
    }
}
