package com.c2v4.splendid.component.reservedcard

import com.c2v4.splendid.core.model.Card

class ReservedCardsModel(val cards:Array<Card?>){

    val cardChangeObserver:MutableList<(position:Int,card:Card)->Unit> = mutableListOf()
    companion object{
        fun empty():ReservedCardsModel{
            return ReservedCardsModel(kotlin.arrayOfNulls<Card>(3))
        }
    }

}