package org.maplibre.spatialk.geojson

/**
 * Returns a position with longitude normalized into [-180, 180), mapping 180° to -180°. Latitude,
 * altitude, and any additional coordinate elements are unchanged. A non-finite longitude becomes
 * NaN.
 */
public fun Position.wrapped(): Position =
    Position(coordinates.copyOf().also { it[0] = wrapLongitude(it[0]) })

/**
 * Returns a bounding box with both longitudes normalized into [-180, 180).
 *
 * The corners are not reordered: an east longitude less than the west longitude encodes an
 * antimeridian crossing as specified in
 * [RFC 7946 Section 5.2](https://tools.ietf.org/html/rfc7946#section-5.2). All other coordinate
 * elements are unchanged. Non-finite longitudes become NaN.
 *
 * Wrapping discards world copies and the original longitude span, including whole-world spans. To
 * preserve the covered area, call [splitAtAntimeridian] on the original box instead.
 */
public fun BoundingBox.wrapped(): BoundingBox =
    withLongitudes(wrapLongitude(coordinates[0]), wrapLongitude(coordinates[size / 2]))

/**
 * Returns non-crossing boxes with longitudes in [-180, 180], preserving all other coordinate
 * elements, including altitude bounds.
 *
 * Accepts both RFC antimeridian encoding (170° to -170°) and continuous, unwrapped encoding (170°
 * to 190°). When east is less than west, the span runs eastward to the next equivalent east
 * longitude, covering less than 360°. Equivalent endpoints in this encoding, such as 180° to -180°,
 * describe a zero-width box.
 *
 * A continuous span of at least 360° returns one full-world box from -180° to 180°. Otherwise, a
 * crossing returns the portion from the wrapped west longitude to 180°, followed by the portion
 * from -180° to the wrapped east longitude. Merely touching ±180° does not create an extra
 * zero-width box. An already non-crossing box within [-180, 180] returns `listOf(this)`; a box
 * wholly in another world copy returns one normalized box.
 *
 * @throws IllegalArgumentException if either longitude is non-finite.
 */
public fun BoundingBox.splitAtAntimeridian(): List<BoundingBox> {
    val west = coordinates[0]
    val east = coordinates[size / 2]
    require(west.isFinite() && east.isFinite()) { "Longitudes must be finite" }

    if (west in -180.0..180.0 && east in west..180.0) return listOf(this)
    if (east - west >= 360.0) return listOf(withLongitudes(-180.0, 180.0))

    val wrappedWest = wrapLongitude(west)
    val wrappedEast = wrapLongitude(east)
    val end = if (wrappedEast == -180.0 && wrappedWest > -180.0) 180.0 else wrappedEast
    return if (end >= wrappedWest) listOf(withLongitudes(wrappedWest, end))
    else listOf(withLongitudes(wrappedWest, 180.0), withLongitudes(-180.0, end))
}

private fun wrapLongitude(longitude: Double): Double {
    val remainder = longitude % 360.0
    return when {
        remainder < -180.0 -> remainder + 360.0
        remainder >= 180.0 -> remainder - 360.0
        else -> remainder
    }
}

private fun BoundingBox.withLongitudes(west: Double, east: Double): BoundingBox =
    BoundingBox(
        coordinates.copyOf().also {
            it[0] = west
            it[size / 2] = east
        }
    )
