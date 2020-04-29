package io.github.dellisd.spatialk.geojson

import io.github.dellisd.spatialk.geojson.serialization.GeometrySerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.UnstableDefault
import kotlinx.serialization.json.Json
import kotlin.jvm.JvmName
import kotlin.jvm.JvmOverloads

private fun DoubleArray.toLngLat() = LngLat(this[0], this[1], this.getOrNull(2))

@Serializable(with = GeometrySerializer::class)
sealed class Geometry(final override val bbox: BoundingBox? = null) : GeoJson {
    @UnstableDefault
    @Suppress("INAPPLICABLE_JVM_NAME")
    @get:JvmName("toJson")
    override val json: String
        get() = Json.stringify(serializer(), this)

    @UnstableDefault
    override fun toString(): String = json
}

class Point @JvmOverloads constructor(val coordinates: Position, bbox: BoundingBox? = null) : Geometry(bbox),
    Position by coordinates {

    @JvmOverloads
    constructor(coordinates: DoubleArray, bbox: BoundingBox? = null) : this(coordinates.toLngLat(), bbox)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as Point

        if (coordinates != other.coordinates) return false

        return true
    }

    override fun hashCode(): Int = coordinates.hashCode()
}

class MultiPoint @JvmOverloads constructor(val coordinates: List<Position>, bbox: BoundingBox? = null) :
    Geometry(bbox) {
    @JvmOverloads
    constructor(vararg coordinates: Position, bbox: BoundingBox? = null) : this(coordinates.toList(), bbox)

    @JvmOverloads
    constructor(
        coordinates: Array<DoubleArray>,
        bbox: BoundingBox? = null
    ) : this(coordinates.map(DoubleArray::toLngLat), bbox)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as MultiPoint

        if (coordinates != other.coordinates) return false

        return true
    }

    override fun hashCode(): Int = coordinates.hashCode()
}

class LineString @JvmOverloads constructor(val coordinates: List<Position>, bbox: BoundingBox? = null) :
    Geometry(bbox) {
    @JvmOverloads
    constructor(vararg coordinates: Position, bbox: BoundingBox? = null) : this(coordinates.toList(), bbox)

    @JvmOverloads
    constructor(
        coordinates: Array<DoubleArray>,
        bbox: BoundingBox? = null
    ) : this(coordinates.map(DoubleArray::toLngLat), bbox)

    init {
        if (coordinates.size < 2) {
            throw IllegalArgumentException("LineString must have at least two positions")
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as LineString

        if (coordinates != other.coordinates) return false

        return true
    }

    override fun hashCode(): Int = coordinates.hashCode()
}

class MultiLineString @JvmOverloads constructor(val coordinates: List<List<Position>>, bbox: BoundingBox? = null) :
    Geometry(bbox) {
    @JvmOverloads
    constructor(vararg coordinates: List<Position>, bbox: BoundingBox? = null) : this(coordinates.toList(), bbox)

    @JvmOverloads
    constructor(
        coordinates: Array<Array<DoubleArray>>,
        bbox: BoundingBox? = null
    ) : this(coordinates.map { it.map(DoubleArray::toLngLat) }, bbox)

    init {
        coordinates.forEach { line ->
            if (line.size < 2) {
                throw IllegalArgumentException("LineString must have at least two positions")
            }
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as MultiLineString

        if (coordinates != other.coordinates) return false

        return true
    }

    override fun hashCode(): Int = coordinates.hashCode()
}

class Polygon @JvmOverloads constructor(val coordinates: List<List<Position>>, bbox: BoundingBox? = null) :
    Geometry(bbox) {
    @JvmOverloads
    constructor(vararg coordinates: List<Position>, bbox: BoundingBox? = null) : this(coordinates.toList(), bbox)

    @JvmOverloads
    constructor(
        coordinates: Array<Array<DoubleArray>>,
        bbox: BoundingBox? = null
    ) : this(coordinates.map { it.map(DoubleArray::toLngLat) }, bbox)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as Polygon

        if (coordinates != other.coordinates) return false

        return true
    }

    override fun hashCode(): Int = coordinates.hashCode()
}

class MultiPolygon @JvmOverloads constructor(val coordinates: List<List<List<Position>>>, bbox: BoundingBox? = null) :
    Geometry(bbox) {
    @JvmOverloads
    constructor(vararg coordinates: List<List<Position>>, bbox: BoundingBox? = null) : this(coordinates.toList(), bbox)

    @JvmOverloads
    constructor(
        coordinates: Array<Array<Array<DoubleArray>>>,
        bbox: BoundingBox? = null
    ) : this(coordinates.map { ring -> ring.map { it.map(DoubleArray::toLngLat) } }, bbox)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as MultiPolygon

        if (coordinates != other.coordinates) return false

        return true
    }

    override fun hashCode(): Int = coordinates.hashCode()
}

class GeometryCollection @JvmOverloads constructor(val geometries: List<Geometry>, bbox: BoundingBox? = null) :
    Geometry(bbox), Collection<Geometry> by geometries {
    @JvmOverloads
    constructor(vararg geometries: Geometry, bbox: BoundingBox? = null) : this(geometries.toList(), bbox)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as GeometryCollection

        if (geometries != other.geometries) return false

        return true
    }

    override fun hashCode(): Int = geometries.hashCode()
}
