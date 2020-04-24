package io.github.dellisd.spatialk.geojson

import kotlin.jvm.JvmOverloads

data class LngLat @JvmOverloads constructor(
    override val longitude: Double,
    override val latitude: Double,
    override val altitude: Double? = null
) : Position
