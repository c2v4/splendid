package com.c2v4.splendid.controller

import com.c2v4.splendid.core.model.Resource
import com.c2v4.splendid.entity.ResourceTable

class ResourlceTableController(val resourceTable: ResourceTable, val boardController: BoardController) {
    var clickedResources = mutableMapOf<Resource, Int>()

    fun onClick(resource: Resource) {
        if (boardController.playerEvent == PlayerEvent.NONE) {
            boardController.playerEvent = PlayerEvent.GET_COINS
        }
        if (boardController.playerEvent == PlayerEvent.GET_COINS) {
            if (clickedResources.contains(resource)) {

            } else {
                if (clickedResources.values.sum() < 3) {
                    clickedResources.put(resource, 1)
                }
            }
        }
    }
}