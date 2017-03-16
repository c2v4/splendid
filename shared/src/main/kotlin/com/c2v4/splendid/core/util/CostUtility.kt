package com.c2v4.splendid.core.util

import com.c2v4.splendid.core.model.Resource
import java.util.*

fun canBuy(costs: Map<Resource, Int>, wallet: Map<Resource, Int>): Boolean {
    val stillToPay = costs.entries.sumBy {
        Math.max(it.value - wallet.getOrElse(it.key, { 0 }), 0)
    }
    val gold = wallet.getOrElse(Resource.GOLD, { 0 })
    return stillToPay <= gold
}

fun isTakenCorrect(taken: Map<Resource, Int>): Boolean {
    if (taken.keys.contains(Resource.GOLD)) {
        return false
    }
    when (taken.size) {
        1 -> {
            return taken.values.elementAt(0) == 2
        }
        3 -> {
            return taken.all { it.value == 1 }
        }
        else -> return false
    }
}

fun isDrawCorrect(taken: Map<Resource, Int>,
                  returned: Map<Resource, Int>,
                  wallet: Map<Resource, Int>): Boolean {
    if (!isTakenCorrect(taken)) return false
    val potentialWallet = wallet.merge(taken)
    if (potentialWallet.values.sum() - returned.values.sum() > 10) return false
    if (returned.any { potentialWallet.getOrElse(it.key, { 0 }) - it.value < 0 }) return false
    return true
}

fun <T> Map<T, Int>.merge(map: Map<T, Int>): Map<T, Int> {
    val toReturn = HashMap(this)
    map.forEach {
        toReturn[it.key] = toReturn.getOrDefault(it.key, 0) + it.value
    }
    return toReturn
}

fun <T> Map<T, Int>.subtractPositive(map: Map<T, Int>): Map<T, Int> {
    val toReturn = HashMap(this)
    map.forEach {
        val newValue = getOrElse(it.key, { throw IllegalArgumentException() }) - it.value
        when {
            newValue > 0 -> toReturn[it.key] = newValue
            newValue == 0 -> toReturn.remove(it.key)
            else -> throw IllegalArgumentException()
        }
    }
    return toReturn
}

fun <T> Map<T, Int>.haveValues(values: Map<T, Int>): Boolean {
    return values.all { getOrElse(it.key, { 0 }) - it.value >= 0 }
}