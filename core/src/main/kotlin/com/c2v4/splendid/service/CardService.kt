package com.c2v4.splendid.service

import com.c2v4.splendid.core.model.Card
import com.c2v4.splendid.entity.CardActor
import com.github.czyzby.autumn.annotation.Component
import com.github.czyzby.autumn.annotation.Inject
import com.github.czyzby.autumn.mvc.component.asset.AssetService
import com.github.czyzby.autumn.mvc.component.ui.SkinService

@Component
class CardService {

    @Inject
    private val skinService: SkinService? = null

    @Inject
    private val assetService: AssetService? = null

    fun getCardActor(card: Card): CardActor {
        return CardActor(card, skinService!!.skin)
    }
}
