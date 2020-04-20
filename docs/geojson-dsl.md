# GeoJson DSL

The `geojson-dsl` library provides a Kotlin DSL for constructing GeoJson objects from the `geojson` library.

## Installation

```groovy
dependencies {
    implementation "package.name.here:geojson-dsl:0.1.0"
}
```

## DSL

### Geometry

Each geometry type has a corresponding DSL.

A GeoJson object's `bbox` value can be assigned in any of the DSLs.

#### Point

=== "Kotlin"
    ```kotlin
    point(longitude = -75.0, latitude = 45.0, altitude = 100.0)
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
This means that it is possible to add `Point` objects as well as `LngLat` objects as positions to a `MultiPoint`.

=== "Kotlin"
    ```kotlin
    multiPoint {
        +point(-75.0, 45.0)
        +LngLat(-78.0, 44.0)
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
        +LngLat(45.0, 45.0)
        +LngLat(0.0, 0.0)
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
        +LngLat(45.0, 45.0)
        +LngLat(0.0, 0.0)
    }
    
    multiLineString {
        +simpleLine
        
        // Inline LineString creation
        +lineString {
            +LngLat(44.4, 55.5)
            +LngLat(55.5, 66.6)
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
        +LngLat(45.0, 45.0)
        +LngLat(0.0, 0.0)
    }
    
    polygon {
         ring {
             // LineStrings can be used as part of a ring
             +simpleLine
             +LngLat(12.0, 12.0)
             complete()
         }
         ring {
             +LngLat(4.0, 4.0)
             +LngLat(2.0, 2.0)
             +LngLat(3.0, 3.0)
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

Like with previous "Multi" geometries, the unary plus operator is used to add multipl `Polygon` objects. 
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

The unary plus operator can be used to add any geometry to a `GeometryCollection`.

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