@file:JvmName("Measurement")

package io.github.dellisd.turf

import io.github.dellisd.turf.geojson.LineString
import io.github.dellisd.turf.geojson.LngLat
import kotlin.jvm.JvmName
import kotlin.math.*

/**
 * Takes a [LineString] and returns a [position][LngLat] at a specified distance along the line.
 *
 * @param line input line
 * @param distance distance along the line
 * @param units units of [distance]
 * @return A position [distance] [units] along the line
 */
fun along(line: LineString, distance: Double, units: Units = Units.Kilometers): LngLat {
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
 * Takes two positions ([start], [end]) and finds the geographic bearing between them,
 * i.e. the angle measured in degrees from the north line (0 degrees)
 *
 * @param start starting point
 * @param end ending point
 * @param final calculates the final bearing if true
 * @return bearing in decimal degrees, between -180 and 180 degrees (positive clockwise)
 */
fun bearing(start: LngLat, end: LngLat, final: Boolean = false): Double {
    if (final) return finalBearing(start, end)

    val lon1 = radians(start.longitude)
    val lon2 = radians(end.longitude)
    val lat1 = radians(start.latitude)
    val lat2 = radians(end.latitude)

    val a = sin(lon2 - lon1) * cos(lat2)
    val b = cos(lat1) * sin(lat2) - sin(lat1) * cos(lat2) * cos(lon2 - lon1)

    return degrees(atan2(a, b))
}

internal fun finalBearing(start: LngLat, end: LngLat): Double = (bearing(end, start) + 180) % 360

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
fun destination(origin: LngLat, distance: Double, bearing: Double, units: Units = Units.Kilometers): LngLat {
    val longitude1 = radians(origin.longitude)
    val latitude1 = radians(origin.latitude)
    val bearingRad = radians(bearing)
    val radians = lengthToRadians(distance, units)

    val latitude2 = asin(sin(latitude1) * cos(radians) + cos(latitude1) * sin(radians) * cos(bearingRad))
    val longitude2 = longitude1 + atan2(
        sin(bearingRad) * sin(radians) * cos(radians(latitude1)),
        cos(radians) - sin(latitude1) * sin(latitude2)
    )

    return LngLat(degrees(longitude2), degrees(latitude1))
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
fun distance(from: LngLat, to: LngLat, units: Units = Units.Kilometers): Double {
    val dLat = radians(to.latitude - from.latitude)
    val dLon = radians(to.longitude - from.longitude)
    val lat1 = radians(from.latitude)
    val lat2 = radians(to.latitude)

    val a = sin(dLat / 2).pow(2) + sin(dLon / 2).pow(2) * cos(lat1) * cos(lat2)
    return radiansToLength(2 * atan2(sqrt(a), sqrt(1 - a)), units)
}