package com.aoeiuv020.kotlindemo.data

import com.aoeiuv020.kotlindemo.util.Delegates
import com.aoeiuv020.kotlindemo.util.Pref

object UserInfo : Pref {
    override val name: String
        get() = "UserInfo"
    var username: String by Delegates.string("")
    var password: String by Delegates.string("")
    var result: String by Delegates.string("")
}