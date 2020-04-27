# Turf

The `turf` module contains a pure Kotlin port of [Turfjs](https://turfjs.org) with support for Kotlin Multiplatform projects, as well as a Java API.

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