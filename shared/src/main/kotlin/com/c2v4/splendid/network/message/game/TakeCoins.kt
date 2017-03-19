package com.c2v4.splendid.network.message.game

import com.c2v4.splendid.core.model.Resource
import java.util.*

data class TakeCoins(val taken: HashMap<Resource, Int> = HashMap(),
                     val returned: HashMap<Resource, Int> = HashMap()){
    constructor(taken: Map<Resource, Int>, returned:Map<Resource,Int>) : this(HashMap(taken), HashMap(returned))
}

//class TakeCoinsSerializer : Serializer<TakeCoins>() {
//
//    override fun read(kryo: Kryo, input: Input, type: Class<TakeCoins>): TakeCoins {
//        val balance = mutableMapOf<Resource,Int>()
//        val returned = mutableMapOf<Resource,Int>()
//        (0..input.readInt()).forEach { balance.put(Resource.values()[input.readInt()],input.readInt()) }
//        (0..input.readInt()).forEach { returned.put(Resource.values()[input.readInt()],input.readInt()) }
//        return TakeCoins(balance,returned)
//    }
//
//    override fun write(kryo: Kryo, output: Output, takeCoins: TakeCoins) {
//        output.writeInt(takeCoins.balance.size)
//        takeCoins.balance.forEach { output.writeInt(it.key.ordinal)
//        output.writeInt(it.value)}
//        output.writeInt(takeCoins.returned.size)
//        takeCoins.returned.forEach { output.writeInt(it.key.ordinal)
//            output.writeInt(it.value)}
//    }
//
//}