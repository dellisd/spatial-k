package io.github.dellisd.spatialk.geojson

import io.github.dellisd.spatialk.geojson.serialization.GeometrySerializer
import io.github.dellisd.spatialk.geojson.serialization.foreignMembers
import io.github.dellisd.spatialk.geojson.serialization.foreignMembersJsonProps
import io.github.dellisd.spatialk.geojson.serialization.jsonJoin
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
class LineString @JvmOverloads constructor(
    val coordinates: List<Position>,
    bbox: BoundingBox? = null,
    foreignMembers: Map<String, JsonElement> = emptyMap()
) : Geometry(bbox, foreignMembers) {
    override val coordinatesOrGeometries get() = coordinates

    @JvmOverloads
    constructor(
        vararg coordinates: Position,
        bbox: BoundingBox? = null,
        foreignMembers: Map<String, JsonElement> = emptyMap()
    ) : this(coordinates.toList(), bbox, foreignMembers)

    @JvmOverloads
    constructor(
        coordinates: Array<DoubleArray>,
        bbox: BoundingBox? = null,
        foreignMembers: Map<String, JsonElement> = emptyMap()
    ) : this(coordinates.map(::Position), bbox, foreignMembers)

    init {
        require(coordinates.size >= 2) { "LineString must have at least two positions" }
    }

    @Suppress("MaxLineLength")
    override fun json(): String =
        """{"type":"LineString",${bbox.jsonProp()}"coordinates":${coordinates.jsonJoin(transform = Position::json)}${foreignMembersJsonProps()}}"""

    companion object {
        @JvmStatic
        public fun fromJson(json: String): LineString = fromJson(Json.decodeFromString(JsonObject.serializer(), json))

        @JvmStatic
        public fun fromJsonOrNull(json: String): LineString? = try {
            fromJson(json)
        } catch (_: Exception) {
            null
        }

        @JvmStatic
        public fun fromJson(json: JsonObject): LineString {
            require(json.getValue("type").jsonPrimitive.content == "LineString") {
                "Object \"type\" is not \"LineString\"."
            }

            val coords = json.getValue("coordinates").jsonArray.map { it.jsonArray.toPosition() }
            val bbox = json["bbox"]?.jsonArray?.toBbox()
            val foreignMembers = json.foreignMembers()

            return LineString(coords, bbox, foreignMembers)
        }
    }
}
