package com.c2v4.splendid.component.resourcetable

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.c2v4.splendid.component.CommonModel
import com.c2v4.splendid.component.PlayerEvent
import com.c2v4.splendid.component.playerstable.playersttate.PlayerStateModel
import com.c2v4.splendid.component.playerstable.playersttate.PlayerStateView
import com.c2v4.splendid.core.model.Resource
import com.c2v4.splendid.core.util.isDrawCorrect
import com.c2v4.splendid.core.util.isTakenCorrect
import com.c2v4.splendid.core.util.merge
import com.c2v4.splendid.network.message.game.TakeCoins

class ResourceTableController(val resourceTable: ResourceTableView,
                              val playerStateView: PlayerStateView,
                              val playerStateModel: PlayerStateModel,
                              val resourceTableModel: ResourceTableModel,
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
                    { inputEvent: InputEvent, widget: ClickableResource -> onClickPlayer(it) })
        }
    }

    fun onClickResourceTable(resource: Resource) {
        if (commonModel.playerEvent == PlayerEvent.NONE) {
            commonModel.playerEvent = PlayerEvent.GET_COINS
        }
        if (commonModel.playerEvent == PlayerEvent.GET_COINS) {
            val available = resourceTableModel.resourcesAvailable.getOrElse(resource, { 0 })
            if (available > 0) {
                if (clickedTableResources.containsKey(resource)) {
                    when (clickedTableResources[resource]) {
                        1 -> {
                            when (clickedTableResources.values.sum()) {
                                1 -> {
                                    if (available > 3) {
                                        setTableResource(resource, 2)
                                    } else {
                                        setTableResource(resource, 0)
                                    }
                                }
                                2, 3 -> {
                                    setTableResource(resource, 0)
                                    resetPlayerResources()
                                }
                                else -> throw IllegalStateException()
                            }
                        }
                        2 -> {
                            setTableResource(resource, 0)
                            resetPlayerResources()
                        }
                    }
                } else {
                    when (clickedTableResources.values.sum()) {
                        0, 1 -> setTableResource(resource, 1)
                        2 -> {
                            if (clickedTableResources.size > 1) {
                                setTableResource(resource, 1)
                            }
                        }
                    }
                }
            }
            if (clickedTableResources.values.sum() == 0) {
                commonModel.playerEvent = PlayerEvent.NONE
                commonModel.setActionCorrect(false)
            } else {
                checkActionCorrect()
            }
        }
    }

    private fun checkActionCorrect() {
        commonModel.setActionCorrect(isDrawCorrect(clickedTableResources,
                clickedPlayerResources,
                playerStateModel.wallet))

    }

    private fun resetPlayerResources() {
        clickedPlayerResources.keys.toSet().forEach {
            setPlayerResource(it, 0)
        }
    }

    private fun resetTableResources() {
        clickedTableResources.keys.toSet().forEach {
            setTableResource(it, 0)
        }
    }

    fun onClickPlayer(resource: Resource) {
        if (commonModel.playerEvent == PlayerEvent.GET_COINS && isTakenCorrect(clickedTableResources)) {
            val merged = clickedTableResources.merge(playerStateModel.wallet)
            val amountToReturn = merged.values.sum() - 10
            setPlayerResourceClicked(amountToReturn, merged, resource)
            checkActionCorrect()
        }
        if (commonModel.playerEvent == PlayerEvent.RESERVE) {
            val amountToReturn = playerStateModel.wallet.values.sum() - 10 + if (resourceTableModel.resourcesAvailable.getOrElse(
                    Resource.GOLD,
                    { 0 }) > 0) 1 else 0
            setPlayerResourceClicked(amountToReturn, playerStateModel.wallet, resource)
            commonModel.setActionCorrect(amountToReturn < 1 || clickedPlayerResources.values.sum() == 1)
        }
    }

    private fun setPlayerResourceClicked(amountToReturn: Int,
                                         playerSummedWallet: Map<Resource, Int>,
                                         resource: Resource) {
        if (amountToReturn > 0) {
            val stillToReturn = amountToReturn - clickedPlayerResources.values.sum()
            if (clickedPlayerResources.containsKey(resource)) {
                if (stillToReturn > 0 && playerSummedWallet.getOrDefault(resource,
                        0) >= (clickedPlayerResources.getOrDefault(resource, 0) + 1)) {
                    setPlayerResource(resource,
                            clickedPlayerResources.getOrDefault(resource, 0) + 1)
                } else {
                    setPlayerResource(resource, 0)
                }
            } else {
                if (stillToReturn > 0) {
                    setPlayerResource(resource, 1)
                }
            }
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

    fun setPlayerResource(resource: Resource, amount: Int) {
        if (amount == 0) {
            clickedPlayerResources.remove(resource)
        } else {
            clickedPlayerResources[resource] = amount
        }
        playerStateView.gems[resource]!!.setChosenAmount(amount, Color.RED)
    }

    fun getEvent(): TakeCoins {
        commonModel.playerEvent = PlayerEvent.NONE
        val takeCoins = TakeCoins(clickedTableResources, clickedPlayerResources)
        resetPlayerResources()
        resetTableResources()
        return takeCoins
    }

}