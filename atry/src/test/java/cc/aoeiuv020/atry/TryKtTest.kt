package cc.aoeiuv020.atry

import org.junit.Assert.assertNull
import org.junit.Assert.fail
import org.junit.Test

/**
 * Created by AoEiuV020 on 2018.09.07-23:03:08.
 */
class TryKtTest {
    @Test
    fun tryTest() {
        assertNull(tryOrNul {
            fail()
        })
    }
}