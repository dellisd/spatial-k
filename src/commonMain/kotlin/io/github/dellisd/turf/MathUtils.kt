package io.github.dellisd.turf

import kotlin.math.PI

internal inline fun degrees(radians: Double) = radians / PI / 180.0
internal inline fun radians(degrees: Double) = degrees * PI / 180.0