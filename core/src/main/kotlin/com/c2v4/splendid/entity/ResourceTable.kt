package com.c2v4.splendid.entity

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.c2v4.splendid.core.model.Resource
import com.c2v4.splendid.manager.FontManager
import com.c2v4.splendid.model.ResourceTableModel

class ResourceTable(skin: Skin, resourceTableModel: ResourceTableModel) : Table(skin) {
    val amounts = Resource.values().map { it to Label("0", skin, FontManager.UI_FONT, Color.WHITE) }.toMap()
    val clickableResources = Resource.values().map { it to ClickableResource(skin, it) }.toMap()

    init {
        defaults().pad(5f)
        Resource.values().forEach {
            resourceTableModel.addResourceAvailableLIstener { i -> setAmount(it, i) }
            resourceTableModel.addResourceSelectedLIstener { i -> setSelected(it, i) }
        }
        amounts.forEach {
            row()
            val resource = clickableResources[it.key]
            add(resource)
            add(it.value)
        }
        pack()
    }

    private fun setSelected(resource: Resource, amount: Int) {
        clickableResources[resource]!!.setChosenAmount(amount)
    }

    private fun setAmount(resource: Resource, amount: Int) {
        amounts[resource]!!.setText("$amount")
    }
}