package cc.aoeiuv020.type

import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Created by AoEiuV020 on 2018.09.07-22:44:41.
 */
class TypeKtTest {
    @Test
    fun typeTest() {
        assertEquals("class java.lang.String", type<String>().toString())
        assertEquals("java.util.List<? extends java.lang.String>", type<List<String>>().toString())
        assertEquals("java.util.Map<java.lang.Integer, ? extends java.util.List<? extends java.lang.String>>", type<Map<Int, List<String>>>().toString())
    }
}