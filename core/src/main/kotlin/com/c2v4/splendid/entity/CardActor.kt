package com.c2v4.splendid.entity

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.Touchable
import com.badlogic.gdx.scenes.scene2d.ui.*
import com.badlogic.gdx.utils.Scaling
import com.c2v4.splendid.core.model.Card
import com.c2v4.splendid.manager.FontManager
import ktx.actors.onClick

class CardActor(card: Card, skin: Skin) : Table(skin) {

    val buttonTable = Table(skin)
    var showButtons = false

    val buyButton = Button(skin)
    val holdButton = Button(skin)

    init {
        top().defaults().center().pad(5f)
//        background = skin.getDrawable("card-${card.resource.name.toLowerCase()}-${card.tier}")
        background = skin.getDrawable("card-red-1")
        val scale = 0.75f
        val gem = Image(skin, "gem/${card.resource.name.toLowerCase()}")
        gem.setScaling(Scaling.fit)
        add(gem).left().maxHeight(scale * gem.height)

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
            val scale = 0.65f
            resourceTable.add(image).maxHeight(image.height * scale)//.width(image.width * scale).height(image.height * scale)
            resourceTable.add("${it.value}", FontManager.UI_FONT, Color.WHITE)
            resourceTable.row()
        }
        add(resourceTable).colspan(2).expand().left().bottom()

        buttonTable.defaults().padBottom(50f)
        buyButton.add("BUY!", FontManager.UI_FONT, Color.WHITE)
        buttonTable.add(buyButton)
        buttonTable.row().padBottom(0f)
        holdButton.add("HOLD!", FontManager.UI_FONT, Color.WHITE)
        buttonTable.add(holdButton)

        pack()
        buttonTable.pack()
        touchable = Touchable.enabled
    }

    override fun draw(batch: Batch?, parentAlpha: Float) {
        super.draw(batch, parentAlpha)
//        if (showButtons) {
//            buttonTable.x = this.x + buttonTable.width/2
//            buttonTable.y = this.y + buttonTable.height/2
//            buttonTable.draw(batch, parentAlpha)
//        }
    }

    fun showButtons() {
        showButtons = true
    }

    fun hideButtons() {
        showButtons = false
    }

    fun onHold(listener:(InputEvent, Button) -> Unit){

    }

}
