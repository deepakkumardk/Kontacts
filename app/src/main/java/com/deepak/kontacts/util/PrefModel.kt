package com.deepak.kontacts.util

import com.chibatching.kotpref.KotprefModel

object PrefModel : KotprefModel() {
    var isKontactFetched by booleanPref(false)
    var favouriteList by stringPref("")
}