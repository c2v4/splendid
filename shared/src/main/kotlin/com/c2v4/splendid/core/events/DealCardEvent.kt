package com.c2v4.splendid.core.events

import com.c2v4.splendid.core.model.Card

data class DealCardEvent(val card: Card, val tier: Int)