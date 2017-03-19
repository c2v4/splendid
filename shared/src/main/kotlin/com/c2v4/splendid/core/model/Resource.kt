package com.c2v4.splendid.core.model

enum class Resource {
    GREEN, RED, BLACK, WHITE, BLUE, GOLD;
    companion object{
        val WITHOUT_GOLD = Resource.values().filter { it!=GOLD }
    }
}
