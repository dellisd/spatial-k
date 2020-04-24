@file:JvmName("-GeometryDslKt")

package io.github.dellisd.spatialk.geojson.dsl

import io.github.dellisd.spatialk.geojson.BoundingBox
import io.github.dellisd.spatialk.geojson.Geometry
import io.github.dellisd.spatialk.geojson.GeometryCollection
import io.github.dellisd.spatialk.geojson.LineString
import io.github.dellisd.spatialk.geojson.LngLat
import io.github.dellisd.spatialk.geojson.MultiLineString
import io.github.dellisd.spatialk.geojson.MultiPoint
import io.github.dellisd.spatialk.geojson.MultiPolygon
import io.github.dellisd.spatialk.geojson.Point
import io.github.dellisd.spatialk.geojson.Polygon
import io.github.dellisd.spatialk.geojson.Position
import kotlin.jvm.JvmName

// Copies a Position into a LngLat. Mostly for turning a Point into a LngLat.
private val Position.lngLat get() = LngLat(
    longitude,
    latitude,
    altitude
)

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
        LngLat(
            longitude,
            latitude,
            altitude
        )
    ).apply(block).create()

class MultiPointDsl(private val coordinates: MutableList<Position> = mutableListOf()) : GeometryDsl<MultiPoint>() {
    override fun create(): MultiPoint =
        MultiPoint(coordinates, bbox)

    operator fun Position.unaryPlus() {
        coordinates.add(this.lngLat)
    }
}

inline fun multiPoint(block: MultiPointDsl.() -> Unit): MultiPoint = MultiPointDsl()
    .apply(block).create()

class LineStringDsl(internal val coordinates: MutableList<Position> = mutableListOf()) : GeometryDsl<LineString>() {
    override fun create(): LineString =
        LineString(coordinates, bbox)

    operator fun Position.unaryPlus() {
        coordinates.add(this.lngLat)
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
        this@MultiLineStringDsl.coordinates.add(this.coordinates)
    }
}

inline fun multiLineString(block: MultiLineStringDsl.() -> Unit) = MultiLineStringDsl()
    .apply(block).create()

class PolygonDsl(internal val coordinates: MutableList<List<Position>> = mutableListOf()) : GeometryDsl<Polygon>() {
    override fun create(): Polygon =
        Polygon(coordinates, bbox)

    inner class RingDsl(internal val coordinates: MutableList<Position> = mutableListOf()) {
        operator fun Position.unaryPlus() {
            coordinates.add(this)
        }

        operator fun LineString.unaryPlus() {
            this@RingDsl.coordinates.addAll(this.coordinates)
        }

        operator fun LineStringDsl.unaryPlus() {
            this@RingDsl.coordinates.addAll(this.coordinates)
        }

        fun complete() {
            coordinates.add(coordinates[0])
        }
    }

    fun ring(block: RingDsl.() -> Unit) {
        coordinates.add(RingDsl().apply(block).coordinates)
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
