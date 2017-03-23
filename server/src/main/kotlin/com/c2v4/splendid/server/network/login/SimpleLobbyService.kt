package com.c2v4.splendid.server.network.login

class SimpleLobbyService(val gameService: GameService, val numberOfPlayers: Int) : LobbyService {
    val lobbies = mutableMapOf<Int, MutableSet<String>>()

    override fun join(player: String, lobbyId: Int) {
        val lobby = lobbies.getOrPut(lobbyId, { mutableSetOf() })
        lobby.add(player)
        checkFull(lobbyId)
    }

    private fun checkFull(lobbyId: Int) {
        val lobby = lobbies.getOrElse(lobbyId, { throw IllegalStateException() })
        if (lobby.size > numberOfPlayers - 1) {
            lobbies.remove(lobbyId)
            gameService.commenceGame(lobby)
        }
    }


}