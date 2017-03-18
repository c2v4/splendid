package com.c2v4.splendid.entity

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.Touchable
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.utils.Scaling
import com.c2v4.splendid.core.model.Noble
import com.c2v4.splendid.manager.FontManager

class NobleActor(noble: Noble,
                 skin: Skin) : Table(){

    init {
        val resourceTable = Table(skin)
        resourceTable.bottom().right()
        resourceTable.defaults().pad(5f)
        noble.resources.entries.forEach {
            val image = Image(skin, "gem/${it.key.name.toLowerCase()}")
            image.setScaling(Scaling.fit)
            val scale = 0.75f
            resourceTable.add(image).prefWidth(image.width*scale).prefHeight(image.height*scale)//.width(image.width * scale).height(image.height * scale)
            resourceTable.add("${it.value}", FontManager.UI_FONT, Color.WHITE)
            resourceTable.row()
        }
        add(resourceTable).expand().left().bottom()
        bottom().left()
        pack()
        touchable = Touchable.enabled
    }
}