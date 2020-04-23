# GeoJson

The `geojson` module contains an implementation of the [GeoJson standard](https://tools.ietf.org/html/rfc7946).

For Kotlin, a DSL for constructing GeoJson objects is available in the [`geojson-dsl`](/geojson-dsl) module.

## Installation 

```groovy
dependencies {
    implementation "io.github.dellisd.spatialk:geojson:0.1.0"
}
```

## GeoJSON Objects

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
All seven types of GeoJSON geometries are implemented and summarized below.

#### Position

Positions are implemented as an interface where the longitude, latitude, and optionally an altitude are accessible as 
properties. The basic implementation of the `Position` interface is the `LngLat` class.

=== "Kotlin" 
    ``` kotlin
    val position: Position = LngLat(-75.0, 45.0)
    
    // Access values
    position.longitude
    position.latitude
    position.altitude // null if unspecified
    ```

=== "Java" 
    ``` java
    Position position = new LngLat(-75.0, 45.0);
        
    // Access values
    position.getLongitude();
    position.getLatitude();
    position.getAltitude(); // null if unspecified
    ```

#### Point

A Point is a single Position. The `Point` class implements the `Position` interface.

=== "Kotlin" 
    ```kotlin
    val point = Point(LngLat(-75.0, 45.0))
    
    println(point.longitude) 
    // Prints: -75.0
    ```

=== "Java" 
    ```java
    Point point = new Point(new LngLat(-75.0, 45.0));
    
    System.out.println(point.getLongitude());
    // Prints: -75.0
    ```

#### MultiPoint

A `MultiPoint` is an array of Positions.

=== "Kotlin"
    ```kotlin
    val multiPoint = MultiPoint(LngLat(-75.0, 45.0), LngLat(-79.0, 44.0))
    ```
    
=== "Java"
    ```java
    MultiPoint multiPoint = new MultiPoint(
            new LngLat(-75.0, 45.0), 
            new LngLat(-79.0, 44.0)
    );
    ```

#### LineString

A `LineString` is a sequence of two or more Positions.

=== "Kotlin"
    ```kotlin
    val lineString = LineString(LngLat(-75.0, 45.0), LngLat(-79.0, 44.0))
    ```

=== "Java"
    ```java
    LineString lineString = new LineString(
            new LngLat(-75.0, 45.0),
            new LngLat(-79.0, 44.0)
    );
    ```

#### MultiLineString

A `MultiLineString` is an array of LineStrings.

=== "Kotlin"
    ```kotlin
    val multiLineString = MultiLineString(
        listOf(LngLat(12.3, 45.6), LngLat(78.9, 12.3)),
        listOf(LngLat(87.6, 54.3), LngLat(21.9, 56.4))
    )
    ```

=== "Java"
    ```java
    ArrayList<LngLat> list1 = new ArrayList<>();
    list1.add(new LngLat(12.3, 45.6));
    list1.add(new LngLat(78.9, 12.3));
    
    ArrayList<LngLat> list2 = new ArrayList<>();
    list2.add(new LngLat(87.6, 54.3));
    list2.add(new LngLat(21.9, 56.4))
    
    MultiLineString multiLineString = new MultiLineString(list1, list2);
    ```

#### Polygon

TODO

#### MultiPolygon

TODO

#### GeometryCollection

TODO

### Feature

TODO

### FeatureCollection

TODO

### BoundingBox

TODO

## Serialization

Any `GeoJson` object can be serialized to Json using the `json` property.

=== "Kotlin"
    ``` kotlin
    val featureCollection: FeatureCollection = getFeatureCollection()
    
    println(featureCollection.json)
    ```
=== "Java"
    ```java
    FeatureCollection featureCollection = getFeatureCollection();
    
    System.out.println(featureCollection.toJson());
    ```

## Deserialization
Json strings can be converted to GeoJson objects using various methods.

### Geometry

Geometry can be deserialized using generic functions that will automatically deserialize the given Json into the 
appropriate `Geometry` subclass.

In Kotlin, these functions are available as extension functions on a `String`. 
In Java, these functions are available as static methods on `GeometryFactory`. 

=== "Kotlin"
    ```kotlin
    val myPoint = "{...geojson...}".toGeometry<Point>()
    val nullable = "{...not a point...}".toGeometryOrNull<Point>()
    ```
=== "Java"
    ```java
    Point myPoint = GeometryFactory.fromJson("{...geojson...}");
    Point nullable = GeometryFactory.fromJsonOrNull("{... not a point...}");
    ```
    
### Feature and FeatureCollection

`Feature` and `FeatureCollection` objects are deserialized in a similar fashion.

=== "Kotlin"
    ```kotlin
    val feature = "{...feature...}".toFeature()
    val featureCollection = "{...feature collection...}".toFeatureCollection()
    ```
    
=== "Java"
    ```java
    Feature feature = Feature.fromJson("{...feature...}");
    FeatureCollection = FeatureCollection.fromJson("{...feature collection...}");
    ```