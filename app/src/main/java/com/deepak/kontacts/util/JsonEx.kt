package com.deepak.kontacts.util

import com.google.gson.Gson

fun <T : Any> T.convertToString(): String? = Gson().toJson(this)

fun <T : Any> convertToClass(string: String, to: Class<T>): T = Gson().fromJson(string, to)

fun <T : Any> T.createDeepCopy(): T {
    val gson = Gson()
    val tStr = gson.toJson(this)
    return gson.fromJson<T>(tStr, this::class.java)
}

/**
 * Convert class T to class E with the help of Gson
 */
fun <T : Any, E : Any> T.convert(to: Class<E>): E {
    val gson = Gson()
    val tStr = gson.toJson(this)
    return gson.fromJson<E>(tStr, to)
}