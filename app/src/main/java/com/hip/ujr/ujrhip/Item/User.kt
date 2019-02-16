package com.hip.ujr.ujrhip.Item

import java.util.HashMap

data class User(var token: String = "", var name: String = "", var profileUrl: String = "",
                val userAttributes: MutableMap<String, String> = HashMap()
)