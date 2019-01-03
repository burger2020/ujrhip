package com.hip.ujr.ujrhip.Item

data class UjrItem(val photo: String, val content: String, val writer: String, val date: Long, val pass: String) {
    constructor():this("","","",0, "")
}