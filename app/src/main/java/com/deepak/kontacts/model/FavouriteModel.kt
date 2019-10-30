package com.deepak.kontacts.model

import io.realm.RealmList
import io.realm.RealmObject

open class FavouriteModel(
        var favouriteList: RealmList<String> = RealmList()
) : RealmObject() {
}