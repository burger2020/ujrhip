package com.hip.ujr.ujrhip.Etc

interface AWSDBCallback {
    fun <T>loadDataCallback(data: ArrayList<T>)
    fun <T>addDataCallback(data: ArrayList<T>)
}