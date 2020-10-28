package com.example.leaguehelper.model

class Champion(var championId: String, var imageURI: String, var name: String) {
    override fun toString(): String {
        return championId + " - " + imageURI + " - " + name
    }
}