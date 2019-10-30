package com.deepak.kontacts.viewmodel

import androidx.lifecycle.ViewModel
import com.deepak.kontacts.db.MyContactModel
import com.deepak.kontacts.repository.RealmKontactsRepository
import io.realm.Realm

class RealmKontactsViewModel : ViewModel() {
    private val realm: Realm? = Realm.getDefaultInstance()
    private val repository = RealmKontactsRepository(realm)

    fun getLiveKontacts() = repository.getLiveKontacts()

    fun saveAllKontacts(list: MutableList<MyContactModel>) = repository.saveAllKontacts(list)

    fun deleteAllKontacts() = repository.deleteAllKontact()

    override fun onCleared() {
        super.onCleared()
        realm?.removeAllChangeListeners()
        realm?.close()
    }
}