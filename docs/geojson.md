# GeoJson

The `geojson` module contains an implementation of the [GeoJson standard](https://tools.ietf.org/html/rfc7946).

In Kotlin projects, it's recommended to use the DSL for constructing GeoJson objects which is available in the [`geojson-dsl`](geojson-dsl/) module.

## Installation 

![Sonatype Nexus (Snapshots)](https://img.shields.io/nexus/s/io.github.dellisd.spatialk/geojson?server=https%3A%2F%2Foss.sonatype.org)

```groovy
dependencies {
    implementation "io.github.dellisd.spatialk:geojson:0.1.0"
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
All seven types of GeoJSON geometries are implemented and summarized below. Full documentation can be found in the [API pages](api/geojson/).

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

=== "JSON"
    ```json
    [-75, 45]
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
    val multiPoint = MultiPoint(LngLat(-75.0, 45.0), LngLat(-79.0, 44.0))
    ```
    
=== "Java"
    ```java
    MultiPoint multiPoint = new MultiPoint(
            new LngLat(-75.0, 45.0), 
            new LngLat(-79.0, 44.0)
    );
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
    val lineString = LineString(LngLat(-75.0, 45.0), LngLat(-79.0, 44.0))
    ```

=== "Java"
    ```java
    LineString lineString = new LineString(
            new LngLat(-75.0, 45.0),
            new LngLat(-79.0, 44.0)
    );
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
            LngLat(-79.87, 43.42),
            LngLat(-78.89, 43.49),
            LngLat(-79.07, 44.02),
            LngLat(-79.95, 43.87),
            LngLat(-79.87, 43.42)
        ),
        listOf(
            LngLat(-79.75, 43.81),
            LngLat(-79.56, 43.85),
            LngLat(-79.7, 43.88),
            LngLat(-79.75, 43.81)
        )
    )
    ```

=== "Java"
    ```java
    ArrayList<LngLat> ring1 = new ArrayList<>();
    ring1.add(new LngLat(-79.87, 43.42));
    ring1.add(new LngLat(-78.89, 43.49));
    ring1.add(new LngLat(-79.07, 44.02));
    ring1.add(new LngLat(-79.95, 43.87));
    ring1.add(new LngLat(-79.87, 43.42));
    
    ArrayList<LngLat> ring2 = new ArrayList<>();
    ring2.add(new LngLat(-79.75, 43.81));
    ring2.add(new LngLat(-79.56, 43.85));
    ring2.add(new LngLat(-79.7, 43.88));
    ring2.add(new LngLat(-79.75, 43.81));
    
    Polygon polygon = new Polygon(ring1, ring2);
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
        LngLat(-79.87, 43.42),
        LngLat(-78.89, 43.49),
        LngLat(-79.07, 44.02),
        LngLat(-79.95, 43.87),
        LngLat(-79.87, 43.42)
    ),
    listOf(
        LngLat(-79.75, 43.81),
        LngLat(-79.56, 43.85),
        LngLat(-79.7, 43.88),
        LngLat(-79.75, 43.81)
    )
    val multiPolygon = MultiPolygon(polygon, polygon)
    ```

=== "Java"
    ```java
    ArrayList<LngLat> ring1 = new ArrayList<>();
    ring1.add(new LngLat(-79.87, 43.42));
    ring1.add(new LngLat(-78.89, 43.49));
    ring1.add(new LngLat(-79.07, 44.02));
    ring1.add(new LngLat(-79.95, 43.87));
    ring1.add(new LngLat(-79.87, 43.42));
    
    ArrayList<LngLat> ring2 = new ArrayList<>();
    ring2.add(new LngLat(-79.75, 43.81));
    ring2.add(new LngLat(-79.56, 43.85));
    ring2.add(new LngLat(-79.7, 43.88));
    ring2.add(new LngLat(-79.75, 43.81));
    
    ArrayList<ArrayList<LngLat> polygon = new ArrayList<>();
    polygon.add(ring1);
    polygon.add(ring2);
    
    MultiPolygon multiPolygon = new MultiPolygon(polygon, polygon);
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

=== "Java"
    ```java
    GeometryCollection = new GeometryCollection(point, lineString);
    
    // Can be iterated over, and used in any way a Collection<T> can be
    for (Geometry geometry : geometryCollection) {
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

=== "Java"
    ```java
    Feature feature = new Feature(point);
    feature.setNumberProperty("size", 9999);
    
    Number size = feature.getNumberProperty("size"); // 9999
    Geometry geometry = feature.getGeometry(); // point
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

=== "Java"
    ```java
    FeatureCollection featureCollection = new FeatureCollection(pointFeature);
    
    for (Feature feature : featureCollection) {
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
They are represented by two positions: the southwest, and northeastern points of the bounded area.

=== "Kotlin"
    ```kotlin
    val bbox = BoundingBox(west = 11.6, south = 45.1, east = 12.7, north = 45.7)
    ```

=== "Java"
    ```java
    BoundingBox bbox = new BoundingBox(11.6, 45.1, 12.7, 45.7);
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
=== "Java"
    ```java
    FeatureCollection featureCollection = getFeatureCollection();
    
    String json = featureCollection.toJson();
    System.out.println(json);
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
=== "Java"
    ```java
    // Throws exception if the JSON cannot be deserialized to a Point
    Point myPoint = GeometryFactory.fromJson("{...geojson...}");
    
    // Returns null if an error occurs
    Point nullable = GeometryFactory.fromJsonOrNull("{... not a point...}");
    ```
    
#### Feature and FeatureCollection

`Feature` and `FeatureCollection` objects can be converted from Json similarly.

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
