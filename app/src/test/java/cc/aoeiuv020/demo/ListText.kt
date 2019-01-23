package cc.aoeiuv020.demo

import org.junit.Assert.assertEquals
import org.junit.Test

class ListText {
    @Test
    fun indexOfTest() {
        fun newMap(empty: Boolean): MutableMap<String, Int> = if (empty) {
            mutableMapOf()
        } else {
            mutableMapOf("key" to 0)
        }

        val list = listOf(newMap(true), newMap(false), newMap(true))
        assertEquals(list[0], list[2])
        // indexOf走的是equals,
        assertEquals(0, list.indexOf(list[2]))
    }
}