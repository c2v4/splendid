package com.c2v4.splendid.server.network.login

import com.esotericsoftware.kryonet.Connection
import java.math.BigInteger
import java.security.SecureRandom

class ChallengeLogInService(val secureRandom: SecureRandom) : LogInService {
    val challenges = mutableMapOf<Connection, String>()

    fun challenge(connection: Connection): String {
        val randomString = getRandomString()
        challenges.put(connection, randomString)
        return randomString
    }

    private fun getRandomString(): String {
        return BigInteger(130, secureRandom).toString(32)
    }
}