package com.c2v4.splendid.model

import com.c2v4.splendid.core.model.Resource
import java.util.*

class ResourceTableModel() {
    val resourcesAvailable: MutableMap<Resource, Int> = HashMap(Resource.values().map { it to 0 }.toMap())
    val resourcesSelected: MutableMap<Resource, Int> = HashMap(Resource.values().map { it to 0 }.toMap())

    var resourceAvailableListeners = mutableListOf<(Int)->Unit>()
    var resourceSelectedListeners = mutableListOf<(Int)->Unit>()

    fun addResourceAvailableLIstener(listener:(Int)->Unit){
        resourceAvailableListeners.add(listener)
    }
    fun addResourceSelectedLIstener(listener:(Int)->Unit){
        resourceSelectedListeners.add(listener)
    }

}