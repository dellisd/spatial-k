package io.github.dellisd.turf.geojson

sealed class Geometry

data class Point(val coordinates: LngLat) : Geometry()

data class MultiPoint(val coordinates: List<LngLat>) : Geometry()

data class LineString(val coordinates: List<LngLat>) : Geometry()

data class MultiLineString(val coordinates: List<List<LngLat>>) : Geometry()

data class Polygon(val coordinates: List<List<LngLat>>) : Geometry()

data class MultiPolygon(val coordinates: List<List<List<LngLat>>>) : Geometry()

data class GeometryCollection(val geometries: List<Geometry>) : Geometry()