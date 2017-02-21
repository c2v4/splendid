package com.c2v4.splendid.component.resourcetable

import com.c2v4.splendid.core.model.Resource
import java.util.*

class ResourceTableModel(val resourcesAvailable:MutableMap<Resource, Int>,val resourcesSelected:MutableMap<Resource, Int>) {

    companion object{
        fun empty():ResourceTableModel{
            return ResourceTableModel(HashMap(Resource.values().map { it to 0 }.toMap()),
            HashMap(Resource.values().map { it to 0 }.toMap()))
        }
    }
    var resourceAvailableListeners = mutableListOf<(Int)->Unit>()
    var resourceSelectedListeners = mutableListOf<(Int)->Unit>()

    fun addResourceAvailableListener(listener:(Int)->Unit){
        resourceAvailableListeners.add(listener)
    }
    fun addResourceSelectedListener(listener:(Int)->Unit){
        resourceSelectedListeners.add(listener)
    }

}