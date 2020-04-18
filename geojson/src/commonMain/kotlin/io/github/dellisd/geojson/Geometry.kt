package io.github.dellisd.geojson

import io.github.dellisd.geojson.serialization.GeometrySerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.UnstableDefault
import kotlinx.serialization.json.Json
import kotlin.jvm.JvmName
import kotlin.jvm.JvmOverloads

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
    Geometry(bbox) {
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
