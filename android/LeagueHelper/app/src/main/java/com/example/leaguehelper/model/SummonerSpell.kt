package com.example.leaguehelper.model

class SummonerSpell(var spellId: String, var imageURI: String, var name: String, var cooldown: Int) {
    override fun toString(): String {
        return spellId + " - " + imageURI + " - " + name + " - " + cooldown.toString()
    }
}