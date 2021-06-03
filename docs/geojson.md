# GeoJson

The `geojson` module contains an implementation of the [GeoJson standard](https://tools.ietf.org/html/rfc7946).

See below for constructing GeoJson objects using the DSL.

## Installation 

![Maven Central](https://img.shields.io/maven-central/v/io.github.dellisd.spatialk/geojson)
![Sonatype Nexus (Snapshots)](https://img.shields.io/nexus/s/io.github.dellisd.spatialk/geojson?server=https%3A%2F%2Foss.sonatype.org)

=== "Kotlin"
    ```kotlin
    dependencies {
    implementation("io.github.dellisd.spatialk:geojson:<version>")
    }
    ```
=== "Groovy"
    ```groovy
    dependencies {
    implementation "io.github.dellisd.spatialk:geojson:<version>"
    }
    ```

## GeoJson Objects

The `GeoJson` interface represents all GeoJson objects. All GeoJson objects can have a `bbox` property specified on them
which is a `BoundingBox` that represents the bounds of that object's geometry.

### Geometry

Geometry objects are a sealed hierarchy of classes that inherit from the `Geometry` class. This allows for exhaustive 
type checks in Kotlin using a `when` block.

=== "Kotlin"
    ``` kotlin
    val geometry: Geometry = getGeometry()
    
    val type = when (geometry) {
        is Point -> "Point"
        is MultiPoint -> "MultiPoint"
        is LineString -> "LineString"
        is MultiLineString -> "MultiLineString"
        is Polygon -> "Polygon"
        is MultiPolygon -> "MultiPolygon"
        is GeometryCollection -> "GeometryCollection"
    }
    ```
All seven types of GeoJSON geometries are implemented and summarized below. Full documentation can be found in the [API pages](../api/geojson/).

#### Position

Positions are implemented as a `DoubleArray`-backed class. Each component (`longitude`, `latitude`, `altitude`) can be accessed by its propery.
The class also supports destructuring.

Positions are implemented as an interface where the longitude, latitude, and optionally an altitude are accessible as 
properties. The basic implementation of the `Position` interface is the `LngLat` class.

=== "Kotlin" 
    ``` kotlin
    val position: Position = Position(-75.0, 45.0)
    val (longitude, latitude, altitude) = position
    
    // Access values
    position.longitude
    position.latitude
    position.altitude // null if unspecified
    ```

=== "JSON"
    ```json
    [-75, 45]
    ```

#### Point

A Point is a single Position.

=== "Kotlin" 
    ```kotlin
    val point = Point(Position(-75.0, 45.0))
    
    println(point.longitude) 
    // Prints: -75.0
    ```
    
=== "JSON"
    ```json
    {
        "type": "Point",
        "coordinates": [-75, 45]
    }
    ```

#### MultiPoint

A `MultiPoint` is an array of Positions.

=== "Kotlin"
    ```kotlin
    val multiPoint = MultiPoint(Position(-75.0, 45.0), Position(-79.0, 44.0))
    ```
    
=== "JSON"
    ```json
    {
        "type": "MultiPoint",
        "coordinates": [[-75, 45], [-79, 44]]
    }
    ```

#### LineString

A `LineString` is a sequence of two or more Positions.

=== "Kotlin"
    ```kotlin
    val lineString = LineString(Position(-75.0, 45.0), Position(-79.0, 44.0))
    ```

=== "JSON"
    ```json
    {
        "type": "LineString",
        "coordinates": [[-75, 45], [-79, 44]]
    }
    ```

#### MultiLineString

A `MultiLineString` is an array of LineStrings.

=== "Kotlin"
    ```kotlin
    val multiLineString = MultiLineString(
        listOf(Position(12.3, 45.6), Position(78.9, 12.3)),
        listOf(Position(87.6, 54.3), Position(21.9, 56.4))
    )
    ```

=== "JSON"
    ```json
    {
        "type": "MultiLineString",
        "coordinates": [
            [[12.3, 45.6], [78.9, 12.3]],
            [[87.6, 54.3], [21.9, 56.4]]
        ]
    }
    ```

#### Polygon

A `Polygon` is an array of rings. Each ring is a sequence of points with the last point matching the first point to indicate a closed area.
The first ring defines the outer shape of the polygon, while all the following rings define "holes" inside the polygon.


=== "Kotlin"
    ```kotlin
    val polygon = Polygon(
        listOf(
            Position(-79.87, 43.42),
            Position(-78.89, 43.49),
            Position(-79.07, 44.02),
            Position(-79.95, 43.87),
            Position(-79.87, 43.42)
        ),
        listOf(
            Position(-79.75, 43.81),
            Position(-79.56, 43.85),
            Position(-79.7, 43.88),
            Position(-79.75, 43.81)
        )
    )
    ```
    
=== "JSON"
    ```json
    {
        "type": "Polygon",
        "coordinates": [
            [[-79.87, 43.42], [-78.89, 43.49], [-79.07, 44.02], [-79.95, 43.87], [-79.87, 43.42]],
            [[-79.75, 43.81], [-79.56, 43.85], [-79.7, 43.88], [-79.75, 43.81]]
        ]
    }
    ```

#### MultiPolygon

A `MultiPolygon` is an array of Polygons.

=== "Kotlin"
    ```kotlin
    val polygon = listOf(
        Position(-79.87, 43.42),
        Position(-78.89, 43.49),
        Position(-79.07, 44.02),
        Position(-79.95, 43.87),
        Position(-79.87, 43.42)
    ),
    listOf(
        Position(-79.75, 43.81),
        Position(-79.56, 43.85),
        Position(-79.7, 43.88),
        Position(-79.75, 43.81)
    )
    val multiPolygon = MultiPolygon(polygon, polygon)
    ```
    
=== "JSON"
    ```json
    {
        "type": "MultiPolygon",
        "coordinates": [
            [
                [[-79.87, 43.42], [-78.89, 43.49], [-79.07, 44.02], [-79.95, 43.87], [-79.87, 43.42]],
                [[-79.75, 43.81], [-79.56, 43.85], [-79.7, 43.88], [-79.75, 43.81]]
            ],
            [
                [[-79.87, 43.42], [-78.89, 43.49], [-79.07, 44.02], [-79.95, 43.87], [-79.87, 43.42]],
                [[-79.75, 43.81], [-79.56, 43.85], [-79.7, 43.88], [-79.75, 43.81]]
            ]
        ]
    }
    ```

#### GeometryCollection

A `GeometryCollection` is a collection of different types of Geometry. It implements the `Collection` interface and can be used in any place that a collection can be used.

=== "Kotlin"
    ```kotlin
    val geometryCollection = GeometryCollection(point, lineString)
    
    // Can be iterated over, and used in any way a Collection<T> can be
    geometryCollection.forEach { geometry ->
        // ...
    }
    ```
    
=== "JSON"
    ```json
    {
        "type": "GeometryCollection",
        "coordinates": [
            {
                "type": "Point",
                "coordinates": [-75, 45]
            },
            {
                "type": "LineString",
                "coordinates": [[-75, 45], [-79, 44]]
            }
        ]
    }
    ```

### Feature

A `Feature` can contain a `Geometry` object, as well as a set of data properties, and optionally a commonly used identifier (`id`).

A feature's properties are stored as a map of `JsonElement` objects from `kotlinx.serialization`. 
A set of helper methods to get and set properties with the appropriate types directly.

=== "Kotlin"
    ```kotlin
    val feature = Feature(point)
    feature.setNumberProperty("size", 9999)
    
    val size: Number? = feature.getNumberProperty("size") // 9999
    val geometry: Geometry? = feature.geometry // point
    ```
    
=== "JSON"
    ```json
    {
        "type": "Feature",
        "geometry": 
        {
            "type": "Point",
            "coordinates": [-75, 45]
        },
        "properties": 
        {
            "size": 9999
        }
    }
    ```

### FeatureCollection

A `FeatureCollection` is a collection of multiple features. `FeatureCollection` implements the `Collection` interface and can be used in any place that a collection can be used.  

=== "Kotlin"
    ```kotlin
    val featureCollection = FeatureCollection(pointFeature)
    
    featureCollection.forEach { feature ->
        // ...
    }
    ```
    
=== "JSON"
    ```json
    {
        "type": "FeatureCollection",
        "features": [
            {
                "type": "Feature",
                "geometry": 
                {
                    "type": "Point",
                    "coordinates": [-75, 45]
                },
                "properties": 
                {
                    "size": 9999
                }
            }
        ]
    }
    ```

### BoundingBox

The `BoundingBox` class is used to represent the bounding boxes that can be set for any `GeoJson` object.
Like the `Position` class, bounding boxes are backed by a `DoubleArray` with each component accessible by its propery (`southwest` and `northeast`).
Bounding boxes also support destructuring.

=== "Kotlin"
    ```kotlin
    val bbox = BoundingBox(west = 11.6, south = 45.1, east = 12.7, north = 45.7)
    val (southwest, northeast) = bbox // Two Positions
    ```
    
=== "JSON"
    ```json
    [11.6, 45.1, 12.7, 45.7]
    ```


## Serialization

Serialization is done using `kotlinx.serialization` and the serializer for any object listed above can be obtained using the static `.serializer()` method.
The `Geometry` sealed class hierarchy uses a polymorphic serializer, so the serializer for all types of geometry are simply obtained from `Geometry.serializer()`.

### To Json

Any `GeoJson` object can be serialized to Json using the `json` property.

=== "Kotlin"
    ``` kotlin
    val featureCollection: FeatureCollection = getFeatureCollection()
    
    val json = featureCollection.json
    println(json)
    ```

### From Json
Json strings can be converted to GeoJson objects using various methods.

#### Geometry

Geometry can be converted from Json using generic functions that will automatically deserialize the given Json into the 
appropriate `Geometry` subclass.

In Kotlin, these functions are available as extension functions on a `String`. 
In Java, these functions are available as static methods on `GeometryFactory`. 

=== "Kotlin"
    ```kotlin
    // Throws exception if the JSON cannot be deserialized to a Point
    val myPoint = "{...geojson...}".toGeometry<Point>()
    
    // Returns null if an error occurs
    val nullable = "{...not a point...}".toGeometryOrNull<Point>()
    ```
    
#### Feature and FeatureCollection

`Feature` and `FeatureCollection` objects can be converted from Json similarly.

=== "Kotlin"
    ```kotlin
    val feature = "{...feature...}".toFeature()
    
    val featureCollection = "{...feature collection...}".toFeatureCollection()
    ```

## GeoJson DSL

It's recommended to construct GeoJson objects in-code using the included DSL.

### Positions

Convenience functions to construct latitude/longitude Position instances is included.
These functions will check for valid latitude and longitude values and will throw an `IllegalArgumentException` otherwise.

=== "Kotlin"
    ```kotlin
    lngLat(longitude = -75.0, latitude = 45.0)
    
    // Throws exception!!
    lngLat(longitude = -565.0, latitude = 45.0)
    ```
=== "JSON"
    ```json
    [-75.0, 45.0]
    ```

### Geometry

Each geometry type has a corresponding DSL.

A GeoJson object's `bbox` value can be assigned in any of the DSLs.

#### Point

=== "Kotlin"
    ```kotlin
    point(longitude = -75.0, latitude = 45.0, altitude = 100.0)

    // Or...

    point(Position(12.5, 35.9))
    ```

=== "JSON"
    ```json
    {
      "type": "Point",
      "coordinates": [-75.0, 45.0, 100.0]
    }
    ```
#### MultiPoint

The `MultiPoint` DSL uses the unary plus operator to add `Position` instances as positions in the geometry.
`Point` geometries can also be added to the multi point using the unary plus operator.

=== "Kotlin"
    ```kotlin
    multiPoint {
        +point(-75.0, 45.0)
        +lngLat(-78.0, 44.0)
    }
    ```
=== "JSON"
    ``` json
    {
      "type": "MultiPoint",
      "coordinates": [
        [-75.0, 45.0],
        [-78.0, 44.0]
      ]
    }
    ```

#### LineString

Like with `MultiPoint`, the `LineString` DSL uses the unary plus operator to add positions as part of the line.
The order in which positions are added to the `LineString` is the order that the `LineString` will follow.

=== "Kotlin"
    ```kotlin
    lineString {
    +lngLat(45.0, 45.0)
    +lngLat(0.0, 0.0)
    }
    ```
=== "JSON"
    ```json
    {
      "type": "LineString",
      "coordinates": [[45.0, 45.0], [0.0, 0.0]]
    }
    ```


#### MultiLineString

The `MultiLineString` DSL uses the unary plus operator to add multiple line strings. The `LineString` DSL can be used to
create `LineString` objects to add.

=== "Kotlin"
    ```kotlin
    val simpleLine = lineString {
        +lngLat(45.0, 45.0)
        +lngLat(0.0, 0.0)
    }

    multiLineString {
        +simpleLine
        
        // Inline LineString creation
        +lineString {
            +lngLat(44.4, 55.5)
            +lngLat(55.5, 66.6)
        }
    }
    ```
=== "JSON"
    ```json
    {
      "type": "MultiLineString",
      "coordinates": [
        [[45.0, 45.0], [0.0, 0.0]],
        [[44.4, 55.5], [55.5, 66.6]]
      ]
    }
    ```

#### Polygon

The `Polygon` DSL is used by specifying linear rings that make up the polygon's shape and holes.
The first `ring` is the exterior ring with four or more positions. The last position must be the same as the first position.
All `ring`s that follow will represent interior rings (i.e. holes) in the polygon.

For convenience, the `complete()` function can be used to "complete" a ring.
It adds the last position in the ring by copying the first position that was added.

=== "Kotlin"
    ```kotlin
    val simpleLine = lineString {
        +lngLat(45.0, 45.0)
        +lngLat(0.0, 0.0)
    }

    polygon {
         ring {
             // LineStrings can be used as part of a ring
             +simpleLine
             +lngLat(12.0, 12.0)
             complete()
         }
         ring {
             +lngLat(4.0, 4.0)
             +lngLat(2.0, 2.0)
             +lngLat(3.0, 3.0)
             complete()
         }
     }
    ```
=== "JSON"
    ```json
    {
      "type": "Polygon",
      "coordinates": [
        [[45.0, 45.0], [0.0, 0.0], [12.0, 12.0], [45.0, 45.0]],
        [[4.0, 4.0], [2.0, 2.0], [3.0, 3.0], [4.0, 4.0]]
      ]
    }
    ```

#### MultiPolygon

Like with previous "Multi" geometries, the unary plus operator is used to add multiple `Polygon` objects.
The `Polygon` DSL can also be used here.

=== "Kotlin"
    ```kotlin
    val simplePolygon = previousExample()

    multiPolygon {
        +simplePolygon
        +polygon {
            ring {
                +LngLat(12.0, 0.0)
                +LngLat(0.0, 12.0)
                +LngLat(-12.0, 0.0)
                +LngLat(5.0, 5.0)
                complete()
            }
        }
    }
    ```
=== "JSON"
    ```json
    {
      "type": "MultiPolygon",
      "coordinates": [
        [
          [[45.0, 45.0], [0.0, 0.0], [12.0, 12.0], [45.0, 45.0]],
          [[4.0, 4.0], [2.0, 2.0], [3.0, 3.0], [4.0, 4.0]]
        ], [
          [[12.0, 0.0], [0.0, 12.0], [-12.0, 0.0], [5.0, 5.0], [12.0, 0.0]]
        ]
      ]
    }
    ```

#### Geometry Collection

The unary plus operator can be used to add any geometry instance to a `GeometryCollection`.

=== "Kotlin"
    ```kotlin
    val simplePoint: Point = previousPoint()
    val simpleLine: LineString = previousLineString()
    val simplePolygon: Polygon = previousPolygon()

    geometryCollection {
        +simplePoint
        +simpleLine
        +simplePolygon
    }
    ```

=== "JSON"
    ```json
    {
      "type": "GeometryCollection",
      "geometries": [
        {
          "type": "Point",
          "coordinates": [-75.0, 45.0, 100.0]
        },
        {
          "type": "LineString",
          "coordinates": [[45.0, 45.0], [0.0, 0.0]]
        },
        {
          "type": "Polygon",
          "coordinates": [
          [[45.0, 45.0], [0.0, 0.0], [12.0, 12.0], [45.0, 45.0]],
          [[4.0, 4.0], [2.0, 2.0], [3.0, 3.0], [4.0, 4.0]]
          ]
        }
      ]
    }
    ```
### Feature

The `Feature` DSL can construct a `Feature` object with a geometry, a set of properties, a bounding box, and an id.

=== "Kotlin"
    ```kotlin
    feature {
        geometry = point(-75.0, 45.0)
        id = "point1"
        bbox = BoundingBox(-76.9, 44.1, -74.2, 45.7)
        properties {
            "name" to "Hello World"
            "value" to 13
            "cool" to true
        }
    }
    ```

=== "JSON"
    ```json
    {
      "type": "Feature",
      "id": "point1",
      "bbox": [-76.9, 44.1, -74.2, 45.7],
      "properties": {
        "name": "Hello World",
        "value": 13,
        "cool": true
      },
      "geometry": {
        "type": "Point",
        "coordinates": [-75.0, 45.0]
      }
    }
    ```

### Feature Collection

A `FeatureCollection` is constructed by adding multiple `Feature` objects using the unary plus operator.

=== "Kotlin"
    ```kotlin
    featureCollection {
        +feature {
            geometry = point(-75.0, 45.0)
        }
    }
    ```

=== "JSON"
    ```json
    {
      "type": "FeatureCollection",
      "features": [
        {
          "type": "Feature",
          "geometry": {
            "type": "Point",
            "coordinates": [-75.0, 45.0]
          },
          "properties": {}
        }
      ]
    }
    ```
