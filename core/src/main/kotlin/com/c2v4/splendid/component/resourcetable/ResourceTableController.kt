package com.c2v4.splendid.component.resourcetable

import com.c2v4.splendid.component.CommonModel
import com.c2v4.splendid.core.model.Resource
import com.c2v4.splendid.controller.PlayerEvent

class ResourceTableController(val resourceTable: ResourceTableView, val model: ResourceTableModel,val commonModel: CommonModel) {
    var clickedResources = mutableMapOf<Resource, Int>()

    fun onClick(resource: Resource) {
        if (commonModel.playerEvent == PlayerEvent.NONE) {
            commonModel.playerEvent = PlayerEvent.GET_COINS
        }
        if (commonModel.playerEvent == PlayerEvent.GET_COINS) {
            if (clickedResources.contains(resource)) {

            } else {
                if (clickedResources.values.sum() < 3) {
                    clickedResources.put(resource, 1)
                }
            }
        }
    }
}