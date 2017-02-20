package com.c2v4.splendid.cardtable

import com.c2v4.splendid.core.model.Board
import com.c2v4.splendid.core.model.Card
import com.c2v4.splendid.core.model.Noble

class CardTableModel(val nobles:Array<Noble?>,val cards:Array<Array<Card?>>) {
val cardChangeObservers = mutableListOf<(tier:Int,cardPosition:Int,oldCard:Card?,newCard:Card?)->Unit>()
val nobleChangeObservers = mutableListOf<(noblePosition:Int,oldNoble:Noble?,newNoble:Noble?)->Unit>()

    companion object{
        fun empty():CardTableModel{
            return CardTableModel(kotlin.arrayOfNulls<Noble>(Noble.MAX_NOBLES),
            arrayOf(kotlin.arrayOfNulls<Card>(Board.NUMBER_OF_CARDS_PER_TIER),
                    kotlin.arrayOfNulls<Card>(Board.NUMBER_OF_CARDS_PER_TIER),
                    kotlin.arrayOfNulls<Card>(Board.NUMBER_OF_CARDS_PER_TIER)))
        }
    }

    fun addCardChangeObserver(obs:(tier:Int,cardPosition:Int,oldCard:Card?,newCard:Card?)->Unit){
        cardChangeObservers.add(obs)
    }
    fun addNobleChangeObserver(obs:(noblePosition:Int,oldNoble:Noble?,newNoble:Noble?)->Unit){
        nobleChangeObservers.add(obs)
    }
    fun changeCard(tier:Int,position:Int,newCard: Card?){
        val oldCard = cards[tier][position]
        cards[tier][position]=newCard
        cardChangeObservers.forEach { it.invoke(tier,position,oldCard,newCard) }
    }
}