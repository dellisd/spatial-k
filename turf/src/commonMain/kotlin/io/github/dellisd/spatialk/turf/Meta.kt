@file:JvmName("TurfMeta")

package io.github.dellisd.spatialk.turf

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

fun Geometry.coordEach(block: (Position) -> Unit) {
    when (this) {
        is Point -> block(coordinates)
        is MultiPoint -> coordinates.forEach(block)
        is LineString -> coordinates.forEach(block)
        is MultiLineString -> coordinates.forEach { line -> line.forEach(block) }
        is Polygon -> coordinates.forEach { ring -> ring.forEach(block) }
        is MultiPolygon -> coordinates.forEach { polygon -> polygon.forEach { ring -> ring.forEach(block) } }
        is GeometryCollection -> geometries.forEach { geometry -> geometry.coordEach(block) }
    }
}
