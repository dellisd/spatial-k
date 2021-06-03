@file:JvmName("TurfMeta")

package io.github.dellisd.spatialk.turf

import io.github.dellisd.spatialk.geojson.Feature
import io.github.dellisd.spatialk.geojson.FeatureCollection
import io.github.dellisd.spatialk.geojson.Geometry
import io.github.dellisd.spatialk.geojson.GeometryCollection
import io.github.dellisd.spatialk.geojson.LineString
import io.github.dellisd.spatialk.geojson.MultiLineString
import io.github.dellisd.spatialk.geojson.MultiPoint
import io.github.dellisd.spatialk.geojson.MultiPolygon
import io.github.dellisd.spatialk.geojson.Point
import io.github.dellisd.spatialk.geojson.Polygon
import io.github.dellisd.spatialk.geojson.Position
import kotlin.jvm.JvmName

@ExperimentalTurfApi
fun Geometry.coordAll(): List<Position> = when (this) {
    is Point -> this.coordAll()
    is MultiPoint -> this.coordAll()
    is LineString -> this.coordAll()
    is MultiLineString -> this.coordAll()
    is Polygon -> this.coordAll()
    is MultiPolygon -> this.coordAll()
    is GeometryCollection -> this.coordAll()
}

@ExperimentalTurfApi
fun Point.coordAll() = listOf(coordinates)

@ExperimentalTurfApi
fun MultiPoint.coordAll() = coordinates

@ExperimentalTurfApi
fun LineString.coordAll() = coordinates

@ExperimentalTurfApi
fun MultiLineString.coordAll() = coordinates.reduce { acc, list -> acc + list }

@ExperimentalTurfApi
fun Polygon.coordAll() = coordinates.reduce { acc, list -> acc + list }

@ExperimentalTurfApi
fun MultiPolygon.coordAll() =
    coordinates.fold(emptyList<Position>()) { acc, list ->
        list.reduce { innerAcc, innerList -> innerAcc + innerList } + acc
    }

@ExperimentalTurfApi
fun GeometryCollection.coordAll() =
    geometries.fold(emptyList<Position>()) { acc, geometry -> acc + geometry.coordAll() }

@ExperimentalTurfApi
fun Feature.coordAll() = geometry?.coordAll()

@ExperimentalTurfApi
fun FeatureCollection.coordAll() =
    features.fold(emptyList<Position>()) { acc, feature -> acc + (feature.coordAll() ?: emptyList()) }
