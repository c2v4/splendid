package com.c2v4.splendid.component.cardtable

import com.c2v4.splendid.core.model.Board
import com.c2v4.splendid.core.model.Card
import com.c2v4.splendid.core.model.Noble

class CardTableModel(val nobles:Array<Noble?>, val cards:Array<Array<Card?>>) {
val cardChangeObservers = mutableListOf<(tier:Int, cardPosition:Int, oldCard: Card?, newCard: Card?)->Unit>()
val nobleChangeObservers = mutableListOf<(noblePosition:Int, oldNoble: Noble?, newNoble: Noble?)->Unit>()

    companion object{
        fun empty(): CardTableModel {
            return CardTableModel(arrayOfNulls<Noble>(Noble.Companion.MAX_NOBLES),
            arrayOf(arrayOfNulls<Card>(Board.Companion.NUMBER_OF_CARDS_PER_TIER),
                    arrayOfNulls<Card>(Board.Companion.NUMBER_OF_CARDS_PER_TIER),
                    arrayOfNulls<Card>(Board.Companion.NUMBER_OF_CARDS_PER_TIER)))
        }
    }

    fun addCardChangeObserver(obs:(tier:Int, cardPosition:Int, oldCard: Card?, newCard: Card?)->Unit){
        cardChangeObservers.add(obs)
    }
    fun addNobleChangeObserver(obs:(noblePosition:Int, oldNoble: Noble?, newNoble: Noble?)->Unit){
        nobleChangeObservers.add(obs)
    }
    fun changeCard(tier:Int,position:Int,newCard: Card?){
        val oldCard = cards[tier][position]
        cards[tier][position]=newCard
        cardChangeObservers.forEach { it.invoke(tier,position,oldCard,newCard) }
    }

    fun  changeNoble(position: Int, noble: Noble?) {
        val oldNoble = nobles[position]
        nobles[position] = noble
        nobleChangeObservers.forEach { it.invoke(position,oldNoble,noble) }
    }
}