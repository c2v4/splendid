package com.c2v4.splendid.component.resourcetable

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.ui.Widget
import com.c2v4.splendid.component.CommonModel
import com.c2v4.splendid.component.playerstable.playersttate.PlayerStateView
import com.c2v4.splendid.controller.PlayerEvent
import com.c2v4.splendid.core.model.Resource

class ResourceTableController(val resourceTable: ResourceTableView,
                              val playerStateView: PlayerStateView,
                              val model: ResourceTableModel,
                              val commonModel: CommonModel) {
    var clickedTableResources = mutableMapOf<Resource, Int>()
    var clickedPlayerResources = mutableMapOf<Resource, Int>()

    init {
        Resource.WITHOUT_GOLD.forEach {
            resourceTable.onClick(it,
                    { inputEvent: InputEvent, clickableResource: ClickableResource ->
                        onClickResourceTable(it)
                    })
        }
        Resource.values().forEach {
            playerStateView.onClick(it,
                    { inputEvent: InputEvent, widget: Widget -> onClickPlayer(it) })
        }
    }

    fun onClickResourceTable(resource: Resource) {
        println("Clicked resource on Available Resources: " + resource)
        if (commonModel.playerEvent == PlayerEvent.NONE) {
            commonModel.playerEvent = PlayerEvent.GET_COINS
        }
        if (commonModel.playerEvent == PlayerEvent.GET_COINS) {
            if (clickedTableResources.containsKey(resource)) {
                when (clickedTableResources[resource]) {
                    1 -> {
                        when (clickedTableResources.values.sum()) {
                            1 -> {
                                setTableResource(resource, 2)
                            }
                            2 -> {

                            }
                            3 -> {

                            }
                            else -> throw IllegalStateException()
                        }
                    }
                    2 -> {

                    }
                }
            } else {
                when (clickedTableResources.values.sum()) {
                    0 -> setTableResource(resource, 1)
                }
            }
            if (clickedTableResources.values.sum() < 2) {

            }
        }
    }

    fun onClickPlayer(resource: Resource) {
        if (commonModel.playerEvent == PlayerEvent.NONE) {
            commonModel.playerEvent = PlayerEvent.GET_COINS
        }
        if (commonModel.playerEvent == PlayerEvent.GET_COINS) {

        }
    }

    fun setTableResource(resource: Resource, amount: Int) {

        if (amount == 0) {
            clickedTableResources.remove(resource)
        } else {
            clickedTableResources[resource] = amount
        }
        resourceTable.clickableResources[resource]!!.setChosenAmount(amount, Color.GREEN)
    }

}