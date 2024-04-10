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
import kotlinx.serialization.json.JsonElement
import kotlin.jvm.JvmName

@GeoJsonDsl
abstract class GeometryDsl<T : Geometry> protected constructor(var bbox: BoundingBox? = null, val foreignMembers: MutableMap<String, JsonElement> = mutableMapOf()) {
    abstract fun create(): T

    fun foreignMembers(foreignMembers: ForeignMembersBuilder.() -> Unit = {}) {
        this.foreignMembers.putAll(ForeignMembersBuilder().apply(foreignMembers).build())
    }
}

@GeoJsonDsl
class PointDsl(private var coordinates: Position) : GeometryDsl<Point>() {
    override fun create(): Point =
        Point(coordinates, bbox, foreignMembers)
}

@GeoJsonDsl
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

@GeoJsonDsl
class MultiPointDsl(private val points: MutableList<Position> = mutableListOf()) : GeometryDsl<MultiPoint>() {
    override fun create(): MultiPoint =
        MultiPoint(points, bbox, foreignMembers)

    operator fun Position.unaryPlus() {
        points.add(this)
    }

    operator fun Point.unaryPlus() {
        points.add(this.coordinates)
    }

    fun point(longitude: Double, latitude: Double, altitude: Double? = null) {
        points.add(Position(longitude, latitude, altitude))
    }
}

@GeoJsonDsl
inline fun multiPoint(block: MultiPointDsl.() -> Unit): MultiPoint = MultiPointDsl()
    .apply(block).create()

@GeoJsonDsl
class LineStringDsl(private val points: MutableList<Position> = mutableListOf()) : GeometryDsl<LineString>() {
    override fun create(): LineString =
        LineString(points, bbox, foreignMembers)

    operator fun Position.unaryPlus() {
        points.add(this)
    }

    operator fun Point.unaryPlus() {
        points.add(this.coordinates)
    }

    fun point(longitude: Double, latitude: Double, altitude: Double? = null) {
        points.add(Position(longitude, latitude, altitude))
    }
}

@GeoJsonDsl
inline fun lineString(block: LineStringDsl.() -> Unit) = LineStringDsl()
    .apply(block).create()

@GeoJsonDsl
class MultiLineStringDsl(private val coordinates: MutableList<List<Position>> = mutableListOf()) :
    GeometryDsl<MultiLineString>() {
    override fun create(): MultiLineString =
        MultiLineString(coordinates, bbox, foreignMembers)

    inline fun lineString(block: LineStringDsl.() -> Unit) {
        +LineStringDsl().apply(block).create()
    }

    operator fun LineString.unaryPlus() {
        this@MultiLineStringDsl.coordinates.add(this.coordinates)
    }
}

@GeoJsonDsl
inline fun multiLineString(block: MultiLineStringDsl.() -> Unit) = MultiLineStringDsl()
    .apply(block).create()

@GeoJsonDsl
class PolygonDsl(internal val coordinates: MutableList<List<Position>> = mutableListOf()) : GeometryDsl<Polygon>() {
    override fun create(): Polygon =
        Polygon(coordinates, bbox, foreignMembers)

    inner class RingDsl(internal val points: MutableList<Position> = mutableListOf()) {
        operator fun Position.unaryPlus() {
            points.add(this)
        }

        operator fun Point.unaryPlus() {
            points.add(this.coordinates)
        }

        inline fun lineString(block: LineStringDsl.() -> Unit) {
            +LineStringDsl().apply(block).create()
        }

        fun point(longitude: Double, latitude: Double, altitude: Double? = null) {
            points.add(Position(longitude, latitude, altitude))
        }

        operator fun LineString.unaryPlus() {
            this@RingDsl.points.addAll(this.coordinates)
        }

        fun complete() {
            points.add(points.first())
        }
    }

    fun ring(block: RingDsl.() -> Unit) {
        coordinates.add(RingDsl().apply(block).points)
    }
}

@GeoJsonDsl
inline fun polygon(block: PolygonDsl.() -> Unit) = PolygonDsl()
    .apply(block).create()

@GeoJsonDsl
class MultiPolygonDsl(private val coordinates: MutableList<List<List<Position>>> = mutableListOf()) :
    GeometryDsl<MultiPolygon>() {
    override fun create(): MultiPolygon =
        MultiPolygon(coordinates, bbox, foreignMembers)

    inline fun polygon(block: PolygonDsl.() -> Unit) {
        +PolygonDsl().apply(block).create()
    }

    operator fun Polygon.unaryPlus() {
        this@MultiPolygonDsl.coordinates.add(this.coordinates)
    }

}

@GeoJsonDsl
inline fun multiPolygon(block: MultiPolygonDsl.() -> Unit) = MultiPolygonDsl()
    .apply(block).create()

@GeoJsonDsl
class GeometryCollectionDsl(private val geometries: MutableList<Geometry> = mutableListOf()) :
    GeometryDsl<GeometryCollection>() {
    override fun create(): GeometryCollection =
        GeometryCollection(geometries, bbox, foreignMembers)

    operator fun Geometry.unaryPlus() {
        geometries.add(this)
    }
}

@GeoJsonDsl
inline fun geometryCollection(block: GeometryCollectionDsl.() -> Unit) = GeometryCollectionDsl()
    .apply(block).create()
