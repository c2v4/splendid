package com.c2v4.splendid.component.resourcetable

import com.c2v4.splendid.core.model.Resource
import java.util.*

class ResourceTableModel(val resourcesAvailable:MutableMap<Resource, Int>) {

    companion object{
        fun empty():ResourceTableModel{
            return ResourceTableModel(HashMap(Resource.values().map { it to 0 }.toMap())
            )
        }
    }
    var resourceAvailableListeners = mutableListOf<(Resource,Int)->Unit>()
    var resourceSelectedListeners = mutableListOf<(Resource,Int)->Unit>()

    fun addResourceAvailableListener(listener:(Resource,Int)->Unit){
        resourceAvailableListeners.add(listener)
    }
    fun addResourceSelectedListener(listener:(Resource,Int)->Unit){
        resourceSelectedListeners.add(listener)
    }

    fun setResourceAvailable(resources:Map<Resource, Int>){
        resourcesAvailable.putAll(resources)
        resourceAvailableListeners.forEach { listener -> resources.forEach { listener.invoke(it.key,it.value) } }
    }

}