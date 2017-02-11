package com.c2v4.splendid.core.events

interface EventObserver{
    fun handle(event:DealCardEvent)
}