# Turf

[Turfjs](https://turfjs.org) is a spatial analysis library for JavaScript applications and the `turf` module contains a Kotlin port of it with support for Kotlin Multiplatform projects, as well as a Java API.

This module makes use of the classes defined in the [`geojson`](geojson/) module as the GeoJson inputs to many of the turf functions.

The documentation for the ported functions can be found in the [API docs](api/turf/), while more details on each function can be found on the [Turfjs](https://turfjs.org) site. 

## Installation 

![Sonatype Nexus (Snapshots)](https://img.shields.io/nexus/s/io.github.dellisd.spatialk/turf?server=https%3A%2F%2Foss.sonatype.org)

```groovy
dependencies {
    implementation "io.github.dellisd.spatialk:turf:0.1.0"
}
```

## Example

Turf functions are available as top-level functions in Kotlin, or as static member functions in Java.

=== "Kotlin"
    ```kotlin
    val point = LngLat(-75.0, 45.0)
    val (longitude, latitude) = destination(point, 100.0, 0.0)
    ```
=== "Java"
    ```java
    Position point = new LngLat(-75.0, 45.0);
    Position result = Measurement.destination(point, 100.0, 0.0);
    ```
    
## Turf Functions

A list of all turf functions and their current status in the port can be found on [this page](../ported-functions/).

## Units of Measurement

Units of measurement are represented using the [`Units`](../api/turf/io.github.dellisd.spatialk.turf/-units/) enum. These enum values can be passed into functions to specify the units used by other values passed into the function.

=== "Kotlin"
    ```kotlin
    val result = convertLength(12.5, from = Units.Kilometers, to = Units.Miles)
    ``` 
=== "Java"
    ```java
    double result = TurfUtils.convertLength(12.5, Units.Kilometers, Units.Miles);
    ```
    
Not all units are valid for every function. For example: acres cannot be used as a measure of distance.
Calling a function like `convertLength` with `Units.Acres` as one of the arguments will cause an `IllegalArgumentException`.