@file:JvmName("-GeometryDslKt")

package io.github.dellisd.spatialk.geojson.dsl

import io.github.dellisd.spatialk.geojson.BoundingBox
import io.github.dellisd.spatialk.geojson.Geometry
import io.github.dellisd.spatialk.geojson.GeometryCollection
import io.github.dellisd.spatialk.geojson.LineString
import io.github.dellisd.spatialk.geojson.Position
import io.github.dellisd.spatialk.geojson.MultiLineString
import io.github.dellisd.spatialk.geojson.MultiPoint
import io.github.dellisd.spatialk.geojson.MultiPolygon
import io.github.dellisd.spatialk.geojson.Point
import io.github.dellisd.spatialk.geojson.Polygon
import kotlin.jvm.JvmName

@GeoJsonDsl
abstract class GeometryDsl<T : Geometry> protected constructor(var bbox: BoundingBox? = null) {
    abstract fun create(): T
}

class PointDsl(private var coordinates: Position) : GeometryDsl<Point>() {
    override fun create(): Point =
        Point(coordinates, bbox)
}

inline fun point(
    longitude: Double,
    latitude: Double,
    altitude: Double? = null,
    block: PointDsl.() -> Unit = {}
): Point =
    PointDsl(
        Position(
            longitude,
            latitude,
            altitude
        )
    ).apply(block).create()

class MultiPointDsl(private val points: MutableList<Position> = mutableListOf()) : GeometryDsl<MultiPoint>() {
    override fun create(): MultiPoint =
        MultiPoint(points, bbox)

    operator fun Position.unaryPlus() {
        points.add(this)
    }

    operator fun Point.unaryPlus() {
        points.add(this.coordinates)
    }
}

inline fun multiPoint(block: MultiPointDsl.() -> Unit): MultiPoint = MultiPointDsl()
    .apply(block).create()

class LineStringDsl(internal val points: MutableList<Position> = mutableListOf()) : GeometryDsl<LineString>() {
    override fun create(): LineString =
        LineString(points, bbox)

    operator fun Position.unaryPlus() {
        points.add(this)
    }
}

inline fun lineString(block: LineStringDsl.() -> Unit) = LineStringDsl()
    .apply(block).create()

class MultiLineStringDsl(private val coordinates: MutableList<List<Position>> = mutableListOf()) :
    GeometryDsl<MultiLineString>() {
    override fun create(): MultiLineString =
        MultiLineString(coordinates)

    inline fun lineString(block: LineStringDsl.() -> Unit): LineStringDsl = LineStringDsl()
        .apply(block)

    operator fun LineString.unaryPlus() {
        this@MultiLineStringDsl.coordinates.add(this.coordinates)
    }

    operator fun LineStringDsl.unaryPlus() {
        this@MultiLineStringDsl.coordinates.add(this.points)
    }
}

inline fun multiLineString(block: MultiLineStringDsl.() -> Unit) = MultiLineStringDsl()
    .apply(block).create()

class PolygonDsl(internal val coordinates: MutableList<List<Position>> = mutableListOf()) : GeometryDsl<Polygon>() {
    override fun create(): Polygon =
        Polygon(coordinates, bbox)

    inner class RingDsl(internal val points: MutableList<Position> = mutableListOf()) {
        operator fun Position.unaryPlus() {
            points.add(this)
        }

        operator fun LineString.unaryPlus() {
            this@RingDsl.points.addAll(this.coordinates)
        }

        operator fun LineStringDsl.unaryPlus() {
            this@RingDsl.points.addAll(this.points)
        }

        fun complete() {
            points.add(points.first())
        }
    }

    fun ring(block: RingDsl.() -> Unit) {
        coordinates.add(RingDsl().apply(block).points)
    }
}

inline fun polygon(block: PolygonDsl.() -> Unit) = PolygonDsl()
    .apply(block).create()

class MultiPolygonDsl(private val coordinates: MutableList<List<List<Position>>> = mutableListOf()) :
    GeometryDsl<MultiPolygon>() {
    override fun create(): MultiPolygon =
        MultiPolygon(coordinates, bbox)

    inline fun polygon(block: PolygonDsl.() -> Unit) = PolygonDsl()
        .apply(block)

    operator fun Polygon.unaryPlus() {
        this@MultiPolygonDsl.coordinates.add(this.coordinates)
    }

    operator fun PolygonDsl.unaryPlus() {
        this@MultiPolygonDsl.coordinates.add(this.coordinates)
    }
}

inline fun multiPolygon(block: MultiPolygonDsl.() -> Unit) = MultiPolygonDsl()
    .apply(block).create()

class GeometryCollectionDsl(private val geometries: MutableList<Geometry> = mutableListOf()) :
    GeometryDsl<GeometryCollection>() {
    override fun create(): GeometryCollection =
        GeometryCollection(geometries)

    operator fun Geometry.unaryPlus() {
        geometries.add(this)
    }
}

inline fun geometryCollection(block: GeometryCollectionDsl.() -> Unit) = GeometryCollectionDsl()
    .apply(block).create()
