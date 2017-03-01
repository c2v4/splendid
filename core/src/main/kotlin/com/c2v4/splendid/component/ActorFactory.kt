package com.c2v4.splendid.component

import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.c2v4.splendid.core.model.Card
import com.c2v4.splendid.core.model.Noble
import com.c2v4.splendid.entity.CardActor

fun getNoble(noble: Noble?, skin: Skin): Actor {
    return Image(skin, "card-empty")
}

fun getCard(card: Card?, skin: Skin): Actor {
    if (card == null) return Image(skin, "card-empty")
    return CardActor(card,skin)
}