package com.deepak.kontacts

import android.app.Application
import io.realm.Realm
import io.realm.RealmConfiguration

class KontactsApp : Application() {

    override fun onCreate() {
        super.onCreate()

        Realm.init(this)
        val configRealm = RealmConfiguration.Builder()
                .name("kontacts-realm.realm")
                .deleteRealmIfMigrationNeeded()
                .schemaVersion(0)
                .build()
        Realm.setDefaultConfiguration(configRealm)
    }

}