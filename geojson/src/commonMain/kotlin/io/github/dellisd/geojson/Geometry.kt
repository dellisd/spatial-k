package io.github.dellisd.geojson

import kotlin.jvm.JvmOverloads

sealed class Geometry(final override val bbox: BoundingBox? = null) : GeoJson

class Point @JvmOverloads constructor(val coordinates: Position, bbox: BoundingBox? = null) : Geometry(bbox),
    Position by coordinates {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as Point

        if (coordinates != other.coordinates) return false

        return true
    }

    override fun hashCode(): Int {
        return coordinates.hashCode()
    }

    override fun toString(): String {
        return "Point(coordinates=$coordinates)"
    }

    fun copy(coordinates: Position = this.coordinates): Point = Point(coordinates)
}

class MultiPoint @JvmOverloads constructor(val coordinates: List<Position>, bbox: BoundingBox? = null) : Geometry(bbox)

class LineString @JvmOverloads constructor(val coordinates: List<Position>, bbox: BoundingBox? = null) : Geometry(bbox)

class MultiLineString @JvmOverloads constructor(val coordinates: List<List<Position>>, bbox: BoundingBox? = null) :
    Geometry(bbox)

class Polygon @JvmOverloads constructor(val coordinates: List<List<Position>>, bbox: BoundingBox? = null) :
    Geometry(bbox)

class MultiPolygon @JvmOverloads constructor(val coordinates: List<List<List<Position>>>, bbox: BoundingBox? = null) :
    Geometry(bbox)

class GeometryCollection @JvmOverloads constructor(val geometries: List<Geometry>, bbox: BoundingBox? = null) :
    Geometry(bbox)
