package com.example.leaguehelper.model

class Summoner (
    var name: String,
    var puuid: String = "",
    var id: String,
    var accountId: String = "") {


    private val champBaseUrl = "http://ddragon.leagueoflegends.com/cdn/10.21.1/img/champion/"
    private val spellBaseUrl = "http://ddragon.leagueoflegends.com/cdn/10.21.1/img/spell/"

    var hasCosmicInsight = false
    private lateinit var champion: Champion
    private lateinit var spell1: SummonerSpell
    private lateinit var spell2: SummonerSpell

    fun setChampion(champion: Champion?) {
        if (champion != null) {
            this.champion = champion
        }
    }

    fun getChampion() : Champion {
        return champion
    }

    fun setSummonerSpell (spell1: SummonerSpell?, spell2: SummonerSpell?) {
        if (spell1 != null && spell2 != null) {
            this.spell1 = spell1
            this.spell2 = spell2
        }
    }

    fun getSpell1Image() : String {
        return spellBaseUrl + spell1.imageURI
    }

    fun getSpell2Image() : String {
        return spellBaseUrl + spell2.imageURI
    }

    fun getSpell1() : SummonerSpell {
        return spell1
    }

    fun getSpell2() : SummonerSpell {
        return spell2
    }

    fun getChampImage() : String {
        return champBaseUrl + champion.imageURI
    }

    override fun toString(): String {
        return name + " - " + id + " - " + champion.toString() + " - " + spell1.toString() + " - " + spell2.toString()
    }

}
