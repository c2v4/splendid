package com.c2v4.splendid.component.cardtable

class CardTableController(val view: CardTableView, val model: CardTableModel) {

    init {
        view.registerController(this)
    }

    fun clickedCard(tier: Int, position: Int) {

    }
}