package io.github.dellisd.geojson

import io.github.dellisd.geojson.serialization.serializer
import kotlinx.serialization.UnstableDefault
import kotlinx.serialization.builtins.list
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals

@UnstableDefault
class SerializationTests {

    @Test
    fun testSerializePosition() {
        val position = LngLat(-75.1, 45.1)
        val result = Json.stringify(Position.serializer(), position)
        assertEquals("[-75.1,45.1]", result)

        val altitude = LngLat(60.2, 23.2354, 100.5)
        val altitudeResult = Json.stringify(Position.serializer(), altitude)
        assertEquals("[60.2,23.2354,100.5]", altitudeResult)

        val list = listOf(LngLat(12.3, 45.6), LngLat(78.9, 12.3))
        val listResult = Json.stringify(Position.serializer().list, list)
        assertEquals("[[12.3,45.6],[78.9,12.3]]", listResult)
    }

    @Test
    fun testDeserializePosition() {
        val position = Json.parse(Position.serializer(), "[32.4,54.1234]")
        assertEquals(LngLat(32.4, 54.1234), position)

        val altitude = Json.parse(Position.serializer(), "[60.2,23.2354,100.5]")
        assertEquals(LngLat(60.2, 23.2354, 100.5), altitude)

        val list = Json.parse(Position.serializer().list, "[[12.3,45.6],[78.9,12.3]]")
        assertEquals(listOf(LngLat(12.3, 45.6), LngLat(78.9, 12.3)), list)
    }

}