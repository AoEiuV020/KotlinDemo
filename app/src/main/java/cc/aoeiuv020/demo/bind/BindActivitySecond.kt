package cc.aoeiuv020.demo.bind

import android.os.Bundle

class BindActivitySecond : BindActivityFirst() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = "Second"
    }
}
