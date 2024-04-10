package io.github.dellisd.spatialk.geojson

import io.github.dellisd.spatialk.geojson.serialization.GeometrySerializer
import io.github.dellisd.spatialk.geojson.serialization.foreignMembers
import io.github.dellisd.spatialk.geojson.serialization.foreignMembersJsonProps
import io.github.dellisd.spatialk.geojson.serialization.jsonProp
import io.github.dellisd.spatialk.geojson.serialization.toBbox
import io.github.dellisd.spatialk.geojson.serialization.toPosition
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonPrimitive
import kotlin.jvm.JvmOverloads
import kotlin.jvm.JvmStatic

@Suppress("SERIALIZER_TYPE_INCOMPATIBLE")
@Serializable(with = GeometrySerializer::class)
class Point @JvmOverloads constructor(
    val coordinates: Position,
    bbox: BoundingBox? = null,
    foreignMembers: Map<String, JsonElement> = emptyMap()
) : Geometry(bbox, foreignMembers) {
    override val coordinatesOrGeometries get() = coordinates

    @JvmOverloads
    constructor(
        coordinates: DoubleArray,
        bbox: BoundingBox? = null,
        foreignMembers: Map<String, JsonElement> = emptyMap()
    ) : this(Position(coordinates), bbox, foreignMembers)

    override fun json(): String = """{"type":"Point",${bbox.jsonProp()}"coordinates":${coordinates.json()}${foreignMembersJsonProps()}}"""

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
        public fun fromJson(json: JsonObject): Point {
            require(json.getValue("type").jsonPrimitive.content == "Point") {
                "Object \"type\" is not \"Point\"."
            }

            val coords = json.getValue("coordinates").jsonArray.toPosition()
            val bbox = json["bbox"]?.jsonArray?.toBbox()
            val foreignMembers = json.foreignMembers()

            return Point(coords, bbox, foreignMembers)
        }
    }
}
