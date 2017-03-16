package com.c2v4.splendid.server.data

import com.c2v4.splendid.core.model.Card
import com.c2v4.splendid.core.model.Noble
import com.c2v4.splendid.core.model.Resource
import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVRecord
import java.io.BufferedReader
import java.io.InputStreamReader

class InitialDataLoader {

    val initialCards:List<List<Card>> = loadCards()
    val initialNobles:List<Noble> = loadNobles()

    fun initialBank(numberOfPlayers:Int):Map<Resource,Int>{
        val initialBank = mutableMapOf(Resource.GOLD to 5)
        initialBank.putAll(Resource.values().filter { it != Resource.GOLD }.map { it to numberOfPlayers+3 })
        return initialBank
    }

    private fun loadNobles(): List<Noble> {
        val nobles = mutableListOf<Noble>()
        javaClass.getResourceAsStream("/initialNobles.csv").use {
            BufferedReader(InputStreamReader(it)).use {
                val iterator = CSVFormat.DEFAULT.withHeader().parse(it)
                iterator.forEach {
                    nobles.add(
                            Noble(getCosts(it))
                    )
                }
            }
        }
        return nobles
    }

    private fun loadCards(): List<MutableList<Card>> {
        val cardsByTier = mutableMapOf<Int, MutableList<Card>>()
        javaClass.getResourceAsStream("/initialCards.csv").use {
            BufferedReader(InputStreamReader(it)).use {
                val iterator = CSVFormat.DEFAULT.withHeader().parse(it)
                iterator.forEach {
                    val tier = it.get("level").toInt()
                    cardsByTier.getOrPut(tier, { mutableListOf() }).add(
                            Card(tier, if (it.get("points").isBlank()) {
                                0
                            } else {
                                it.get("points").toInt()
                            }, getCosts(it), Resource.valueOf(it["color"]))
                    )
                }
            }
        }
        return cardsByTier.values.toList().reversed()
    }

    private fun getCosts(record: CSVRecord): Map<Resource, Int> {
        val costs = mutableMapOf<Resource, Int>()
        listOf("WHITE", "BLACK", "BLUE", "GREEN", "RED").forEach {
            val value = record[it]
            if (value != null && !value.isBlank()) {
                costs.put(Resource.valueOf(it), value.toInt())
            }
        }
        return costs
    }


}