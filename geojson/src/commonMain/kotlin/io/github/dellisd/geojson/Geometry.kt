package io.github.dellisd.geojson

sealed class Geometry

class Point(val coordinates: Position) : Geometry(), Position by coordinates {
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

data class MultiPoint(val coordinates: List<Position>) : Geometry()

data class LineString(val coordinates: List<Position>) : Geometry()

data class MultiLineString(val coordinates: List<List<Position>>) : Geometry()

data class Polygon(val coordinates: List<List<Position>>) : Geometry()

data class MultiPolygon(val coordinates: List<List<List<Position>>>) : Geometry()

data class GeometryCollection(val geometries: List<Geometry>) : Geometry()