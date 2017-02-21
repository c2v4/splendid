package com.c2v4.splendid.component

import com.c2v4.splendid.controller.PlayerEvent

class CommonModel(var playerEvent: PlayerEvent){
    companion object{
        fun empty():CommonModel{
            return CommonModel(PlayerEvent.NONE)
        }
    }
}