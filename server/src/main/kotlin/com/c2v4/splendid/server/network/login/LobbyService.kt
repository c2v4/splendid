package com.c2v4.splendid.server.network.login

interface LobbyService{
    fun join(player: String, lobbyId: Int)

}