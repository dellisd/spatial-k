@file:JvmName("TurfMeta")

package io.github.dellisd.turf

import io.github.dellisd.geojson.Geometry
import io.github.dellisd.geojson.GeometryCollection
import io.github.dellisd.geojson.LineString
import io.github.dellisd.geojson.MultiLineString
import io.github.dellisd.geojson.MultiPoint
import io.github.dellisd.geojson.MultiPolygon
import io.github.dellisd.geojson.Point
import io.github.dellisd.geojson.Polygon
import io.github.dellisd.geojson.Position
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
