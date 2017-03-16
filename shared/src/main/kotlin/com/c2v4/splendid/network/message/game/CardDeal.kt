package com.c2v4.splendid.network.message.game

import com.c2v4.splendid.core.model.Card

data class CardDeal(val tier:Int=-1,val position:Int=-1,val card: Card?=null)