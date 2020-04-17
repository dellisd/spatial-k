@file:JvmName("Measurement")

package io.github.dellisd.turf

import io.github.dellisd.geojson.Geometry
import io.github.dellisd.geojson.GeometryCollection
import io.github.dellisd.geojson.LineString
import io.github.dellisd.geojson.LngLat
import io.github.dellisd.geojson.MultiPolygon
import io.github.dellisd.geojson.Polygon
import io.github.dellisd.geojson.Position
import kotlin.jvm.JvmName
import kotlin.jvm.JvmOverloads
import kotlin.math.abs
import kotlin.math.asin
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

/**
 * Takes a [LineString] and returns a [position][LngLat] at a specified distance along the line.
 *
 * @param line input line
 * @param distance distance along the line
 * @param units units of [distance]
 * @return A position [distance] [units] along the line
 */
@JvmOverloads
@Suppress("MagicNumber")
fun along(line: LineString, distance: Double, units: Units = Units.Kilometers): Position {
    var travelled = 0.0

    line.coordinates.forEachIndexed { i, coordinate ->
        when {
            distance >= travelled && i == line.coordinates.size - 1 -> {
            }
            travelled >= distance -> {
                val overshot = distance - travelled
                return if (overshot == 0.0) coordinate
                else {
                    val direction = bearing(coordinate, line.coordinates[i - 1]) - 180
                    destination(coordinate, overshot, direction, units)
                }
            }
            else -> travelled += distance(coordinate, line.coordinates[i + 1], units)
        }
    }

    return line.coordinates[line.coordinates.size - 1]
}

/**
 * Takes a geometry and returns its area in square meters.
 *
 * @param geometry input geometry
 * @return area in square meters
 */
fun area(geometry: Geometry): Double {
    return when (geometry) {
        is GeometryCollection -> geometry.geometries.fold(0.0) { acc, geom -> acc + area(geom) }
        else -> calculateArea(geometry)
    }
}

/**
 * Takes multiple geometries and returns their area in square meters.
 *
 * @param geometry input geometries
 * @return area in square meters
 */
fun area(geometry: Iterable<Geometry>): Double = geometry.fold(0.0) { acc, geom -> acc + area(geom) }

private fun calculateArea(geometry: Geometry): Double {
    return when (geometry) {
        is Polygon -> polygonArea(geometry.coordinates)
        is MultiPolygon -> geometry.coordinates.fold(0.0) { acc, coords -> acc + polygonArea(coords) }
        else -> 0.0
    }
}

private fun polygonArea(coordinates: List<List<Position>>): Double {
    var total = 0.0
    if (coordinates.isNotEmpty()) {
        total += abs(ringArea(coordinates[0]))
        for (i in 1 until coordinates.size) {
            total -= abs(ringArea(coordinates[i]))
        }
    }
    return total
}

/**
 * Calculates the approximate area of the [polygon][coordinates] were it projected onto the earth.
 * Note that this area will be positive if ring is oriented clockwise, otherwise it will be negative.
 *
 * Reference:
 * Robert. G. Chamberlain and William H. Duquette, "Some Algorithms for Polygons on a Sphere",
 * JPL Publication 07-03, Jet Propulsion
 * Laboratory, Pasadena, CA, June 2007 https://trs.jpl.nasa.gov/handle/2014/40409
 */
private fun ringArea(coordinates: List<Position>): Double {
    var p1: Position
    var p2: Position
    var p3: Position
    var lowerIndex: Int
    var middleIndex: Int
    var upperIndex: Int
    var total = 0.0

    if (coordinates.size > 2) {
        for (i in coordinates.indices) {
            when (i) {
                coordinates.size - 2 -> {
                    lowerIndex = coordinates.size - 2
                    middleIndex = coordinates.size - 1
                    upperIndex = 0
                }
                coordinates.size - 1 -> {
                    lowerIndex = coordinates.size - 1
                    middleIndex = 0
                    upperIndex = 1
                }
                else -> {
                    lowerIndex = i
                    middleIndex = i + 1
                    upperIndex = i + 2
                }
            }
            p1 = coordinates[lowerIndex]
            p2 = coordinates[middleIndex]
            p3 = coordinates[upperIndex]
            total = (radians(p3.longitude) - radians(p1.longitude)) * sin(radians(p2.latitude))
        }
        total = total * EARTH_RADIUS * EARTH_RADIUS / 2
    }
    return total
}

/**
 * Takes two positions ([start], [end]) and finds the geographic bearing between them,
 * i.e. the angle measured in degrees from the north line (0 degrees)
 *
 * @param start starting point
 * @param end ending point
 * @param final calculates the final bearing if true
 * @return bearing in decimal degrees, between -180 and 180 degrees (positive clockwise)
 */
@JvmOverloads
fun bearing(start: Position, end: Position, final: Boolean = false): Double {
    if (final) return finalBearing(start, end)

    val lon1 = radians(start.longitude)
    val lon2 = radians(end.longitude)
    val lat1 = radians(start.latitude)
    val lat2 = radians(end.latitude)

    val a = sin(lon2 - lon1) * cos(lat2)
    val b = cos(lat1) * sin(lat2) - sin(lat1) * cos(lat2) * cos(lon2 - lon1)

    return degrees(atan2(a, b))
}
@Suppress("MagicNumber")
internal fun finalBearing(start: Position, end: Position): Double = (bearing(end, start) + 180) % 360

/**
 * Takes a [position][origin] and calculates the location of a destination position given a distance in
 * degrees, radians, miles, or kilometers; and bearing in degrees.
 * This uses the Haversine formula to account for global curvature.
 *
 * @param origin starting point
 * @param distance distance from the origin point
 * @param bearing ranging from -180 to 180
 * @param units Unit of [distance]
 * @return destination position
 *
 * @see <a href="https://en.wikipedia.org/wiki/Haversine_formula">Haversine formula</a>
 */
@JvmOverloads
fun destination(origin: Position, distance: Double, bearing: Double, units: Units = Units.Kilometers): Position {
    val longitude1 = radians(origin.longitude)
    val latitude1 = radians(origin.latitude)
    val bearingRad = radians(bearing)
    val radians = lengthToRadians(distance, units)

    val latitude2 = asin(sin(latitude1) * cos(radians) + cos(latitude1) * sin(radians) * cos(bearingRad))
    val longitude2 = longitude1 + atan2(
        sin(bearingRad) * sin(radians) * cos(latitude1),
        cos(radians) - sin(latitude1) * sin(latitude2)
    )

    return LngLat(degrees(longitude2), degrees(latitude2))
}

/**
 * Calculates the distance between two positions.
 * This uses the Haversine formula to account for global curvature.
 *
 * @param from origin point
 * @param to destination point
 * @param units units of returned distance
 * @return distance between the two points in [units]
 *
 * @see <a href="https://en.wikipedia.org/wiki/Haversine_formula">Haversine formula</a>
 */
@JvmOverloads
fun distance(from: Position, to: Position, units: Units = Units.Kilometers): Double {
    val dLat = radians(to.latitude - from.latitude)
    val dLon = radians(to.longitude - from.longitude)
    val lat1 = radians(from.latitude)
    val lat2 = radians(to.latitude)

    val a = sin(dLat / 2).pow(2) + sin(dLon / 2).pow(2) * cos(lat1) * cos(lat2)
    return radiansToLength(2 * atan2(sqrt(a), sqrt(1 - a)), units)
}
