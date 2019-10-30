package com.deepak.kontacts.repository

import androidx.lifecycle.LiveData
import com.deepak.kontacts.db.MyContactModel
import com.deepak.kontacts.viewmodel.asLiveData
import io.realm.Realm
import io.realm.RealmResults
import io.realm.kotlin.where

class RealmKontactsRepository(private val realm: Realm?) {

    fun getLiveKontacts(): LiveData<RealmResults<MyContactModel>> {
        val query = realm?.where<MyContactModel>()
                ?.findAllAsync()
        return query?.asLiveData()!!
    }

    fun saveAllKontacts(list: MutableList<MyContactModel>) {
        realm?.executeTransaction {
            it.insertOrUpdate(list)
        }
    }

    fun deleteAllKontact() {
        realm?.executeTransaction {
            it.where<MyContactModel>()
                    .findAllAsync()
                    ?.deleteAllFromRealm()
        }
    }
}