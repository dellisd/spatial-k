package io.github.dellisd.turf

import kotlin.native.concurrent.SharedImmutable

/**
 * Radius of the Earth used with the Harvesine formula. Approximated using a spherical (non-ellipsoid) Earth.
 */
@SharedImmutable
const val EARTH_RADIUS = 6371008.8

/**
 * Supported units of measurement in Turf.
 *
 * @property unitFactor Units of measurement factors in relation to 1 meter.
 * @property factor Units of measurement factors using a spherical earth radius.
 * @property areaFactor Area of measurement factors based on 1 square meter.
 */
enum class Units(internal val unitFactor: Double, internal val factor: Double, internal val areaFactor: Double) {
    Meters(1.0, EARTH_RADIUS, 1.0),
    Millimeters(1000.0, EARTH_RADIUS * 1000, 1_000_000.0),
    Centimeters(100.0, EARTH_RADIUS * 100, 10_000.0),
    Kilometers(1 / 1000.0, EARTH_RADIUS / 1000, 0.000_001),
    Acres(Double.NaN, Double.NaN, 0.000_247_105),
    Miles(1 / 1609.344, EARTH_RADIUS / 1609.344, 3.86e-7),
    NauticalMiles(1 / 1852.0, EARTH_RADIUS / 1852.0, Double.NaN),
    Inches(39.370, EARTH_RADIUS * 39.370, 1550.003_100_006),
    Yards(1 / 1.0936, EARTH_RADIUS / 1.0936, 1.195_990_046),
    Feet(3.28084, EARTH_RADIUS * 3.28084, 10.763_910_417),
    Radians(1 / EARTH_RADIUS, 1.0, Double.NaN),
    Degrees(1 / 111325.0, EARTH_RADIUS / 111325, Double.NaN),
}

/**
 * Convert a distance measurement (assuming a spherical Earth) from radians to a more friendly unit.
 *
 * @param radians Radians in radians across the sphere
 * @param units Can be [Miles][Units.Miles], [NauticalMiles][Units.NauticalMiles], [Inches][Units.Inches],
 * [Yards][Units.Yards], [Meters][Units.Meters], [Kilometers][Units.Kilometers], [Centimeters][Units.Centimeters],
 * [Feet][Units.Feet], [Degrees][Units.Degrees], [Radians][Units.Radians]
 * @return Distance
 *
 * @exception IllegalArgumentException if the given units are invalid
 */
fun radiansToLength(radians: Double, units: Units = Units.Kilometers): Double {
    if (units.factor.isNaN())
        throw IllegalArgumentException("${units.name} units is invalid")
    return radians * units.factor
}

/**
 * Convert a distance measurement (assuming a spherical Earth) from a real-world unit into radians.
 *
 * @param distance Distance in real units
 * @param units Can be [Miles][Units.Miles], [NauticalMiles][Units.NauticalMiles], [Inches][Units.Inches],
 * [Yards][Units.Yards], [Meters][Units.Meters], [Kilometers][Units.Kilometers], [Centimeters][Units.Centimeters],
 * [Feet][Units.Feet], [Degrees][Units.Degrees], [Radians][Units.Radians]
 * @return Radians
 *
 * @exception IllegalArgumentException if the given units are invalid
 */
fun lengthToRadians(distance: Double, units: Units = Units.Kilometers): Double {
    if (units.factor.isNaN())
        throw IllegalArgumentException("${units.name} units is invalid")
    return distance / units.factor
}

/**
 * Convert a distance measurement (assuming a spherical Earth) from a real-world unit into degrees.
 *
 * @param distance Distance in real units
 * @param units Can be [Miles][Units.Miles], [NauticalMiles][Units.NauticalMiles], [Inches][Units.Inches],
 * [Yards][Units.Yards], [Meters][Units.Meters], [Kilometers][Units.Kilometers], [Centimeters][Units.Centimeters],
 * [Feet][Units.Feet], [Degrees][Units.Degrees], [Radians][Units.Radians]
 * @return Degrees
 *
 * @exception IllegalArgumentException if the given units are invalid
 */
fun lengthToDegrees(distance: Double, units: Units = Units.Kilometers) = degrees(lengthToRadians(distance, units))

/**
 * Converts a length to the requested unit
 *
 * @param length Length to be converted
 * @param from Unit of the [length]
 * @param to Unit to convert the [length] to
 * @returns The converted length
 *
 * @exception IllegalArgumentException if the given length is negative
 */
fun convertLength(length: Double, from: Units = Units.Meters, to: Units = Units.Kilometers): Double {
    if (length < 0)
        throw IllegalArgumentException("length must be a positive number")
    return radiansToLength(lengthToRadians(length, from), to)
}

/**
 * Converts an area to the requested unit.
 * Valid units: [Acres][Units.Acres], [Miles][Units.Miles], [Inches][Units.Inches], [Yards][Units.Yards],
 * [Meters][Units.Meters], [Kilometers][Units.Kilometers], [Centimeters][Units.Centimeters], [Feet][Units.Feet]
 *
 * @param area Area to be converted
 * @param from Original units of the [area]
 * @param to Units to convert the [area] to
 * @return the converted area
 *
 * @exception IllegalArgumentException if the given units are invalid, or if the area is negative
 */
fun convertArea(area: Double, from: Units = Units.Meters, to: Units = Units.Kilometers): Double {
    if (area < 0)
        throw IllegalArgumentException("area must be a positive number")

    if (from.areaFactor.isNaN())
        throw IllegalArgumentException("invalid original units")

    if (to.areaFactor.isNaN())
        throw IllegalArgumentException("invalid final units")

    return (area / from.areaFactor) * to.areaFactor
}