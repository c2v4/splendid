package com.c2v4.splendid.component.resourcetable

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.Touchable
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.c2v4.splendid.core.model.Resource
import com.c2v4.splendid.manager.FontManager
import ktx.actors.onClick


class ClickableResource(skin: Skin, val resource: Resource) : Table(skin) {
    val label = Label("", skin, FontManager.UI_FONT, Color.WHITE)

    init {
        setBackground("gem/${resource.name.toLowerCase()}")
        right().bottom()
        add(label)
        touchable = Touchable.enabled
    }

    fun setOnClick(listener: (InputEvent, ClickableResource) -> Unit) {
        onClick(listener)
    }

    fun setChosenAmount(i: Int) {
        label.setText("$i")
    }
}