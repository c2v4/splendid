package com.c2v4.splendid.entity

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.Touchable
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.utils.Scaling
import com.c2v4.splendid.core.model.Card
import com.c2v4.splendid.manager.FontManager

class CardActor(card: Card, skin: Skin) : Table(skin) {

    init {
        top().defaults().center().pad(5f)
//        background = skin.getDrawable("card-${card.resource.name.toLowerCase()}-${card.tier}")
        background = skin.getDrawable("card-red-1")
        add(Image(skin, "gem/${card.resource.name.toLowerCase()}")).left()

        val label: Label
        if (card.points > 0) {
            label = Label("${card.points}", skin, FontManager.UI_FONT, Color.WHITE)
        } else {
            label = Label("", skin, FontManager.UI_FONT, Color.WHITE)

        }
        add(label).expandX().right()

        row()

        val resourceTable = Table(skin)
        resourceTable.bottom().right()
        resourceTable.defaults().pad(5f)
        card.costs.entries.forEach {
            val image = Image(skin, "gem/${it.key.name.toLowerCase()}")
            image.setScaling(Scaling.fit)
            val scale = 0.75f
            resourceTable.add(image).width(image.width * scale).height(image.height * scale)
            resourceTable.add("${it.value}", FontManager.UI_FONT, Color.WHITE)
            resourceTable.row()
        }
        resourceTable.pack()
        add(resourceTable).colspan(2).expand().left().bottom()
        debugAll()
        pack()
        touchable = Touchable.enabled
        addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                super.clicked(event, x, y)
                println("${card.resource}")
            }
        })
    }
}
